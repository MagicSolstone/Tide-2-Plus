package com.mojang.realmsclient.gui.screens.configuration;

import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.FocusableTextWidget;
import net.minecraft.client.gui.components.FocusableTextWidget$BackgroundFill;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
class RealmsPlayersTab$HeaderEntry extends RealmsPlayersTab$Entry {
   private String cachedNumberOfInvites;
   private final FocusableTextWidget invitedWidget;

   public RealmsPlayersTab$HeaderEntry(final RealmsPlayersTab this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.cachedNumberOfInvites = "";
      Component invitedText = Component.translatable("mco.configure.world.invited.number", "").withStyle(ChatFormatting.UNDERLINE);
      this.invitedWidget = FocusableTextWidget.builder(invitedText, this$0x.font)
         .alwaysShowBorder(false)
         .backgroundFill(FocusableTextWidget$BackgroundFill.ON_FOCUS)
         .build();
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      String numberOfInvites = this.this$0.serverData.players != null ? Integer.toString(this.this$0.serverData.players.size()) : "0";
      if (!numberOfInvites.equals(this.cachedNumberOfInvites)) {
         this.cachedNumberOfInvites = numberOfInvites;
         Component invitedComponent = Component.translatable("mco.configure.world.invited.number", numberOfInvites).withStyle(ChatFormatting.UNDERLINE);
         this.invitedWidget.setMessage(invitedComponent);
      }

      this.invitedWidget
         .setPosition(
            this.this$0.invitedList.getRowLeft() + this.this$0.invitedList.getRowWidth() / 2 - this.invitedWidget.getWidth() / 2,
            this.getY() + this.getHeight() / 2 - this.invitedWidget.getHeight() / 2
         );
      this.invitedWidget.extractRenderState(graphics, mouseX, mouseY, a);
   }

   private int height(final int lineHeight) {
      return lineHeight + this.invitedWidget.getPadding() * 2;
   }

   @Override
   public List<? extends NarratableEntry> narratables() {
      return List.of(this.invitedWidget);
   }

   @Override
   public List<? extends GuiEventListener> children() {
      return List.of(this.invitedWidget);
   }
}
