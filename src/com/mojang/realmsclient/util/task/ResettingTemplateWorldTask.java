package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.exception.RealmsServiceException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class ResettingTemplateWorldTask extends ResettingWorldTask {
   private final WorldTemplate template;

   public ResettingTemplateWorldTask(final WorldTemplate template, final long serverId, final Component title, final Runnable callback) {
      super(serverId, title, callback);
      this.template = template;
   }

   @Override
   protected void sendResetRequest(final RealmsClient client, final long serverId) throws RealmsServiceException {
      client.resetWorldWithTemplate(serverId, this.template.id());
   }
}
