package net.minecraft.util.filefix;

import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.DataFixerBuilder.Result;
import com.mojang.datafixers.schemas.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;

public class FileFixerUpper$Builder {
   public final List<FileFix> fileFixes = new ArrayList<>();
   private final int currentVersion;
   private int latestFileFixerVersion;
   private final List<Schema> knownSchemas = new ArrayList<>();

   public FileFixerUpper$Builder(final int currentVersion) {
      this.currentVersion = currentVersion;
   }

   public void addFixer(final FileFix fileFix) {
      if (!this.knownSchemas.contains(fileFix.getSchema())) {
         throw new IllegalArgumentException("Tried to add file fixer with unknown schema. Add it through FileFixerUpper#addSchema instead");
      } else {
         int fileFixVersion = fileFix.getVersion();
         if (fileFix.getVersion() > this.currentVersion) {
            throw new IllegalArgumentException(
               String.format(
                  Locale.ROOT, "Tried to add too recent file fix for version: %s. The data version of the game is: %s", fileFixVersion, this.currentVersion
               )
            );
         } else {
            if (!this.fileFixes.isEmpty()) {
               FileFix last = this.fileFixes.getLast();
               if (last.getVersion() > fileFixVersion) {
                  throw new IllegalArgumentException(
                     String.format(
                        Locale.ROOT,
                        "Tried to add too recent file fix for version: %s. The most recent file fix version is %s",
                        fileFixVersion,
                        last.getVersion()
                     )
                  );
               }
            }

            this.fileFixes.add(fileFix);
         }
      }
   }

   public Schema addSchema(final DataFixerBuilder fixerUpper, final int version, final BiFunction<Integer, Schema, Schema> factory) {
      this.latestFileFixerVersion = Math.max(version, this.latestFileFixerVersion);
      Schema schema = fixerUpper.addSchema(version, factory);
      this.knownSchemas.add(schema);
      return schema;
   }

   public FileFixerUpper build(final Result dataFixer) {
      return new FileFixerUpper(dataFixer, this.fileFixes, this.latestFileFixerVersion);
   }
}
