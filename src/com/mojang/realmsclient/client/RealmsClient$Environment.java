package com.mojang.realmsclient.client;

import java.util.Locale;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum RealmsClient$Environment {
   PRODUCTION("pc.realms.minecraft.net", "java.frontendlegacy.realms.minecraft-services.net", "java.frontend.realms.minecraft-services.net", "https"),
   STAGE(
      "pc-stage.realms.minecraft.net",
      "java.frontendlegacy.stage-c2a40e62.realms.minecraft-services.net",
      "java.frontend.stage-c2a40e62.realms.minecraft-services.net",
      "https"
   ),
   LOCAL("localhost:8080", "localhost:8080", "localhost:8080", "http");

   public final String baseUrl;
   public final String alternativeUrl;
   public final String frontendV2Url;
   public final String protocol;

   private RealmsClient$Environment(final String baseUrl, final String alternativeUrl, final String frontendV2Url, final String protocol) {
      this.baseUrl = baseUrl;
      this.alternativeUrl = alternativeUrl;
      this.frontendV2Url = frontendV2Url;
      this.protocol = protocol;
   }

   public static Optional<RealmsClient$Environment> byName(final String name) {
      String var1 = name.toLowerCase(Locale.ROOT);

      return switch (var1) {
         case "production" -> Optional.of(PRODUCTION);
         case "local" -> Optional.of(LOCAL);
         case "stage", "staging" -> Optional.of(STAGE);
         default -> Optional.empty();
      };
   }
}
