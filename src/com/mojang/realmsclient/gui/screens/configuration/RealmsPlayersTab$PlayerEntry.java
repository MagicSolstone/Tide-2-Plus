package com.mojang.realmsclient.gui.screens.configuration;

import com.google.common.collect.ImmutableList;
import com.mojang.realmsclient.dto.Ops;
import com.mojang.realmsclient.dto.PlayerInfo;
import com.mojang.realmsclient.gui.screens.RealmsConfirmScreen;
import com.mojang.realmsclient.util.RealmsUtil;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
class RealmsPlayersTab$PlayerEntry extends RealmsPlayersTab$Entry {
   protected static final int SKIN_FACE_SIZE = 32;
   private static final Component NORMAL_USER_TEXT = Component.translatable("mco.configure.world.invites.normal.tooltip");
   private static final Component OP_TEXT = Component.translatable("mco.configure.world.invites.ops.tooltip");
   private static final Component REMOVE_TEXT = Component.translatable("mco.configure.world.invites.remove.tooltip");
   private static final Identifier MAKE_OP_SPRITE = Identifier.withDefaultNamespace("player_list/make_operator");
   private static final Identifier REMOVE_OP_SPRITE = Identifier.withDefaultNamespace("player_list/remove_operator");
   private static final Identifier REMOVE_PLAYER_SPRITE = Identifier.withDefaultNamespace("player_list/remove_player");
   private static final int ICON_WIDTH = 8;
   private static final int ICON_HEIGHT = 7;
   private final PlayerInfo playerInfo;
   private final Button removeButton;
   private final Button makeOpButton;
   private final Button removeOpButton;

   public RealmsPlayersTab$PlayerEntry(final RealmsPlayersTab this$0, final PlayerInfo playerInfo) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.playerInfo = playerInfo;
      int index = this$0x.serverData.players.indexOf(this.playerInfo);
      this.makeOpButton = SpriteIconButton.builder(NORMAL_USER_TEXT, button -> this.op(index), false)
         .sprite(MAKE_OP_SPRITE, 8, 7)
         .width(16 + this$0x.configurationScreen.getFont().width(NORMAL_USER_TEXT))
         .narration(
            defaultNarrationSupplier -> CommonComponents.joinForNarration(
               Component.translatable("mco.invited.player.narration", playerInfo.name),
               defaultNarrationSupplier.get(),
               Component.translatable("narration.cycle_button.usage.focused", OP_TEXT)
            )
         )
         .build();
      this.removeOpButton = SpriteIconButton.builder(OP_TEXT, button -> this.deop(index), false)
         .sprite(REMOVE_OP_SPRITE, 8, 7)
         .width(16 + this$0x.configurationScreen.getFont().width(OP_TEXT))
         .narration(
            defaultNarrationSupplier -> CommonComponents.joinForNarration(
               Component.translatable("mco.invited.player.narration", playerInfo.name),
               defaultNarrationSupplier.get(),
               Component.translatable("narration.cycle_button.usage.focused", NORMAL_USER_TEXT)
            )
         )
         .build();
      this.removeButton = SpriteIconButton.builder(REMOVE_TEXT, button -> this.uninvite(index), false)
         .sprite(REMOVE_PLAYER_SPRITE, 8, 7)
         .width(16 + this$0x.configurationScreen.getFont().width(REMOVE_TEXT))
         .narration(
            defaultNarrationSupplier -> CommonComponents.joinForNarration(
               Component.translatable("mco.invited.player.narration", playerInfo.name), defaultNarrationSupplier.get()
            )
         )
         .build();
      this.updateOpButtons();
   }

   private void op(final int index) {
      UUID selectedInvite = this.this$0.serverData.players.get(index).uuid;
      RealmsUtil.<Ops>supplyAsync(client -> client.op(this.this$0.serverData.id, selectedInvite), e -> RealmsPlayersTab.LOGGER.error("Couldn't op the user", e))
         .thenAcceptAsync(ops -> {
            this.updateOps(ops);
            this.updateOpButtons();
            this.setFocused(this.removeOpButton);
         }, this.this$0.minecraft);
   }

   private void deop(final int index) {
      UUID selectedInvite = this.this$0.serverData.players.get(index).uuid;
      RealmsUtil.<Ops>supplyAsync(
            client -> client.deop(this.this$0.serverData.id, selectedInvite), e -> RealmsPlayersTab.LOGGER.error("Couldn't deop the user", e)
         )
         .thenAcceptAsync(ops -> {
            this.updateOps(ops);
            this.updateOpButtons();
            this.setFocused(this.makeOpButton);
         }, this.this$0.minecraft);
   }

   private void uninvite(final int index) {
      if (index >= 0 && index < this.this$0.serverData.players.size()) {
         PlayerInfo playerInfo = this.this$0.serverData.players.get(index);
         RealmsConfirmScreen confirmScreen = new RealmsConfirmScreen(
            result -> {
               if (result) {
                  RealmsUtil.runAsync(
                     client -> client.uninvite(this.this$0.serverData.id, playerInfo.uuid), e -> RealmsPlayersTab.LOGGER.error("Couldn't uninvite user", e)
                  );
                  this.this$0.serverData.players.remove(index);
                  this.this$0.updateData(this.this$0.serverData);
               }

               this.this$0.minecraft.gui.setScreen(this.this$0.configurationScreen);
            },
            RealmsPlayersTab.QUESTION_TITLE,
            Component.translatable("mco.configure.world.uninvite.player", playerInfo.name)
         );
         this.this$0.minecraft.gui.setScreen(confirmScreen);
      }
   }

   private void updateOps(final Ops ops) {
      for (PlayerInfo playerInfo : this.this$0.serverData.players) {
         playerInfo.operator = ops.ops().contains(playerInfo.name);
      }
   }

   private void updateOpButtons() {
      this.makeOpButton.visible = !this.playerInfo.operator;
      this.removeOpButton.visible = !this.makeOpButton.visible;
   }

   private Button activeOpButton() {
      return this.makeOpButton.visible ? this.makeOpButton : this.removeOpButton;
   }

   @Override
   public List<? extends GuiEventListener> children() {
      return ImmutableList.of(this.activeOpButton(), this.removeButton);
   }

   @Override
   public List<? extends NarratableEntry> narratables() {
      return ImmutableList.of(this.activeOpButton(), this.removeButton);
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      int inviteColor;
      if (!this.playerInfo.accepted) {
         inviteColor = -6250336;
      } else if (this.playerInfo.online) {
         inviteColor = -16711936;
      } else {
         inviteColor = -1;
      }

      int skinYPos = this.getContentYMiddle() - 16;
      RealmsUtil.extractPlayerFace(graphics, this.getContentX(), skinYPos, 32, this.playerInfo.uuid);
      int textYPos = this.getContentYMiddle() - 9 / 2;
      graphics.text(this.this$0.font, this.playerInfo.name, this.getContentX() + 8 + 32, textYPos, inviteColor);
      int iconYPos = this.getContentYMiddle() - 10;
      int removeButtonXPos = this.getContentRight() - this.removeButton.getWidth();
      this.removeButton.setPosition(removeButtonXPos, iconYPos);
      this.removeButton.extractRenderState(graphics, mouseX, mouseY, a);
      int opButtonXPos = removeButtonXPos - this.activeOpButton().getWidth() - 8;
      this.makeOpButton.setPosition(opButtonXPos, iconYPos);
      this.makeOpButton.extractRenderState(graphics, mouseX, mouseY, a);
      this.removeOpButton.setPosition(opButtonXPos, iconYPos);
      this.removeOpButton.extractRenderState(graphics, mouseX, mouseY, a);
   }
}
