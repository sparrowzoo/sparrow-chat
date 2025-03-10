package com.sparrow.im;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.ReferenceCountUtil;

public class BtyeBufTest {

    public static void main(String[] args) {
        PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);
        ByteBuf byteBuf = null;
        try {
            byteBuf = allocator.directBuffer(4);  // 设置初始容量和最大容量
            for (int i = 0; i < 1025; i++) {
                if (!byteBuf.isWritable(i+1)) {
                    byteBuf.ensureWritable(i+1);  // 触发自动扩容
                }
                byteBuf.setByte(i, (byte) i);
            }
        } finally {
            if (byteBuf != null) {
                ReferenceCountUtil.release(byteBuf);  // 确保资源释放‌:ml-citation{ref="1,2" data="citationList"}
            }
        }
    }
}
