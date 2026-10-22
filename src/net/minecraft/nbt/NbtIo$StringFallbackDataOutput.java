package net.minecraft.nbt;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UTFDataFormatException;
import net.minecraft.util.DelegateDataOutput;
import net.minecraft.util.Util;

public class NbtIo$StringFallbackDataOutput extends DelegateDataOutput {
   public NbtIo$StringFallbackDataOutput(final DataOutput parent) {
      super(parent);
   }

   @Override
   public void writeUTF(final String s) throws IOException {
      try {
         super.writeUTF(s);
      } catch (UTFDataFormatException var3) {
         Util.logAndPauseIfInIde("Failed to write NBT String", var3);
         super.writeUTF("");
      }
   }
}
