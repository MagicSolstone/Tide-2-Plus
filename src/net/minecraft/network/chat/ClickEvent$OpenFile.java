package net.minecraft.network.chat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.io.File;
import java.nio.file.Path;

public record ClickEvent$OpenFile(String path) implements ClickEvent {
   public static final MapCodec<ClickEvent$OpenFile> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(Codec.STRING.fieldOf("path").forGetter(ClickEvent$OpenFile::path)).apply(i, ClickEvent$OpenFile::new)
   );

   public ClickEvent$OpenFile(final File file) {
      this(file.toString());
   }

   public ClickEvent$OpenFile(final Path path) {
      this(path.toFile());
   }

   public File file() {
      return new File(this.path);
   }

   @Override
   public ClickEvent$Action action() {
      return ClickEvent$Action.OPEN_FILE;
   }
}
