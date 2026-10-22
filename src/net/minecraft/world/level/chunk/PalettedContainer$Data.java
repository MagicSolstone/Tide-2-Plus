package net.minecraft.world.level.chunk;

import net.minecraft.core.IdMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.BitStorage;

record PalettedContainer$Data<T>(Configuration configuration, BitStorage storage, Palette<T> palette) {
   public void copyFrom(final Palette<T> oldPalette, final BitStorage oldStorage) {
      PaletteResize<T> dummyResizer = PaletteResize.noResizeExpected();
      if (oldPalette.getSize() == 1) {
         T value = oldPalette.valueFor(0);
         this.storage.fill(this.palette.idFor(value, dummyResizer));
      } else {
         int lastOldId = -1;
         int newId = -1;

         for (int i = 0; i < oldStorage.getSize(); i++) {
            int oldId = oldStorage.get(i);
            if (oldId != lastOldId) {
               T value = oldPalette.valueFor(oldId);
               newId = this.palette.idFor(value, dummyResizer);
               lastOldId = oldId;
            }

            this.storage.set(i, newId);
         }
      }
   }

   public int getSerializedSize(final IdMap<T> globalMap) {
      return 1 + this.palette.getSerializedSize(globalMap) + this.storage.getRaw().length * 8;
   }

   public void write(final FriendlyByteBuf buffer, final IdMap<T> globalMap) {
      buffer.writeByte(this.storage.getBits());
      this.palette.write(buffer, globalMap);
      buffer.writeFixedSizeLongArray(this.storage.getRaw());
   }

   public PalettedContainer$Data<T> copy() {
      return new PalettedContainer$Data<>(this.configuration, this.storage.copy(), this.palette.copy());
   }
}
