package net.minecraft.util.filefix.virtualfilesystem;

import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import net.minecraft.util.DummyFileAttributes;

class CopyOnWriteFSProvider$1 implements BasicFileAttributeView {
   @Override
   public String name() {
      return "basic";
   }

   @Override
   public BasicFileAttributes readAttributes() {
      return DummyFileAttributes.DIRECTORY;
   }

   @Override
   public void setTimes(final FileTime lastModifiedTime, final FileTime lastAccessTime, final FileTime createTime) throws IOException {
   }
}
