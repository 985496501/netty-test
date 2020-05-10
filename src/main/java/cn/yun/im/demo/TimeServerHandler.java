package cn.yun.im.demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Ignore any message we received, sent an message as noon as the connection is established,
 * which contains 32-byte integer, without receiving any requests and closes the connection
 * once the message is sent.
 *
 * In this section, you will learn how to construct and send messages, and close the connection
 * on completion.
 *
 * @author: Liu Jinyun
 * @date: 2020/5/11/0:15
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * Because we are going to ignore any received requests, but to send a message
     * as soon as the connection is established. We override channelActive().
     * This method is invoked when the connection is established and is ready to generate traffic.
     * Let's write a 32-bit integer to represent the time.
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        // To send a new message, wo should allocate a new buffer which contains a new message.
        // 32-bit needs at least 4 bytes. int has 4 bytes.
        final ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        // ByteBuf has two pointers. One is for read, another is for write.
        // They work respectively. ChannelFuture represents an I/O operation which is not yet occurred.
        final ChannelFuture f = ctx.writeAndFlush(time);
        f.addListener(ChannelFutureListener.CLOSE);
//        f.addListener(new ChannelFutureListener() {
//            // ChannelFuture is complete, you should call the close()
//            @Override
//            public void operationComplete(ChannelFuture future) {
//                assert f == future;
//                ctx.close();
//            }
//        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
