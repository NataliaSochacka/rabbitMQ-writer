package net.atos.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitHandlerUnitTest {

    EmbeddedChannel embeddedChannel = new EmbeddedChannel(new RabbitHandler());

    @Test
    public void testHandler(){

        String message = "test";

        assertTrue(embeddedChannel.writeOutbound(message));

        assertEquals(embeddedChannel.readOutbound(), message);

        assertFalse(embeddedChannel.finish());
    }

    static class RabbitHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {

            ctx.channel().writeAndFlush(message);
        }
    }
}
