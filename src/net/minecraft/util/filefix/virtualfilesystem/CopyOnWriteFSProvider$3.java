package net.minecraft.util.filefix.virtualfilesystem;

import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Objects;
import net.minecraft.util.filefix.virtualfilesystem.exception.CowFSNoSuchFileException;

class CopyOnWriteFSProvider$3 implements BasicFileAttributeView {
   CopyOnWriteFSProvider$3(final CopyOnWriteFSProvider this$0, final CopyOnWriteFSPath val$cowPath) {
      this.val$cowPath = val$cowPathx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public String name() {
      return "basic";
   }

   @Override
   public BasicFileAttributes readAttributes() throws IOException {
      throw new CowFSNoSuchFileException(this.val$cowPath.toString());
   }

   @Override
   public void setTimes(final FileTime lastModifiedTime, final FileTime lastAccessTime, final FileTime createTime) throws IOException {
      throw new CowFSNoSuchFileException(this.val$cowPath.toString());
   }
}
