package com.mojang.realmsclient;

import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsServer$State;
import com.mojang.realmsclient.gui.screens.RealmsCreateRealmScreen;
import com.mojang.realmsclient.util.RealmsUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetTooltipHolder;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientActivePlayersTooltip$ActivePlayersTooltip;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.PlayerSkinRenderCache$RenderInfo;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.component.ResolvableProfile;

@Environment(EnvType.CLIENT)
class RealmsMainScreen$ServerEntry extends RealmsMainScreen$Entry {
   private static final Component ONLINE_PLAYERS_TOOLTIP_HEADER = Component.translatable("mco.onlinePlayers");
   private static final int PLAYERS_ONLINE_SPRITE_SIZE = 9;
   private static final int PLAYERS_ONLINE_SPRITE_SEPARATION = 3;
   private static final int SKIN_HEAD_LARGE_WIDTH = 36;
   private final RealmsServer serverData;
   private final WidgetTooltipHolder tooltip;

   public RealmsMainScreen$ServerEntry(final RealmsMainScreen this$0, final RealmsServer serverData) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
      this.tooltip = new WidgetTooltipHolder();
      this.serverData = serverData;
      boolean selfOwnedServer = RealmsMainScreen.isSelfOwnedServer(serverData);
      if (RealmsMainScreen.isSnapshot() && selfOwnedServer && serverData.isSnapshotRealm()) {
         this.tooltip.set(Tooltip.create(Component.translatable("mco.snapshot.paired", serverData.parentWorldName)));
      } else if (!selfOwnedServer && serverData.needsDowngrade()) {
         this.tooltip.set(Tooltip.create(Component.translatable("mco.snapshot.friendsRealm.downgrade", serverData.activeVersion)));
      }
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      if (this.serverData.state == RealmsServer$State.UNINITIALIZED) {
         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RealmsMainScreen.NEW_REALM_SPRITE, this.getContentX() - 5, this.getContentYMiddle() - 10, 40, 20);
         int textYPos = this.getContentYMiddle() - 9 / 2;
         graphics.text(RealmsMainScreen.access$2600(this.this$0), RealmsMainScreen.SERVER_UNITIALIZED_TEXT, this.getContentX() + 40 - 2, textYPos, -8388737);
      } else {
         RealmsUtil.extractPlayerFace(graphics, this.getContentX(), this.getContentY(), 32, this.serverData.ownerUUID);
         this.extractFirstLine(graphics, this.getContentY(), this.getContentX(), this.getContentWidth(), -1, this.serverData);
         this.extractSecondLine(graphics, this.getContentY(), this.getContentX(), this.getContentWidth(), this.serverData);
         this.extractThirdLine(graphics, this.getContentY(), this.getContentX(), this.serverData);
         this.extractStatusLights(this.serverData, graphics, this.getContentRight(), this.getContentY(), mouseX, mouseY);
         boolean hasTooltip = this.extractOnlinePlayers(
            graphics, this.getContentY(), this.getContentX(), this.getContentWidth(), this.getContentHeight(), mouseX, mouseY, a
         );
         if (!hasTooltip) {
            this.tooltip
               .refreshTooltipForNextRenderPass(
                  graphics,
                  mouseX,
                  mouseY,
                  hovered,
                  this.isFocused(),
                  new ScreenRectangle(this.getContentX(), this.getContentY(), this.getContentWidth(), this.getContentHeight())
               );
         }
      }
   }

   private boolean extractOnlinePlayers(
      final GuiGraphicsExtractor graphics,
      final int rowTop,
      final int rowLeft,
      final int rowWidth,
      final int rowHeight,
      final int mouseX,
      final int mouseY,
      final float a
   ) {
      List<ResolvableProfile> profileResults = this.this$0.onlinePlayersPerRealm.getProfileResultsFor(this.serverData.id);
      int playerCount = profileResults.size();
      if (playerCount > 0) {
         int playersOnlineXEnd = rowLeft + rowWidth - 21;
         int playersOnlineY = rowTop + rowHeight - 9 - 2;
         int playerOnlineWidth = 9 * playerCount + 3 * (playerCount - 1);
         int playersOnlineXStart = playersOnlineXEnd - playerOnlineWidth;
         List<PlayerSkinRenderCache$RenderInfo> tooltipEntries;
         if (mouseX >= playersOnlineXStart && mouseX <= playersOnlineXEnd && mouseY >= playersOnlineY && mouseY <= playersOnlineY + 9) {
            tooltipEntries = new ArrayList<>(playerCount);
         } else {
            tooltipEntries = null;
         }

         PlayerSkinRenderCache skinCache = RealmsMainScreen.access$2700(this.this$0).playerSkinRenderCache();

         for (int i = 0; i < profileResults.size(); i++) {
            ResolvableProfile profile = profileResults.get(i);
            PlayerSkinRenderCache$RenderInfo profileRenderInfo = skinCache.getOrDefault(profile);
            int xPos = playersOnlineXStart + 12 * i;
            PlayerFaceExtractor.extractRenderState(graphics, profileRenderInfo.playerSkin(), xPos, playersOnlineY, 9);
            if (tooltipEntries != null) {
               tooltipEntries.add(profileRenderInfo);
            }
         }

         if (tooltipEntries != null) {
            graphics.setTooltipForNextFrame(
               RealmsMainScreen.access$2800(this.this$0),
               List.of(ONLINE_PLAYERS_TOOLTIP_HEADER),
               Optional.of(new ClientActivePlayersTooltip$ActivePlayersTooltip(tooltipEntries)),
               mouseX,
               mouseY
            );
            return true;
         }
      }

      return false;
   }

   private void playRealm() {
      RealmsMainScreen.access$2900(this.this$0).getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
      RealmsMainScreen.play(this.serverData, this.this$0);
   }

   private void createUnitializedRealm() {
      RealmsMainScreen.access$3000(this.this$0).getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
      RealmsCreateRealmScreen createScreen = new RealmsCreateRealmScreen(this.this$0, this.serverData, this.serverData.isSnapshotRealm());
      RealmsMainScreen.access$3100(this.this$0).gui.setScreen(createScreen);
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      if (this.serverData.state == RealmsServer$State.UNINITIALIZED) {
         this.createUnitializedRealm();
      } else if (this.serverData.shouldPlayButtonBeActive() && doubleClick && this.isFocused()) {
         this.playRealm();
      }

      return true;
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      if (event.isSelection()) {
         if (this.serverData.state == RealmsServer$State.UNINITIALIZED) {
            this.createUnitializedRealm();
            return true;
         }

         if (this.serverData.shouldPlayButtonBeActive()) {
            this.playRealm();
            return true;
         }
      }

      return super.keyPressed(event);
   }

   @Override
   public Component getNarration() {
      return (Component)(this.serverData.state == RealmsServer$State.UNINITIALIZED
         ? RealmsMainScreen.UNITIALIZED_WORLD_NARRATION
         : Component.translatable("narrator.select", Objects.requireNonNullElse(this.serverData.name, "unknown server")));
   }

   public RealmsServer getServer() {
      return this.serverData;
   }
}
