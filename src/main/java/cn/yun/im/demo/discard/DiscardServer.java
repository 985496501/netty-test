package cn.yun.im.demo.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * create a discard server, ignoring any received message.
 *
 * @author: Liu Jinyun
 * @date: 2020/5/10/19:19
 */
public class DiscardServer {
    /**
     * 定义服务器的端口号
     */
    private final int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        // NioEventLoopGroup: thread group

        // accepts an incoming connection
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // handles the traffic of the accepted connection
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        //  https://netty.io/wiki/user-guide-for-4.x.html

        try {
            // ServerBootstrap is a helper class to set up a server, you can use a channel directly.
            // Note that that's a tedious process, you do not need to that in most cases.
            ServerBootstrap b = new ServerBootstrap();
            // handle events and IO for ServerChannel, Channel
            // workerGroup is b's EventLoopGroup childGroup and bossGroup is b's EventLoopGroup group
            // return ServerBootstrap.
            b.group(bossGroup, workerGroup)
                    // NioServerSocketChannel which is used to instantiate a new channel to accept incoming connection.
                    // return ServerBootstrap.
                    // ChannelFactory<NioServerSocketChannel> channelFactory = new ReflectiveChannelFactory<NioServerSocketChannel>
                    // Constructor<NioServerSocketChannel> constructor
                    .channel(NioServerSocketChannel.class)
//                    .channelFactory(NioServerSocketChannel.class)
                    // The handler specified here will always be evaluated by a newly accept channel,
                    // ChannelInitializer is a special handler to help user to configure a new channel.
                    // It's most likely that you want to configure ChannelPipeline of the new Channel
                    // by adding some handlers such as DiscardServerHandler to implement your network application.
                    // As application gets complicated, you add more handlers to the pipeline,
                    // and extract this anonymous class into a top-level class eventually.

                    // need: ChannelHandler   ||    childHandler <ChannelHandler>
                    // SocketChannel: A TCP/IP socket {@link Channel}. pipeline() all channel have.
                    // ChannelHandler childHandler = new ChannelInitializer<SocketChannel>();
                    // pipeline.addLast(ChannelHandler...) insert ChannelHandler.
                    // ServerBootstrap
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    // refer to ChannelOption and ChannelConfig
                    // option is for NioServerSocketChannel to accept incoming connections.
                    // private final Map<ChannelOption<?>, Object> options = new LinkedHashMap<ChannelOption<?>, Object>();
                    // ServerBootstrap
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // childOption is for channels accepted by the parent ServerChannel
                    // Map<ChannelOption<?>, Object> childOptions = new LinkedHashMap<ChannelOption<?>, Object>();
                    // ServerBootstrap
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        try {
            new DiscardServer(8080).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
