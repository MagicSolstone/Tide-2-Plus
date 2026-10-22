package net.minecraft.util.filefix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.nio.file.Path;
import java.util.List;
import net.minecraft.util.filefix.virtualfilesystem.FileMove;

public record FileFixerUpper$UpgradeInProgress(List<FileMove> moves) {
   public static Codec<FileFixerUpper$UpgradeInProgress> codec(final Path fromDirectory, final Path toDirectory) {
      return RecordCodecBuilder.create(
         i -> i.group(FileMove.moveCodec(fromDirectory, toDirectory).listOf().fieldOf("moves").forGetter(FileFixerUpper$UpgradeInProgress::moves))
            .apply(i, FileFixerUpper$UpgradeInProgress::new)
      );
   }
}
