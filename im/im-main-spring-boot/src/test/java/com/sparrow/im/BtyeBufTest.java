package com.sparrow.im;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.ReferenceCountUtil;

public class BtyeBufTest {

    private static void writeByteBufInfo(ByteBuf byteBuf, int i) {
        System.out.print("capacity:" + byteBuf.capacity());
        System.out.print(",writableBytes:" + byteBuf.writableBytes());
        System.out.print(",readableBytes:" + byteBuf.readableBytes());
        System.out.print(",writerIndex:" + byteBuf.writerIndex());
        System.out.print(",readerIndex:" + byteBuf.readerIndex());
        System.out.println("getByte:" + byteBuf.getByte(i));
    }

    public static void main(String[] args) {
        PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);
        ByteBuf byteBuf = null;
        try {
            byteBuf = allocator.directBuffer(4);  // 设置初始容量和最大容量
            for (int i = 0; i < 8; i++) {
                if (!byteBuf.isWritable(i + 1)) {
                    byteBuf.ensureWritable(i + 1);  // 触发自动扩容
                }
                //byteBuf.writeByte((byte) i);
                byteBuf.setByte(i, (byte) i);
                writeByteBufInfo(byteBuf, i);
            }

            for (int i = 0; i < 10; i++) {
                System.out.println("getByte:" + byteBuf.getByte(i));
                writeByteBufInfo(byteBuf, i);
            }
        } finally {
            if (byteBuf != null) {
                ReferenceCountUtil.release(byteBuf);  // 确保资源释放‌:ml-citation{ref="1,2" data="citationList"}
            }
        }
    }
}
