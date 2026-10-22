package net.minecraft.util.filefix.operations;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import net.minecraft.util.filefix.access.FileAccessProvider;
import net.minecraft.util.worldupdate.UpgradeProgress;

public class ModifyContent implements FileFixOperation {
   private final FileAccessProvider fileAccessProvider;
   private final ModifyContent$FixFunction fixFunction;

   public ModifyContent(final FileAccessProvider fileAccessProvider, final ModifyContent$FixFunction fixFunction) {
      this.fileAccessProvider = fileAccessProvider;
      this.fixFunction = fixFunction;
   }

   @Override
   public void fix(final Path baseDirectory, final UpgradeProgress upgradeProgress) throws IOException {
      try {
         ScopedValue.where(this.fileAccessProvider.baseDirectory(), baseDirectory).run(() -> {
            try {
               this.fixFunction.run(upgradeProgress);
            } catch (IOException var3) {
               throw new UncheckedIOException(var3);
            }
         });
      } catch (UncheckedIOException var7) {
         throw var7.getCause();
      } finally {
         this.fileAccessProvider.close();
      }
   }
}
