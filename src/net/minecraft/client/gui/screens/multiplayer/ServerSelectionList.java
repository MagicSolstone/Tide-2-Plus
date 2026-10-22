package net.minecraft.client.gui.screens.multiplayer;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.server.LanServer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class ServerSelectionList extends ObjectSelectionList<ServerSelectionList$Entry> {
   private static final Identifier INCOMPATIBLE_SPRITE = Identifier.withDefaultNamespace("server_list/incompatible");
   private static final Identifier UNREACHABLE_SPRITE = Identifier.withDefaultNamespace("server_list/unreachable");
   private static final Identifier PING_1_SPRITE = Identifier.withDefaultNamespace("server_list/ping_1");
   private static final Identifier PING_2_SPRITE = Identifier.withDefaultNamespace("server_list/ping_2");
   private static final Identifier PING_3_SPRITE = Identifier.withDefaultNamespace("server_list/ping_3");
   private static final Identifier PING_4_SPRITE = Identifier.withDefaultNamespace("server_list/ping_4");
   private static final Identifier PING_5_SPRITE = Identifier.withDefaultNamespace("server_list/ping_5");
   private static final Identifier PINGING_1_SPRITE = Identifier.withDefaultNamespace("server_list/pinging_1");
   private static final Identifier PINGING_2_SPRITE = Identifier.withDefaultNamespace("server_list/pinging_2");
   private static final Identifier PINGING_3_SPRITE = Identifier.withDefaultNamespace("server_list/pinging_3");
   private static final Identifier PINGING_4_SPRITE = Identifier.withDefaultNamespace("server_list/pinging_4");
   private static final Identifier PINGING_5_SPRITE = Identifier.withDefaultNamespace("server_list/pinging_5");
   private static final Identifier JOIN_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("server_list/join_highlighted");
   private static final Identifier JOIN_SPRITE = Identifier.withDefaultNamespace("server_list/join");
   private static final Identifier MOVE_UP_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("server_list/move_up_highlighted");
   private static final Identifier MOVE_UP_SPRITE = Identifier.withDefaultNamespace("server_list/move_up");
   private static final Identifier MOVE_DOWN_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("server_list/move_down_highlighted");
   private static final Identifier MOVE_DOWN_SPRITE = Identifier.withDefaultNamespace("server_list/move_down");
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final ThreadPoolExecutor THREAD_POOL = new ScheduledThreadPoolExecutor(
      5,
      new ThreadFactoryBuilder()
         .setNameFormat("Server Pinger #%d")
         .setDaemon(true)
         .setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER))
         .build()
   );
   private static final Component SCANNING_LABEL = Component.translatable("lanServer.scanning");
   private static final Component CANT_RESOLVE_TEXT = Component.translatable("multiplayer.status.cannot_resolve").withColor(-65536);
   private static final Component CANT_CONNECT_TEXT = Component.translatable("multiplayer.status.cannot_connect").withColor(-65536);
   private static final Component INCOMPATIBLE_STATUS = Component.translatable("multiplayer.status.incompatible");
   private static final Component NO_CONNECTION_STATUS = Component.translatable("multiplayer.status.no_connection");
   private static final Component PINGING_STATUS = Component.translatable("multiplayer.status.pinging");
   private static final Component ONLINE_STATUS = Component.translatable("multiplayer.status.online");
   private final JoinMultiplayerScreen screen;
   private final List<ServerSelectionList$OnlineServerEntry> onlineServers = Lists.newArrayList();
   private final ServerSelectionList$Entry lanHeader = new ServerSelectionList$LANHeader();
   private final List<ServerSelectionList$NetworkServerEntry> networkServers = Lists.newArrayList();

   public ServerSelectionList(
      final JoinMultiplayerScreen screen, final Minecraft minecraft, final int width, final int height, final int y, final int itemHeight
   ) {
      super(minecraft, width, height, y, itemHeight);
      this.screen = screen;
   }

   private void refreshEntries() {
      ServerSelectionList$Entry previouslySelected = this.getSelected();
      List<ServerSelectionList$Entry> entriesToAdd = new ArrayList<>(this.onlineServers);
      entriesToAdd.add(this.lanHeader);
      entriesToAdd.addAll(this.networkServers);
      this.replaceEntries(entriesToAdd);
      if (previouslySelected != null) {
         for (ServerSelectionList$Entry entry : entriesToAdd) {
            if (entry.matches(previouslySelected)) {
               this.setSelected(entry);
               break;
            }
         }
      }
   }

   public void setSelected(@Nullable final ServerSelectionList$Entry selected) {
      super.setSelected(selected);
      this.screen.onSelectedChange();
   }

   public void updateOnlineServers(final ServerList servers) {
      this.onlineServers.clear();

      for (int i = 0; i < servers.size(); i++) {
         this.onlineServers.add(new ServerSelectionList$OnlineServerEntry(this, this.screen, servers.get(i)));
      }

      this.refreshEntries();
   }

   public void updateNetworkServers(final List<LanServer> servers) {
      int newServerCount = servers.size() - this.networkServers.size();
      this.networkServers.clear();

      for (LanServer server : servers) {
         this.networkServers.add(new ServerSelectionList$NetworkServerEntry(this.screen, server));
      }

      this.refreshEntries();

      for (int i = this.networkServers.size() - newServerCount; i < this.networkServers.size(); i++) {
         ServerSelectionList$NetworkServerEntry newServer = this.networkServers.get(i);
         int entryIndex = i - this.networkServers.size() + this.children().size();
         int rowTop = this.getRowTop(entryIndex);
         int rowBottom = this.getRowBottom(entryIndex);
         if (rowBottom >= this.getY() && rowTop <= this.getBottom()) {
            this.minecraft.getNarrator().saySystemQueued(Component.translatable("multiplayer.lan.server_found", newServer.getServerNarration()));
         }
      }
   }

   @Override
   public int getRowWidth() {
      return 305;
   }

   public void removed() {
   }
}
