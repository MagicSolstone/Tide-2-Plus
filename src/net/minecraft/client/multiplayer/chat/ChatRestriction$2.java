package net.minecraft.client.multiplayer.chat;

import java.util.Optional;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permission;

@Environment(EnvType.CLIENT)
enum ChatRestriction$2 {
   private ChatRestriction$2(final Component display, final Optional action) {
   }

   @Override
   public void modifyPermissions(final Set<Permission> permissionSet) {
      ChatRestriction.disablePlayerMessages(permissionSet);
   }
}
