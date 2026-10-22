package net.minecraft.client.gui.screens.multiplayer;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.NativeImage;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.SelectableEntry;
import net.minecraft.client.gui.screens.FaviconTexture;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerData$State;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.network.EventLoopGroupHolder;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ServerSelectionList$OnlineServerEntry extends ServerSelectionList$Entry implements SelectableEntry {
   private static final int ICON_SIZE = 32;
   private static final int SPACING = 5;
   private static final int STATUS_ICON_WIDTH = 10;
   private static final int STATUS_ICON_HEIGHT = 8;
   private final JoinMultiplayerScreen screen;
   private final Minecraft minecraft;
   private final ServerData serverData;
   private final FaviconTexture icon;
   @Nullable
   private byte[] lastIconBytes;
   @Nullable
   private List<Component> onlinePlayersTooltip;
   @Nullable
   private Identifier statusIcon;
   @Nullable
   private Component statusIconTooltip;

   protected ServerSelectionList$OnlineServerEntry(final ServerSelectionList this$0, final JoinMultiplayerScreen screen, final ServerData serverData) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.screen = screen;
      this.serverData = serverData;
      this.minecraft = Minecraft.getInstance();
      this.icon = FaviconTexture.forServer(this.minecraft.getTextureManager(), serverData.ip);
      this.refreshStatus();
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      if (this.serverData.state() == ServerData$State.INITIAL) {
         this.serverData.setState(ServerData$State.PINGING);
         this.serverData.motd = CommonComponents.EMPTY;
         this.serverData.status = CommonComponents.EMPTY;
         ServerSelectionList.THREAD_POOL
            .submit(
               () -> {
                  try {
                     this.screen
                        .getPinger()
                        .pingServer(
                           this.serverData,
                           () -> this.minecraft.execute(this::updateServerList),
                           () -> {
                              this.serverData
                                 .setState(
                                    this.serverData.protocol == SharedConstants.getCurrentVersion().protocolVersion()
                                       ? ServerData$State.SUCCESSFUL
                                       : ServerData$State.INCOMPATIBLE
                                 );
                              this.minecraft.execute(this::refreshStatus);
                           },
                           EventLoopGroupHolder.remote(this.minecraft.options.useNativeTransport())
                        );
                  } catch (UnknownHostException var2) {
                     this.serverData.setState(ServerData$State.UNREACHABLE);
                     this.serverData.motd = ServerSelectionList.CANT_RESOLVE_TEXT;
                     this.minecraft.execute(this::refreshStatus);
                  } catch (Exception var3) {
                     this.serverData.setState(ServerData$State.UNREACHABLE);
                     this.serverData.motd = ServerSelectionList.CANT_CONNECT_TEXT;
                     this.minecraft.execute(this::refreshStatus);
                  }
               }
            );
      }

      graphics.text(this.minecraft.font, this.serverData.name, this.getContentX() + 32 + 3, this.getContentY() + 1, -1);
      List<FormattedCharSequence> lines = this.minecraft.font.split(this.serverData.motd, this.getContentWidth() - 32 - 2);

      for (int i = 0; i < Math.min(lines.size(), 2); i++) {
         graphics.text(this.minecraft.font, lines.get(i), this.getContentX() + 32 + 3, this.getContentY() + 12 + 9 * i, -8355712);
      }

      this.extractIcon(graphics, this.getContentX(), this.getContentY(), this.icon.textureLocation());
      int index = this.this$0.children().indexOf(this);
      if (this.serverData.state() == ServerData$State.PINGING) {
         int iconIndex = (int)(Util.getMillis() / 100L + index * 2 & 7L);
         if (iconIndex > 4) {
            iconIndex = 8 - iconIndex;
         }
         this.statusIcon = switch (iconIndex) {
            case 1 -> ServerSelectionList.PINGING_2_SPRITE;
            case 2 -> ServerSelectionList.PINGING_3_SPRITE;
            case 3 -> ServerSelectionList.PINGING_4_SPRITE;
            case 4 -> ServerSelectionList.PINGING_5_SPRITE;
            default -> ServerSelectionList.PINGING_1_SPRITE;
         };
      }

      int statusIconX = this.getContentRight() - 10 - 5;
      if (this.statusIcon != null) {
         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.statusIcon, statusIconX, this.getContentY(), 10, 8);
      }

      byte[] currentIconBytes = this.serverData.getIconBytes();
      if (!Arrays.equals(currentIconBytes, this.lastIconBytes)) {
         if (this.uploadServerIcon(currentIconBytes)) {
            this.lastIconBytes = currentIconBytes;
         } else {
            this.serverData.setIconBytes(null);
            this.updateServerList();
         }
      }

      Component status = (Component)(this.serverData.state() == ServerData$State.INCOMPATIBLE
         ? this.serverData.version.copy().withStyle(ChatFormatting.RED)
         : this.serverData.status);
      int statusWidth = this.minecraft.font.width(status);
      int statusX = statusIconX - statusWidth - 5;
      graphics.text(this.minecraft.font, status, statusX, this.getContentY() + 1, -8355712);
      if (this.statusIconTooltip != null
         && mouseX >= statusIconX
         && mouseX <= statusIconX + 10
         && mouseY >= this.getContentY()
         && mouseY <= this.getContentY() + 8) {
         graphics.setTooltipForNextFrame(this.statusIconTooltip, mouseX, mouseY);
      } else if (this.onlinePlayersTooltip != null
         && mouseX >= statusX
         && mouseX <= statusX + statusWidth
         && mouseY >= this.getContentY()
         && mouseY <= this.getContentY() - 1 + 9) {
         graphics.setTooltipForNextFrame(Lists.transform(this.onlinePlayersTooltip, Component::getVisualOrderText), mouseX, mouseY);
      }

      if (hovered) {
         graphics.fill(this.getContentX(), this.getContentY(), this.getContentX() + 32, this.getContentY() + 32, -1601138544);
         int relX = mouseX - this.getContentX();
         int relY = mouseY - this.getContentY();
         if (this.mouseOverRightHalf(relX, relY, 32)) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ServerSelectionList.JOIN_HIGHLIGHTED_SPRITE, this.getContentX(), this.getContentY(), 32, 32);
            ServerSelectionList.access$000(this.this$0, graphics);
         } else {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ServerSelectionList.JOIN_SPRITE, this.getContentX(), this.getContentY(), 32, 32);
         }

         if (index > 0) {
            if (this.mouseOverTopLeftQuarter(relX, relY, 32)) {
               graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ServerSelectionList.MOVE_UP_HIGHLIGHTED_SPRITE, this.getContentX(), this.getContentY(), 32, 32);
               ServerSelectionList.access$100(this.this$0, graphics);
            } else {
               graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ServerSelectionList.MOVE_UP_SPRITE, this.getContentX(), this.getContentY(), 32, 32);
            }
         }

         if (index < this.screen.getServers().size() - 1) {
            if (this.mouseOverBottomLeftQuarter(relX, relY, 32)) {
               graphics.blitSprite(
                  RenderPipelines.GUI_TEXTURED, ServerSelectionList.MOVE_DOWN_HIGHLIGHTED_SPRITE, this.getContentX(), this.getContentY(), 32, 32
               );
               ServerSelectionList.access$200(this.this$0, graphics);
            } else {
               graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ServerSelectionList.MOVE_DOWN_SPRITE, this.getContentX(), this.getContentY(), 32, 32);
            }
         }
      }
   }

   private void refreshStatus() {
      this.onlinePlayersTooltip = null;
      switch (this.serverData.state()) {
         case INITIAL:
         case PINGING:
            this.statusIcon = ServerSelectionList.PING_1_SPRITE;
            this.statusIconTooltip = ServerSelectionList.PINGING_STATUS;
            break;
         case INCOMPATIBLE:
            this.statusIcon = ServerSelectionList.INCOMPATIBLE_SPRITE;
            this.statusIconTooltip = ServerSelectionList.INCOMPATIBLE_STATUS;
            this.onlinePlayersTooltip = this.serverData.playerList;
            break;
         case UNREACHABLE:
            this.statusIcon = ServerSelectionList.UNREACHABLE_SPRITE;
            this.statusIconTooltip = ServerSelectionList.NO_CONNECTION_STATUS;
            break;
         case SUCCESSFUL:
            if (this.serverData.ping < 150L) {
               this.statusIcon = ServerSelectionList.PING_5_SPRITE;
            } else if (this.serverData.ping < 300L) {
               this.statusIcon = ServerSelectionList.PING_4_SPRITE;
            } else if (this.serverData.ping < 600L) {
               this.statusIcon = ServerSelectionList.PING_3_SPRITE;
            } else if (this.serverData.ping < 1000L) {
               this.statusIcon = ServerSelectionList.PING_2_SPRITE;
            } else {
               this.statusIcon = ServerSelectionList.PING_1_SPRITE;
            }

            this.statusIconTooltip = Component.translatable("multiplayer.status.ping", this.serverData.ping);
            this.onlinePlayersTooltip = this.serverData.playerList;
      }
   }

   public void updateServerList() {
      this.screen.getServers().save();
   }

   protected void extractIcon(final GuiGraphicsExtractor graphics, final int rowLeft, final int rowTop, final Identifier location) {
      graphics.blit(RenderPipelines.GUI_TEXTURED, location, rowLeft, rowTop, 0.0F, 0.0F, 32, 32, 32, 32);
   }

   private boolean uploadServerIcon(@Nullable final byte[] serverIconBytes) {
      if (serverIconBytes == null) {
         this.icon.clear();
      } else {
         try {
            this.icon.upload(NativeImage.read(serverIconBytes));
         } catch (Throwable var3) {
            ServerSelectionList.LOGGER.error("Invalid icon for server {} ({})", new Object[]{this.serverData.name, this.serverData.ip, var3});
            return false;
         }
      }

      return true;
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      if (event.isSelection()) {
         this.join();
         return true;
      } else {
         if (event.hasShiftDown()) {
            ServerSelectionList list = this.screen.serverSelectionList;
            int currentIndex = list.children().indexOf(this);
            if (currentIndex == -1) {
               return true;
            }

            if (event.isDown() && currentIndex < this.screen.getServers().size() - 1 || event.isUp() && currentIndex > 0) {
               this.swap(currentIndex, event.isDown() ? currentIndex + 1 : currentIndex - 1);
               return true;
            }
         }

         return super.keyPressed(event);
      }
   }

   @Override
   public void join() {
      this.screen.join(this.serverData);
   }

   private void swap(final int currentIndex, final int newIndex) {
      this.screen.getServers().swap(currentIndex, newIndex);
      ServerSelectionList.access$300(this.screen.serverSelectionList, currentIndex, newIndex);
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      int relX = (int)event.x() - this.getContentX();
      int relY = (int)event.y() - this.getContentY();
      if (this.mouseOverRightHalf(relX, relY, 32)) {
         this.join();
         return true;
      } else {
         int currentIndex = this.screen.serverSelectionList.children().indexOf(this);
         if (currentIndex > 0 && this.mouseOverTopLeftQuarter(relX, relY, 32)) {
            this.swap(currentIndex, currentIndex - 1);
            return true;
         } else if (currentIndex < this.screen.getServers().size() - 1 && this.mouseOverBottomLeftQuarter(relX, relY, 32)) {
            this.swap(currentIndex, currentIndex + 1);
            return true;
         } else {
            if (doubleClick) {
               this.join();
            }

            return super.mouseClicked(event, doubleClick);
         }
      }
   }

   public ServerData getServerData() {
      return this.serverData;
   }

   @Override
   public Component getNarration() {
      MutableComponent narrationComponent = Component.empty();
      narrationComponent.append(Component.translatable("narrator.select", this.serverData.name));
      narrationComponent.append(CommonComponents.NARRATION_SEPARATOR);
      switch (this.serverData.state()) {
         case PINGING:
            narrationComponent.append(ServerSelectionList.PINGING_STATUS);
            break;
         case INCOMPATIBLE:
            narrationComponent.append(ServerSelectionList.INCOMPATIBLE_STATUS);
            narrationComponent.append(CommonComponents.NARRATION_SEPARATOR);
            narrationComponent.append(Component.translatable("multiplayer.status.version.narration", this.serverData.version));
            narrationComponent.append(CommonComponents.NARRATION_SEPARATOR);
            narrationComponent.append(Component.translatable("multiplayer.status.motd.narration", this.serverData.motd));
            break;
         case UNREACHABLE:
            narrationComponent.append(ServerSelectionList.NO_CONNECTION_STATUS);
            break;
         default:
            narrationComponent.append(ServerSelectionList.ONLINE_STATUS);
            narrationComponent.append(CommonComponents.NARRATION_SEPARATOR);
            narrationComponent.append(Component.translatable("multiplayer.status.ping.narration", this.serverData.ping));
            narrationComponent.append(CommonComponents.NARRATION_SEPARATOR);
            narrationComponent.append(Component.translatable("multiplayer.status.motd.narration", this.serverData.motd));
            if (this.serverData.players != null) {
               narrationComponent.append(CommonComponents.NARRATION_SEPARATOR);
               narrationComponent.append(
                  Component.translatable("multiplayer.status.player_count.narration", this.serverData.players.online(), this.serverData.players.max())
               );
               narrationComponent.append(CommonComponents.NARRATION_SEPARATOR);
               narrationComponent.append(ComponentUtils.formatList(this.serverData.playerList, Component.literal(", ")));
            }
      }

      return narrationComponent;
   }

   @Override
   public void close() {
      this.icon.close();
   }

   @Override
   protected boolean matches(final ServerSelectionList$Entry other) {
      return other instanceof ServerSelectionList$OnlineServerEntry onlineServerEntry && onlineServerEntry.serverData == this.serverData;
   }
}
