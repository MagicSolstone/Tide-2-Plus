package net.minecraft.server.packs.linkfs;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import net.minecraft.util.DummyFileAttributes;
import org.jspecify.annotations.Nullable;

class LinkFSPath implements Path {
   private static final Comparator<LinkFSPath> PATH_COMPARATOR = Comparator.comparing(LinkFSPath::pathToString);
   private final String name;
   private final LinkFileSystem fileSystem;
   @Nullable
   private final LinkFSPath parent;
   @Nullable
   private List<String> pathToRoot;
   @Nullable
   private String pathString;
   private final PathContents pathContents;

   public LinkFSPath(final LinkFileSystem fileSystem, final String name, @Nullable final LinkFSPath parent, final PathContents pathContents) {
      this.fileSystem = fileSystem;
      this.name = name;
      this.parent = parent;
      this.pathContents = pathContents;
   }

   private LinkFSPath createRelativePath(@Nullable final LinkFSPath parent, final String name) {
      return new LinkFSPath(this.fileSystem, name, parent, PathContents.RELATIVE);
   }

   public LinkFileSystem getFileSystem() {
      return this.fileSystem;
   }

   @Override
   public boolean isAbsolute() {
      return this.pathContents != PathContents.RELATIVE;
   }

   @Override
   public File toFile() {
      if (this.pathContents instanceof PathContents$FileContents file) {
         return file.contents().toFile();
      } else {
         throw new UnsupportedOperationException("Path " + this.pathToString() + " does not represent file");
      }
   }

   @Nullable
   public LinkFSPath getRoot() {
      return this.isAbsolute() ? this.fileSystem.rootPath() : null;
   }

   public LinkFSPath getFileName() {
      return this.createRelativePath(null, this.name);
   }

   @Nullable
   public LinkFSPath getParent() {
      return this.parent;
   }

   @Override
   public int getNameCount() {
      return this.pathToRoot().size();
   }

   private List<String> pathToRoot() {
      if (this.name.isEmpty()) {
         return List.of();
      } else {
         if (this.pathToRoot == null) {
            Builder<String> result = ImmutableList.builder();
            if (this.parent != null) {
               result.addAll(this.parent.pathToRoot());
            }

            result.add(this.name);
            this.pathToRoot = result.build();
         }

         return this.pathToRoot;
      }
   }

   public LinkFSPath getName(final int index) {
      List<String> names = this.pathToRoot();
      if (index >= 0 && index < names.size()) {
         return this.createRelativePath(null, names.get(index));
      } else {
         throw new IllegalArgumentException("Invalid index: " + index);
      }
   }

   public LinkFSPath subpath(final int beginIndex, final int endIndex) {
      List<String> names = this.pathToRoot();
      if (beginIndex >= 0 && endIndex <= names.size() && beginIndex < endIndex) {
         LinkFSPath current = null;

         for (int i = beginIndex; i < endIndex; i++) {
            current = this.createRelativePath(current, names.get(i));
         }

         return current;
      } else {
         throw new IllegalArgumentException();
      }
   }

   @Override
   public boolean startsWith(final Path other) {
      if (other.isAbsolute() != this.isAbsolute()) {
         return false;
      } else if (other instanceof LinkFSPath otherLink) {
         if (otherLink.fileSystem != this.fileSystem) {
            return false;
         } else {
            List<String> thisNames = this.pathToRoot();
            List<String> otherNames = otherLink.pathToRoot();
            int otherSize = otherNames.size();
            if (otherSize > thisNames.size()) {
               return false;
            } else {
               for (int i = 0; i < otherSize; i++) {
                  if (!otherNames.get(i).equals(thisNames.get(i))) {
                     return false;
                  }
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   @Override
   public boolean endsWith(final Path other) {
      if (other.isAbsolute() && !this.isAbsolute()) {
         return false;
      } else if (other instanceof LinkFSPath otherLink) {
         if (otherLink.fileSystem != this.fileSystem) {
            return false;
         } else {
            List<String> thisNames = this.pathToRoot();
            List<String> otherNames = otherLink.pathToRoot();
            int otherSize = otherNames.size();
            int delta = thisNames.size() - otherSize;
            if (delta < 0) {
               return false;
            } else {
               for (int i = otherSize - 1; i >= 0; i--) {
                  if (!otherNames.get(i).equals(thisNames.get(delta + i))) {
                     return false;
                  }
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   public LinkFSPath normalize() {
      return this;
   }

   public LinkFSPath resolve(final Path other) {
      LinkFSPath otherLink = this.toLinkPath(other);
      return other.isAbsolute() ? otherLink : this.resolve(otherLink.pathToRoot());
   }

   private LinkFSPath resolve(final List<String> names) {
      LinkFSPath current = this;

      for (String name : names) {
         current = current.resolveName(name);
      }

      return current;
   }

   LinkFSPath resolveName(final String name) {
      if (isRelativeOrMissing(this.pathContents)) {
         return new LinkFSPath(this.fileSystem, name, this, this.pathContents);
      } else if (this.pathContents instanceof PathContents$DirectoryContents directory) {
         LinkFSPath child = directory.children().get(name);
         return child != null ? child : new LinkFSPath(this.fileSystem, name, this, PathContents.MISSING);
      } else if (this.pathContents instanceof PathContents$FileContents) {
         return new LinkFSPath(this.fileSystem, name, this, PathContents.MISSING);
      } else {
         throw new AssertionError("All content types should be already handled");
      }
   }

   private static boolean isRelativeOrMissing(final PathContents contents) {
      return contents == PathContents.MISSING || contents == PathContents.RELATIVE;
   }

   public LinkFSPath relativize(final Path other) {
      LinkFSPath otherLink = this.toLinkPath(other);
      if (this.isAbsolute() != otherLink.isAbsolute()) {
         throw new IllegalArgumentException("absolute mismatch");
      } else {
         List<String> thisNames = this.pathToRoot();
         List<String> otherNames = otherLink.pathToRoot();
         if (thisNames.size() >= otherNames.size()) {
            throw new IllegalArgumentException();
         } else {
            for (int i = 0; i < thisNames.size(); i++) {
               if (!thisNames.get(i).equals(otherNames.get(i))) {
                  throw new IllegalArgumentException();
               }
            }

            return otherLink.subpath(thisNames.size(), otherNames.size());
         }
      }
   }

   @Override
   public URI toUri() {
      try {
         return new URI("x-mc-link", this.fileSystem.store().name(), this.pathToString(), null);
      } catch (URISyntaxException var2) {
         throw new AssertionError("Failed to create URI", var2);
      }
   }

   public LinkFSPath toAbsolutePath() {
      return this.isAbsolute() ? this : this.fileSystem.rootPath().resolve(this);
   }

   public LinkFSPath toRealPath(final LinkOption... options) {
      return this.toAbsolutePath();
   }

   @Override
   public WatchKey register(final WatchService watcher, final Kind<?>[] events, final Modifier... modifiers) {
      throw new UnsupportedOperationException();
   }

   @Override
   public int compareTo(final Path other) {
      LinkFSPath otherPath = this.toLinkPath(other);
      return PATH_COMPARATOR.compare(this, otherPath);
   }

   @Override
   public boolean equals(final Object other) {
      if (other == this) {
         return true;
      } else if (other instanceof LinkFSPath that) {
         if (this.fileSystem != that.fileSystem) {
            return false;
         } else {
            boolean hasRealContents = this.hasRealContents();
            if (hasRealContents != that.hasRealContents()) {
               return false;
            } else {
               return hasRealContents
                  ? this.pathContents == that.pathContents
                  : Objects.equals(this.parent, that.parent) && Objects.equals(this.name, that.name);
            }
         }
      } else {
         return false;
      }
   }

   private boolean hasRealContents() {
      return !isRelativeOrMissing(this.pathContents);
   }

   @Override
   public int hashCode() {
      return this.hasRealContents() ? this.pathContents.hashCode() : this.name.hashCode();
   }

   @Override
   public String toString() {
      return this.pathToString();
   }

   private String pathToString() {
      if (this.pathString == null) {
         StringBuilder builder = new StringBuilder();
         if (this.isAbsolute()) {
            builder.append("/");
         }

         Joiner.on("/").appendTo(builder, this.pathToRoot());
         this.pathString = builder.toString();
      }

      return this.pathString;
   }

   private LinkFSPath toLinkPath(@Nullable final Path path) {
      if (path == null) {
         throw new NullPointerException();
      } else if (path instanceof LinkFSPath p && p.fileSystem == this.fileSystem) {
         return p;
      } else {
         throw new ProviderMismatchException();
      }
   }

   public boolean exists() {
      return this.hasRealContents();
   }

   @Nullable
   public Path getTargetPath() {
      return this.pathContents instanceof PathContents$FileContents file ? file.contents() : null;
   }

   @Nullable
   public PathContents$DirectoryContents getDirectoryContents() {
      return this.pathContents instanceof PathContents$DirectoryContents dir ? dir : null;
   }

   public BasicFileAttributeView getBasicAttributeView() {
      return new LinkFSPath$1(this);
   }

   public BasicFileAttributes getBasicAttributes() throws IOException {
      if (this.pathContents instanceof PathContents$DirectoryContents) {
         return DummyFileAttributes.DIRECTORY;
      } else if (this.pathContents instanceof PathContents$FileContents) {
         return DummyFileAttributes.FILE;
      } else {
         throw new NoSuchFileException(this.pathToString());
      }
   }
}
