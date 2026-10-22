package com.mojang.blaze3d.vertex;

import java.nio.ByteBuffer;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.system.MemoryUtil;

@Environment(EnvType.CLIENT)
public class ByteBufferBuilder$Result implements AutoCloseable {
   private final long offset;
   private final int size;
   private final int generation;
   private boolean closed;

   private ByteBufferBuilder$Result(final ByteBufferBuilder this$0, final long offset, final int size, final int generation) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.offset = offset;
      this.size = size;
      this.generation = generation;
   }

   public ByteBuffer byteBuffer() {
      if (!this.this$0.isValid(this.generation)) {
         throw new IllegalStateException("Buffer is no longer valid");
      } else {
         return MemoryUtil.memByteBuffer(this.this$0.pointer + this.offset, this.size);
      }
   }

   public int size() {
      return this.size;
   }

   @Override
   public void close() {
      if (!this.closed) {
         this.closed = true;
         if (this.this$0.isValid(this.generation)) {
            this.this$0.freeResult();
         }
      }
   }
}
