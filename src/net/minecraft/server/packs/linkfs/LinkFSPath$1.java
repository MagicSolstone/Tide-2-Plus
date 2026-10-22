package net.minecraft.server.packs.linkfs;

import java.io.IOException;
import java.nio.file.ReadOnlyFileSystemException;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Objects;

class LinkFSPath$1 implements BasicFileAttributeView {
   LinkFSPath$1(final LinkFSPath this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public String name() {
      return "basic";
   }

   @Override
   public BasicFileAttributes readAttributes() throws IOException {
      return this.this$0.getBasicAttributes();
   }

   @Override
   public void setTimes(final FileTime lastModifiedTime, final FileTime lastAccessTime, final FileTime createTime) {
      throw new ReadOnlyFileSystemException();
   }
}
