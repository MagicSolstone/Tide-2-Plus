package net.minecraft.client.multiplayer.chat;

import java.util.function.BiConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.ChatOptionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonLinks;

@Environment(EnvType.CLIENT)
public record ChatRestriction$Action(Component title, BiConsumer<Minecraft, Screen> runnable) {
   public static final ChatRestriction$Action GO_TO_ACCOUNT = new ChatRestriction$Action(
      Component.translatable("chat_restriction.disabled_by_profile.action"),
      (minecraft, screen) -> ConfirmLinkScreen.confirmLinkNow(screen, CommonLinks.ACCOUNT_SETTINGS, true)
   );
   public static final ChatRestriction$Action GO_TO_CHAT_SETTINGS = new ChatRestriction$Action(
      Component.translatable("chat_restriction.chat_disabled_by_options.action"),
      (minecraft, screen) -> minecraft.gui.setScreen(new ChatOptionsScreen(screen, minecraft.options))
   );
}
