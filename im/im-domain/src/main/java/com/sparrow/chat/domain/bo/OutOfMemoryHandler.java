package com.sparrow.chat.domain.bo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * Recent access records:
 * Created at:
 * io.netty.buffer.PooledByteBufAllocator.newHeapBuffer(PooledByteBufAllocator.java:346)
 * io.netty.buffer.AbstractByteBufAllocator.heapBuffer(AbstractByteBufAllocator.java:168)
 * io.netty.buffer.AbstractByteBufAllocator.heapBuffer(AbstractByteBufAllocator.java:159)
 * io.netty.handler.codec.compression.ZlibDecoder.prepareDecompressBuffer(ZlibDecoder.java:66)
 * io.netty.handler.codec.compression.JdkZlibDecoder.decode(JdkZlibDecoder.java:229)
 * io.netty.handler.codec.ByteToMessageDecoder.decodeRemovalReentryProtection(ByteToMessageDecoder.java:501)
 * io.netty.handler.codec.ByteToMessageDecoder.callDecode(ByteToMessageDecoder.java:440)
 * io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:276)
 * io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
 * io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
 * io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
 * io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410)
 * io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
 * io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
 * io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919)
 * io.netty.channel.embedded.EmbeddedChannel.writeInbound(EmbeddedChannel.java:343)
 * io.netty.handler.codec.http.websocketx.extensions.compression.DeflateDecoder.decompressContent(DeflateDecoder.java:119)
 * io.netty.handler.codec.http.websocketx.extensions.compression.DeflateDecoder.decode(DeflateDecoder.java:80)
 * io.netty.handler.codec.http.websocketx.extensions.compression.PerMessageDeflateDecoder.decode(PerMessageDeflateDecoder.java:87)
 * io.netty.handler.codec.http.websocketx.extensions.compression.PerMessageDeflateDecoder.decode(PerMessageDeflateDecoder.java:31)
 * io.netty.handler.codec.MessageToMessageDecoder.channelRead(MessageToMessageDecoder.java:88)
 * io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
 * io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
 * io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
 * io.netty.handler.codec.ByteToMessageDecoder.fireChannelRead(ByteToMessageDecoder.java:324)
 * io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:296)
 * io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
 * io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
 * io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
 * io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410)
 * io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
 * io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
 * io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919)
 * io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:163)
 * io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:714)
 * io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:650)
 * io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:576)
 * io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:493)
 * io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989)
 * io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
 * io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
 * java.lang.Thread.run(Thread.java:748)
 */
public class OutOfMemoryHandler extends ChannelInboundHandlerAdapter {
    PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);

    @Override public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof BinaryWebSocketFrame) {
            ByteBuf req = ((BinaryWebSocketFrame) msg).content();
            byte[] body = new byte[req.readableBytes()];
            new Thread(new Runnable() {
                @Override public void run() {
                    ByteBuf resp = allocator.heapBuffer(body.length);
                    resp.writeBytes(body);
                    ctx.writeAndFlush(resp);
                }
            }).start();
        }
    }
}