package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer$WorldType;
import com.mojang.realmsclient.dto.WorldTemplatePaginatedList;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class RealmsResetWorldScreen$1 extends Thread {
   RealmsResetWorldScreen$1(final RealmsResetWorldScreen this$0, String name) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(name);
   }

   @Override
   public void run() {
      RealmsClient client = RealmsClient.getOrCreate();

      try {
         WorldTemplatePaginatedList templates = client.fetchWorldTemplates(1, 10, RealmsServer$WorldType.NORMAL);
         WorldTemplatePaginatedList adventuremaps = client.fetchWorldTemplates(1, 10, RealmsServer$WorldType.ADVENTUREMAP);
         WorldTemplatePaginatedList experiences = client.fetchWorldTemplates(1, 10, RealmsServer$WorldType.EXPERIENCE);
         WorldTemplatePaginatedList inspirations = client.fetchWorldTemplates(1, 10, RealmsServer$WorldType.INSPIRATION);
         RealmsResetWorldScreen.access$000(this.this$0).execute(() -> {
            this.this$0.templates = templates;
            this.this$0.adventuremaps = adventuremaps;
            this.this$0.experiences = experiences;
            this.this$0.inspirations = inspirations;
         });
      } catch (RealmsServiceException var6) {
         RealmsResetWorldScreen.LOGGER.error("Couldn't fetch templates in reset world", var6);
      }
   }
}
