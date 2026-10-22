package net.minecraft.client.resources.server;

import com.google.common.collect.Lists;
import com.google.common.hash.HashCode;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.regex.Pattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.main.GameConfig$UserData;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.DownloadQueue;
import net.minecraft.server.packs.FilePackResources$FileResourcesSupplier;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackFormat;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.Pack$Metadata;
import net.minecraft.server.packs.repository.Pack$Position;
import net.minecraft.server.packs.repository.Pack$ResourcesSupplier;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.util.HttpUtil$DownloadProgressListener;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class DownloadedPackSource implements AutoCloseable {
   private static final Component SERVER_NAME = Component.translatable("resourcePack.server.name");
   private static final Pattern SHA1 = Pattern.compile("^[a-fA-F0-9]{40}$");
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final RepositorySource EMPTY_SOURCE = result -> {};
   private static final PackSelectionConfig DOWNLOADED_PACK_SELECTION = new PackSelectionConfig(true, Pack$Position.TOP, true);
   private static final PackLoadFeedback LOG_ONLY_FEEDBACK = new DownloadedPackSource$1();
   private final Minecraft minecraft;
   private RepositorySource packSource = EMPTY_SOURCE;
   @Nullable
   private PackReloadConfig$Callbacks pendingReload;
   private final ServerPackManager manager;
   private final DownloadQueue downloadQueue;
   private PackSource packType = PackSource.SERVER;
   private PackLoadFeedback packFeedback = LOG_ONLY_FEEDBACK;
   private int packIdSerialNumber;

   public DownloadedPackSource(final Minecraft minecraft, final Path packCache, final GameConfig$UserData user) {
      this.minecraft = minecraft;

      try {
         this.downloadQueue = new DownloadQueue(packCache);
      } catch (IOException var5) {
         throw new UncheckedIOException("Failed to open download queue in directory " + packCache, var5);
      }

      Executor executor = minecraft::schedule;
      this.manager = new ServerPackManager(
         this.createDownloader(this.downloadQueue, executor, user.user, user.proxy),
         new DownloadedPackSource$2(this),
         this.createReloadConfig(),
         this.createUpdateScheduler(executor),
         ServerPackManager$PackPromptStatus.PENDING
      );
   }

   private HttpUtil$DownloadProgressListener createDownloadNotifier(final int totalCount) {
      return new DownloadedPackSource$3(this, totalCount);
   }

   private PackDownloader createDownloader(final DownloadQueue downloadQueue, final Executor mainThreadExecutor, final User user, final Proxy proxy) {
      return new DownloadedPackSource$4(this, user, downloadQueue, proxy, mainThreadExecutor);
   }

   private Runnable createUpdateScheduler(final Executor mainThreadExecutor) {
      return new DownloadedPackSource$5(this, mainThreadExecutor);
   }

   private PackReloadConfig createReloadConfig() {
      return this::startReload;
   }

   @Nullable
   private List<Pack> loadRequestedPacks(final List<PackReloadConfig$IdAndPath> packsToLoad) {
      List<Pack> packs = new ArrayList<>(packsToLoad.size());

      for (PackReloadConfig$IdAndPath idAndPath : Lists.reverse(packsToLoad)) {
         String name = String.format(Locale.ROOT, "server/%08X/%s", this.packIdSerialNumber++, idAndPath.id());
         Path path = idAndPath.path();
         PackLocationInfo packLocationInfo = new PackLocationInfo(name, SERVER_NAME, this.packType, Optional.empty());
         Pack$ResourcesSupplier resources = new FilePackResources$FileResourcesSupplier(path);
         PackFormat currentPackVersion = SharedConstants.getCurrentVersion().packVersion(PackType.CLIENT_RESOURCES);
         Pack$Metadata metadata = Pack.readPackMetadata(packLocationInfo, resources, currentPackVersion, PackType.CLIENT_RESOURCES);
         if (metadata == null) {
            LOGGER.warn("Invalid pack metadata in {}, ignoring all", path);
            return null;
         }

         packs.add(new Pack(packLocationInfo, resources, metadata, DOWNLOADED_PACK_SELECTION));
      }

      return packs;
   }

   public RepositorySource createRepositorySource() {
      return output -> this.packSource.loadPacks(output);
   }

   private static RepositorySource configureSource(final List<Pack> packs) {
      return packs.isEmpty() ? EMPTY_SOURCE : packs::forEach;
   }

   private void startReload(final PackReloadConfig$Callbacks callbacks) {
      this.pendingReload = callbacks;
      List<PackReloadConfig$IdAndPath> normalPacks = callbacks.packsToLoad();
      List<Pack> packs = this.loadRequestedPacks(normalPacks);
      if (packs == null) {
         callbacks.onFailure(false);
         List<PackReloadConfig$IdAndPath> recoveryPacks = callbacks.packsToLoad();
         packs = this.loadRequestedPacks(recoveryPacks);
         if (packs == null) {
            LOGGER.warn("Double failure in loading server packs");
            packs = List.of();
         }
      }

      this.packSource = configureSource(packs);
      this.minecraft.reloadResourcePacks();
   }

   public void onRecovery() {
      if (this.pendingReload != null) {
         this.pendingReload.onFailure(false);
         List<Pack> packs = this.loadRequestedPacks(this.pendingReload.packsToLoad());
         if (packs == null) {
            LOGGER.warn("Double failure in loading server packs");
            packs = List.of();
         }

         this.packSource = configureSource(packs);
      }
   }

   public void onRecoveryFailure() {
      if (this.pendingReload != null) {
         this.pendingReload.onFailure(true);
         this.pendingReload = null;
         this.packSource = EMPTY_SOURCE;
      }
   }

   public void onReloadSuccess() {
      if (this.pendingReload != null) {
         this.pendingReload.onSuccess();
         this.pendingReload = null;
      }
   }

   @Nullable
   private static HashCode tryParseSha1Hash(@Nullable final String hash) {
      return hash != null && SHA1.matcher(hash).matches() ? HashCode.fromString(hash.toLowerCase(Locale.ROOT)) : null;
   }

   public void pushPack(final UUID id, final URL url, @Nullable final String hash) {
      HashCode parsedHash = tryParseSha1Hash(hash);
      this.manager.pushPack(id, url, parsedHash);
   }

   public void pushLocalPack(final UUID id, final Path path) {
      this.manager.pushLocalPack(id, path);
   }

   public void popPack(final UUID id) {
      this.manager.popPack(id);
   }

   public void popAll() {
      this.manager.popAll();
   }

   private static PackLoadFeedback createPackResponseSender(final Connection connection) {
      return new DownloadedPackSource$6(connection);
   }

   public void configureForServerControl(final Connection connection, final ServerPackManager$PackPromptStatus packPromptStatus) {
      this.packType = PackSource.SERVER;
      this.packFeedback = createPackResponseSender(connection);
      switch (packPromptStatus) {
         case ALLOWED:
            this.manager.allowServerPacks();
            break;
         case DECLINED:
            this.manager.rejectServerPacks();
            break;
         case PENDING:
            this.manager.resetPromptStatus();
      }
   }

   public void configureForLocalWorld() {
      this.packType = PackSource.WORLD;
      this.packFeedback = LOG_ONLY_FEEDBACK;
      this.manager.allowServerPacks();
   }

   public void allowServerPacks() {
      this.manager.allowServerPacks();
   }

   public void rejectServerPacks() {
      this.manager.rejectServerPacks();
   }

   public CompletableFuture<Void> waitForPackFeedback(final UUID packId) {
      CompletableFuture<Void> result = new CompletableFuture<>();
      PackLoadFeedback original = this.packFeedback;
      this.packFeedback = new DownloadedPackSource$7(this, original, packId, result);
      return result;
   }

   public void cleanupAfterDisconnect() {
      this.manager.popAll();
      this.packFeedback = LOG_ONLY_FEEDBACK;
      this.manager.resetPromptStatus();
   }

   @Override
   public void close() throws IOException {
      this.downloadQueue.close();
   }
}
