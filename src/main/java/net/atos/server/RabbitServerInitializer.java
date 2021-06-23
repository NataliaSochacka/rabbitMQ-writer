package net.atos.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitServerInitializer extends ChannelInitializer<SocketChannel> {

    private final RabbitTemplate rabbitTemplate;

    public RabbitServerInitializer(RabbitTemplate rabbitTemplate) {

        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        System.out.println("initChannel()");

        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("handler", new RabbitServerHandler(this.rabbitTemplate));
    }
}
