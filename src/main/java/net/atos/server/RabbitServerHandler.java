package net.atos.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitServerHandler extends SimpleChannelInboundHandler<String> {

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private RabbitTemplate rabbitTemplate;

    @Value("${EXCHANGE}")
    private String EXCHANGE;

    @Value("${ROUTING_KEY}")
    private String ROUTING_KEY;

    public RabbitServerHandler(RabbitTemplate rabbitTemplate){

        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) {

        Channel incoming = channelHandlerContext.channel();

        for (Channel channel : channels) {

            channel.write("Server: " + incoming.remoteAddress() + " connected");
        }

        channels.add(channelHandlerContext.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) {

        Channel incoming = channelHandlerContext.channel();

        for (Channel channel : channels) {

            channel.write("Server: " + incoming.remoteAddress() + " disconnected");
        }

        channels.add(channelHandlerContext.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String message) throws Exception {

        Channel incoming = channelHandlerContext.channel();

        for (Channel channel : channels) {

            if (channel != incoming) {

                channel.write("Sending: " + incoming.remoteAddress() + " " + message);
                rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, message);
            }
        }
    }
}
