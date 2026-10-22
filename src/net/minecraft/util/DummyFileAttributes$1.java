package net.minecraft.util;

class DummyFileAttributes$1 extends DummyFileAttributes {
   @Override
   public boolean isRegularFile() {
      return false;
   }

   @Override
   public boolean isDirectory() {
      return true;
   }
}
