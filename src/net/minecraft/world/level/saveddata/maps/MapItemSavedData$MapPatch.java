package net.minecraft.world.level.saveddata.maps;

import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record MapItemSavedData$MapPatch(int startX, int startY, int width, int height, byte[] mapColors) {
   public static final StreamCodec<ByteBuf, Optional<MapItemSavedData$MapPatch>> STREAM_CODEC = StreamCodec.of(
      MapItemSavedData$MapPatch::write, MapItemSavedData$MapPatch::read
   );

   private static void write(final ByteBuf output, final Optional<MapItemSavedData$MapPatch> optional) {
      if (optional.isPresent()) {
         MapItemSavedData$MapPatch patch = optional.get();
         output.writeByte(patch.width);
         output.writeByte(patch.height);
         output.writeByte(patch.startX);
         output.writeByte(patch.startY);
         FriendlyByteBuf.writeByteArray(output, patch.mapColors);
      } else {
         output.writeByte(0);
      }
   }

   private static Optional<MapItemSavedData$MapPatch> read(final ByteBuf input) {
      int width = input.readUnsignedByte();
      if (width > 0) {
         int height = input.readUnsignedByte();
         int startX = input.readUnsignedByte();
         int startY = input.readUnsignedByte();
         byte[] mapColors = FriendlyByteBuf.readByteArray(input);
         return Optional.of(new MapItemSavedData$MapPatch(startX, startY, width, height, mapColors));
      } else {
         return Optional.empty();
      }
   }

   public void applyToMap(final MapItemSavedData map) {
      for (int x = 0; x < this.width; x++) {
         for (int y = 0; y < this.height; y++) {
            map.setColor(this.startX + x, this.startY + y, this.mapColors[x + y * this.width]);
         }
      }
   }
}
