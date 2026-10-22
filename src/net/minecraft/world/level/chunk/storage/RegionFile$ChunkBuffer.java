package net.minecraft.world.level.chunk.storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import net.minecraft.util.profiling.jfr.JvmProfiler;
import net.minecraft.world.level.ChunkPos;

class RegionFile$ChunkBuffer extends ByteArrayOutputStream {
   private final ChunkPos pos;

   public RegionFile$ChunkBuffer(final RegionFile this$0, final ChunkPos pos) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(8096);
      super.write(0);
      super.write(0);
      super.write(0);
      super.write(0);
      super.write(this$0x.version.getId());
      this.pos = pos;
   }

   @Override
   public void close() throws IOException {
      ByteBuffer result = ByteBuffer.wrap(this.buf, 0, this.count);
      int streamLength = this.count - 5 + 1;
      JvmProfiler.INSTANCE.onRegionFileWrite(this.this$0.info, this.pos, this.this$0.version, streamLength);
      result.putInt(0, streamLength);
      this.this$0.write(this.pos, result);
   }
}
