package com.sparrow.chat.domain.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static Logger logger = LoggerFactory.getLogger(OutOfMemoryHandler.class);
    PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) msg;
            ByteBuf byteBuf = binaryWebSocketFrame.content();
            /**
             * java.lang.UnsupportedOperationException: null
             * 	at io.netty.buffer.CompositeByteBuf.array(CompositeByteBuf.java:784)
             * 	at com.sparrow.chat.domain.netty.WebSocketFrameHandler.unsafeDuplicate(WebSocketFrameHandler.java:90)
             * 	at com.sparrow.chat.domain.netty.WebSocketFrameHandler.writeAndFlush(WebSocketFrameHandler.java:145)
             * 	at com.sparrow.chat.domain.netty.WebSocketFrameHandler.channelRead0(WebSocketFrameHandler.java:79)
             * 	at com.sparrow.chat.domain.netty.WebSocketFrameHandler.channelRead0(WebSocketFrameHandler.java:38)
             * 	可能会报错
             */
            byte[] bytes = byteBuf.array();
            String content1 = ByteBufUtil.hexDump(bytes, 0, 256);
            String content2 = ByteBufUtil.hexDump(bytes, 0, byteBuf.capacity());
            logger.info("msg content address-hashcode:{},byte-length:{}M,capacity:{},readable-readableBytes:{},\nc-content:{},\na-content:{}\n\n", byteBuf.hashCode(), bytes.length / 1024 / 1024, byteBuf.capacity(), byteBuf.readableBytes(), content2, content1);

            ByteBuf req = ((BinaryWebSocketFrame) msg).content();
            byte[] body = new byte[req.readableBytes()];
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ByteBuf resp = allocator.heapBuffer(body.length);
                    resp.writeBytes(body);
                    ctx.writeAndFlush(resp);
                }
            }).start();
        }
    }
}
