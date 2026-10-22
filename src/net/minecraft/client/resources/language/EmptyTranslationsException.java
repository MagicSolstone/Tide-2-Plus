package net.minecraft.client.resources.language;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class EmptyTranslationsException extends RuntimeException {
   private final String languageCode;

   EmptyTranslationsException(final String languageCode) {
      this.languageCode = languageCode;
   }

   public String getLanguageCode() {
      return this.languageCode;
   }
}
