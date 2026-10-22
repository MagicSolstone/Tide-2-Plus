package net.minecraft.client.multiplayer.chat;

import java.util.Optional;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.Permissions;

@Environment(EnvType.CLIENT)
public enum ChatRestriction {
   CHAT_AND_COMMANDS_DISABLED_BY_OPTIONS(
      Component.translatable("chat_restriction.chat_and_commands_disabled_by_options"), Optional.of(ChatRestriction$Action.GO_TO_CHAT_SETTINGS)
   ),
   CHAT_DISABLED_BY_OPTIONS(Component.translatable("chat_restriction.chat_disabled_by_options"), Optional.of(ChatRestriction$Action.GO_TO_CHAT_SETTINGS)),
   DISABLED_BY_LAUNCHER(Component.translatable("chat_restriction.disabled_by_launcher"), Optional.empty()),
   DISABLED_BY_PROFILE(Component.translatable("chat_restriction.disabled_by_profile"), Optional.of(ChatRestriction$Action.GO_TO_ACCOUNT));

   private final Component display;
   private final Optional<ChatRestriction$Action> action;

   private ChatRestriction(final Component display, final Optional<ChatRestriction$Action> action) {
      this.display = display;
      this.action = action;
   }

   public Component display() {
      return this.display;
   }

   public Optional<ChatRestriction$Action> action() {
      return this.action;
   }

   private static void disablePlayerMessages(final Set<Permission> permissionSet) {
      permissionSet.remove(Permissions.CHAT_SEND_MESSAGES);
      permissionSet.remove(Permissions.CHAT_RECEIVE_PLAYER_MESSAGES);
   }

   private static void disableCommands(final Set<Permission> permissionSet) {
      permissionSet.remove(Permissions.CHAT_SEND_COMMANDS);
      permissionSet.remove(Permissions.CHAT_RECEIVE_SYSTEM_MESSAGES);
   }

   public abstract void modifyPermissions(Set<Permission> permissionSet);
}
