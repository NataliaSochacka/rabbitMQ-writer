package net.atos.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RabbitServer implements CommandLineRunner {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static Logger logger = LoggerFactory.getLogger(RabbitServer.class);

    @Override
    public void run(String... args) throws Exception {

        logger.info("Start Server.run()");

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            logger.info("Start Server.try()");

            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new RabbitServerInitializer(rabbitTemplate));

            serverBootstrap.bind(8000).sync().channel().closeFuture().sync();
            logger.info("Koniec Server.try()");
        }
        finally {

            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
