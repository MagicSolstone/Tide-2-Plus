package net.minecraft.client.multiplayer.chat.report;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public record Report$CannotBuildReason(Component message) {
   public static final Report$CannotBuildReason NO_REASON = new Report$CannotBuildReason(Component.translatable("gui.abuseReport.send.no_reason"));
   public static final Report$CannotBuildReason NO_REPORTED_MESSAGES = new Report$CannotBuildReason(
      Component.translatable("gui.chatReport.send.no_reported_messages")
   );
   public static final Report$CannotBuildReason TOO_MANY_MESSAGES = new Report$CannotBuildReason(
      Component.translatable("gui.chatReport.send.too_many_messages")
   );
   public static final Report$CannotBuildReason COMMENT_TOO_LONG = new Report$CannotBuildReason(Component.translatable("gui.abuseReport.send.comment_too_long"));
   public static final Report$CannotBuildReason NOT_ATTESTED = new Report$CannotBuildReason(Component.translatable("gui.abuseReport.send.not_attested"));

   public Tooltip tooltip() {
      return Tooltip.create(this.message);
   }
}
