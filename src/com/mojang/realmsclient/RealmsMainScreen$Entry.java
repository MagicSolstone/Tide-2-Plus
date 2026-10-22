package com.mojang.realmsclient;

import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsServer$State;
import java.util.Objects;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList$Entry;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.GameType;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
abstract class RealmsMainScreen$Entry extends ObjectSelectionList$Entry<RealmsMainScreen$Entry> {
   protected static final int STATUS_LIGHT_WIDTH = 10;
   private static final int STATUS_LIGHT_HEIGHT = 28;
   protected static final int PADDING_X = 7;
   protected static final int PADDING_Y = 2;

   private RealmsMainScreen$Entry(final RealmsMainScreen this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
   }

   protected void extractStatusLights(
      final RealmsServer serverData, final GuiGraphicsExtractor graphics, final int rowRight, final int rowTop, final int mouseX, final int mouseY
   ) {
      int x = rowRight - 10 - 7;
      int y = rowTop + 2;
      if (serverData.expired) {
         this.extractRealmStatus(graphics, x, y, mouseX, mouseY, RealmsMainScreen.EXPIRED_SPRITE, () -> RealmsMainScreen.SERVER_EXPIRED_TOOLTIP);
      } else if (serverData.state == RealmsServer$State.CLOSED) {
         this.extractRealmStatus(graphics, x, y, mouseX, mouseY, RealmsMainScreen.CLOSED_SPRITE, () -> RealmsMainScreen.SERVER_CLOSED_TOOLTIP);
      } else if (RealmsMainScreen.isSelfOwnedServer(serverData) && serverData.daysLeft < 7) {
         this.extractRealmStatus(
            graphics,
            x,
            y,
            mouseX,
            mouseY,
            RealmsMainScreen.EXPIRES_SOON_SPRITE,
            () -> {
               if (serverData.daysLeft <= 0) {
                  return RealmsMainScreen.SERVER_EXPIRES_SOON_TOOLTIP;
               } else {
                  return (Component)(serverData.daysLeft == 1
                     ? RealmsMainScreen.SERVER_EXPIRES_IN_DAY_TOOLTIP
                     : Component.translatable("mco.selectServer.expires.days", serverData.daysLeft));
               }
            }
         );
      } else if (serverData.state == RealmsServer$State.OPEN) {
         this.extractRealmStatus(graphics, x, y, mouseX, mouseY, RealmsMainScreen.OPEN_SPRITE, () -> RealmsMainScreen.SERVER_OPEN_TOOLTIP);
      }
   }

   private void extractRealmStatus(
      final GuiGraphicsExtractor graphics, final int x, final int y, final int xm, final int ym, final Identifier sprite, final Supplier<Component> tooltip
   ) {
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, 10, 28);
      if (this.this$0.realmSelectionList.isMouseOver(xm, ym) && xm >= x && xm <= x + 10 && ym >= y && ym <= y + 28) {
         graphics.setTooltipForNextFrame(tooltip.get(), xm, ym);
      }
   }

   protected void extractFirstLine(
      final GuiGraphicsExtractor graphics, final int rowTop, final int rowLeft, final int rowWidth, final int serverNameColor, final RealmsServer serverData
   ) {
      int textX = this.textX(rowLeft);
      int firstLineY = this.firstLineY(rowTop);
      Component versionComponent = RealmsMainScreen.getVersionComponent(serverData.activeVersion, serverData.isCompatible());
      int versionTextX = this.versionTextX(rowLeft, rowWidth, versionComponent);
      this.extractClampedString(graphics, serverData.getName(), textX, firstLineY, versionTextX, serverNameColor);
      if (versionComponent != CommonComponents.EMPTY && !serverData.isMinigameActive()) {
         graphics.text(RealmsMainScreen.access$400(this.this$0), versionComponent, versionTextX, firstLineY, -8355712);
      }
   }

   protected void extractSecondLine(final GuiGraphicsExtractor graphics, final int rowTop, final int rowLeft, final int rowWidth, final RealmsServer serverData) {
      int textX = this.textX(rowLeft);
      int firstLineY = this.firstLineY(rowTop);
      int secondLineY = this.secondLineY(firstLineY);
      String minigameName = serverData.getMinigameName();
      boolean minigameActive = serverData.isMinigameActive();
      if (minigameActive && minigameName != null) {
         Component minigameNameComponent = Component.literal(minigameName).withStyle(ChatFormatting.GRAY);
         graphics.text(
            RealmsMainScreen.access$500(this.this$0),
            Component.translatable("mco.selectServer.minigameName", minigameNameComponent).withColor(-171),
            textX,
            secondLineY,
            -1
         );
      } else {
         int maxX = this.extractGameMode(serverData, graphics, rowLeft, rowWidth, firstLineY);
         this.extractClampedString(graphics, serverData.getDescription(), textX, this.secondLineY(firstLineY), maxX, -8355712);
      }
   }

   protected void extractThirdLine(final GuiGraphicsExtractor graphics, final int rowTop, final int rowLeft, final RealmsServer server) {
      int textX = this.textX(rowLeft);
      int firstLineY = this.firstLineY(rowTop);
      int thirdLineY = this.thirdLineY(firstLineY);
      if (!RealmsMainScreen.isSelfOwnedServer(server)) {
         graphics.text(RealmsMainScreen.access$600(this.this$0), server.owner, textX, this.thirdLineY(firstLineY), -8355712);
      } else if (server.expired) {
         Component expirationText = server.expiredTrial ? RealmsMainScreen.TRIAL_EXPIRED_TEXT : RealmsMainScreen.SUBSCRIPTION_EXPIRED_TEXT;
         graphics.text(RealmsMainScreen.access$700(this.this$0), expirationText, textX, thirdLineY, -2142128);
      }
   }

   protected void extractClampedString(
      final GuiGraphicsExtractor graphics, @Nullable final String string, final int x, final int y, final int maxX, final int color
   ) {
      if (string != null) {
         int availableSpace = maxX - x;
         if (RealmsMainScreen.access$800(this.this$0).width(string) > availableSpace) {
            String clampedName = RealmsMainScreen.access$1000(this.this$0)
               .plainSubstrByWidth(string, availableSpace - RealmsMainScreen.access$900(this.this$0).width("... "));
            graphics.text(RealmsMainScreen.access$1100(this.this$0), clampedName + "...", x, y, color);
         } else {
            graphics.text(RealmsMainScreen.access$1200(this.this$0), string, x, y, color);
         }
      }
   }

   protected int versionTextX(final int rowLeft, final int rowWidth, final Component versionComponent) {
      return rowLeft + rowWidth - RealmsMainScreen.access$1300(this.this$0).width(versionComponent) - 20;
   }

   protected int gameModeTextX(final int rowLeft, final int rowWidth, final Component versionComponent) {
      return rowLeft + rowWidth - RealmsMainScreen.access$1400(this.this$0).width(versionComponent) - 20;
   }

   protected int extractGameMode(final RealmsServer server, final GuiGraphicsExtractor graphics, final int rowLeft, final int rowWidth, final int firstLineY) {
      boolean hardcore = server.isHardcore;
      int gameMode = server.gameMode;
      int x = rowLeft;
      if (GameType.isValidId(gameMode)) {
         Component gameModeComponent = RealmsMainScreen.getGameModeComponent(gameMode, hardcore);
         x = this.gameModeTextX(rowLeft, rowWidth, gameModeComponent);
         graphics.text(RealmsMainScreen.access$1500(this.this$0), gameModeComponent, x, this.secondLineY(firstLineY), -8355712);
      }

      if (hardcore) {
         x -= 10;
         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RealmsMainScreen.HARDCORE_MODE_SPRITE, x, this.secondLineY(firstLineY), 8, 8);
      }

      return x;
   }

   protected int firstLineY(final int rowTop) {
      return rowTop + 1;
   }

   protected int lineHeight() {
      return 2 + 9;
   }

   protected int textX(final int rowLeft) {
      return rowLeft + 36 + 2;
   }

   protected int secondLineY(final int firstLineY) {
      return firstLineY + this.lineHeight();
   }

   protected int thirdLineY(final int firstLineY) {
      return firstLineY + this.lineHeight() * 2;
   }
}
