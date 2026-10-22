package net.minecraft.client.multiplayer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.SharedConstants;
import net.minecraft.client.Camera;
import net.minecraft.client.ClientClockManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintCache;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import net.minecraft.client.particle.FireworkParticles$Starter;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.EndFlashState;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.client.resources.sounds.DirectionalSoundInstance;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.Cursor3D;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction$Axis;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.profiling.Zone;
import net.minecraft.util.profiling.jfr.JvmProfiler;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.attribute.AmbientParticle;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.attribute.EnvironmentAttributeSystem$Builder;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity$RemovalReason;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.crafting.RecipeAccess;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level$ExplosionInteraction;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome$Precipitation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Block$UpdateFlags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.LevelEvent$Value;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.entity.TransientEntitySectionManager;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;
import net.minecraft.world.level.levelgen.Heightmap$Types;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.LevelData$RespawnData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.ticks.BlackholeTickAccess;
import net.minecraft.world.ticks.LevelTickAccess;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class ClientLevel extends Level implements BlockAndTintGetter, CacheSlot$Cleaner<ClientLevel> {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final Component DEFAULT_QUIT_MESSAGE = Component.translatable("multiplayer.status.quitting");
   private static final double FLUID_PARTICLE_SPAWN_OFFSET = 0.05;
   private static final int NORMAL_LIGHT_UPDATES_PER_FRAME = 10;
   private static final int LIGHT_UPDATE_QUEUE_SIZE_THRESHOLD = 1000;
   private static final float RAIN_PARTICLES_PER_BLOCK = 0.225F;
   private static final int RAIN_RADIUS = 10;
   private final EntityTickList tickingEntities = new EntityTickList();
   private final TransientEntitySectionManager<Entity> entityStorage = new TransientEntitySectionManager<>(Entity.class, new ClientLevel$EntityCallbacks(this));
   private final ClientPacketListener connection;
   private final LevelExtractor levelExtractor;
   private final LevelEventHandler levelEventHandler;
   private final ClientLevel$ClientLevelData clientLevelData;
   private final TickRateManager tickRateManager;
   @Nullable
   private final EndFlashState endFlashState;
   private final Minecraft minecraft = Minecraft.getInstance();
   private final List<AbstractClientPlayer> players = Lists.newArrayList();
   private final List<EnderDragonPart> dragonParts = Lists.newArrayList();
   private final Map<MapId, MapItemSavedData> mapData = Maps.newHashMap();
   private int skyFlashTime;
   private int rainSoundTime;
   private final Object2ObjectArrayMap<ColorResolver, BlockTintCache> tintCaches = Util.make(new Object2ObjectArrayMap(3), cache -> {
      cache.put(BiomeColors.GRASS_COLOR_RESOLVER, new BlockTintCache(pos -> this.calculateBlockTint(pos, BiomeColors.GRASS_COLOR_RESOLVER)));
      cache.put(BiomeColors.FOLIAGE_COLOR_RESOLVER, new BlockTintCache(pos -> this.calculateBlockTint(pos, BiomeColors.FOLIAGE_COLOR_RESOLVER)));
      cache.put(BiomeColors.DRY_FOLIAGE_COLOR_RESOLVER, new BlockTintCache(pos -> this.calculateBlockTint(pos, BiomeColors.DRY_FOLIAGE_COLOR_RESOLVER)));
      cache.put(BiomeColors.WATER_COLOR_RESOLVER, new BlockTintCache(pos -> this.calculateBlockTint(pos, BiomeColors.WATER_COLOR_RESOLVER)));
   });
   private final ClientChunkCache chunkSource;
   private final Deque<Runnable> lightUpdateQueue = Queues.newArrayDeque();
   private int serverSimulationDistance;
   private final BlockStatePredictionHandler blockStatePredictionHandler = new BlockStatePredictionHandler();
   private final Set<BlockEntity> globallyRenderedBlockEntities = new ReferenceOpenHashSet();
   private final ClientExplosionTracker explosionTracker = new ClientExplosionTracker();
   private final WorldBorder worldBorder = new WorldBorder();
   private final EnvironmentAttributeSystem environmentAttributes;
   private final Int2ObjectMap<BlockDestructionProgress> destroyingBlocks = new Int2ObjectOpenHashMap();
   private final Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress = new Long2ObjectOpenHashMap();
   private final int seaLevel;
   private static final Set<Item> MARKER_PARTICLE_ITEMS = Set.of(Items.BARRIER, Items.LIGHT);

   public void handleBlockChangedAck(final int sequence) {
      if (SharedConstants.DEBUG_BLOCK_BREAK) {
         LOGGER.debug("ACK {}", sequence);
      }

      this.blockStatePredictionHandler.endPredictionsUpTo(sequence, this);
   }

   @Override
   public void onBlockEntityAdded(final BlockEntity blockEntity) {
      BlockEntityRenderer<BlockEntity, ?> renderer = this.minecraft.getBlockEntityRenderDispatcher().getRenderer(blockEntity);
      if (renderer != null && renderer.shouldRenderOffScreen()) {
         this.globallyRenderedBlockEntities.add(blockEntity);
      }
   }

   public Set<BlockEntity> getGloballyRenderedBlockEntities() {
      return this.globallyRenderedBlockEntities;
   }

   public void setServerVerifiedBlockState(final BlockPos pos, final BlockState blockState, @Block$UpdateFlags final int updateFlag) {
      if (!this.blockStatePredictionHandler.updateKnownServerState(pos, blockState)) {
         super.setBlock(pos, blockState, updateFlag, 512);
      }
   }

   public void syncBlockState(final BlockPos pos, final BlockState state, @Nullable final Vec3 playerPos) {
      BlockState oldState = this.getBlockState(pos);
      if (oldState != state) {
         this.setBlock(pos, state, 19);
         Player player = this.minecraft.player;
         if (playerPos != null && this == player.level() && player.isColliding(pos, state)) {
            player.absSnapTo(playerPos.x, playerPos.y, playerPos.z);
         }
      }
   }

   BlockStatePredictionHandler getBlockStatePredictionHandler() {
      return this.blockStatePredictionHandler;
   }

   @Override
   public boolean setBlock(final BlockPos pos, final BlockState blockState, @Block$UpdateFlags final int updateFlags, final int updateLimit) {
      if (this.blockStatePredictionHandler.isPredicting()) {
         BlockState oldState = this.getBlockState(pos);
         boolean success = super.setBlock(pos, blockState, updateFlags, updateLimit);
         if (success) {
            this.blockStatePredictionHandler.retainKnownServerState(pos, oldState, this.minecraft.player);
         }

         return success;
      } else {
         return super.setBlock(pos, blockState, updateFlags, updateLimit);
      }
   }

   public ClientLevel(
      final ClientPacketListener connection,
      final ClientLevel$ClientLevelData levelData,
      final ResourceKey<Level> dimension,
      final Holder<DimensionType> dimensionType,
      final int serverChunkRadius,
      final int serverSimulationDistance,
      final LevelExtractor levelExtractor,
      final boolean isDebug,
      final long biomeZoomSeed,
      final int seaLevel
   ) {
      super(levelData, dimension, connection.registryAccess(), dimensionType, true, isDebug, biomeZoomSeed, 1000000);
      this.connection = connection;
      this.chunkSource = new ClientChunkCache(this, serverChunkRadius);
      this.tickRateManager = new TickRateManager();
      this.clientLevelData = levelData;
      this.levelExtractor = levelExtractor;
      this.seaLevel = seaLevel;
      this.levelEventHandler = new LevelEventHandler(this.minecraft, this);
      this.endFlashState = dimensionType.value().hasEndFlashes() ? new EndFlashState() : null;
      this.setRespawnData(LevelData$RespawnData.of(dimension, new BlockPos(8, 64, 8), 0.0F, 0.0F));
      this.serverSimulationDistance = serverSimulationDistance;
      this.environmentAttributes = this.addEnvironmentAttributeLayers(EnvironmentAttributeSystem.builder()).build();
      this.updateSkyBrightness();
   }

   private EnvironmentAttributeSystem$Builder addEnvironmentAttributeLayers(final EnvironmentAttributeSystem$Builder environmentAttributes) {
      environmentAttributes.addDefaultLayers(this);
      Vector3fc flashColor = new Vector3f(0.8F, 0.8F, 1.0F);
      environmentAttributes.addTimeBasedLayer(
         EnvironmentAttributes.SKY_COLOR, (skyColor, cacheTickId) -> this.getSkyFlashTime() > 0 ? ARGB.srgbLerp(0.22F, skyColor, flashColor) : skyColor
      );
      environmentAttributes.addTimeBasedLayer(EnvironmentAttributes.SKY_LIGHT_FACTOR, (skyFactor, cacheTickId) -> this.getSkyFlashTime() > 0 ? 1.0F : skyFactor);
      return environmentAttributes;
   }

   public void queueLightUpdate(final Runnable update) {
      this.lightUpdateQueue.add(update);
   }

   public void pollLightUpdates() {
      int size = this.lightUpdateQueue.size();
      int lightUpdatesPerFrame = size < 1000 ? Math.max(10, size / 10) : size;

      for (int i = 0; i < lightUpdatesPerFrame; i++) {
         Runnable update = this.lightUpdateQueue.poll();
         if (update == null) {
            break;
         }

         update.run();
      }
   }

   @Nullable
   public EndFlashState endFlashState() {
      return this.endFlashState;
   }

   public void tick(final BooleanSupplier haveTime) {
      this.updateSkyBrightness();
      if (this.tickRateManager().runsNormally()) {
         this.getWorldBorder().tick();
         this.tickTime();
         this.tickWeatherEffects();
         this.removeBlockBreakingProgress();
      }

      if (this.skyFlashTime > 0) {
         this.setSkyFlashTime(this.skyFlashTime - 1);
      }

      if (this.endFlashState != null) {
         this.endFlashState.tick(this.getDefaultClockTime());
         if (this.endFlashState.flashStartedThisTick() && !(this.minecraft.gui.screen() instanceof WinScreen)) {
            this.minecraft
               .getSoundManager()
               .playDelayed(
                  new DirectionalSoundInstance(
                     SoundEvents.WEATHER_END_FLASH,
                     SoundSource.WEATHER,
                     this.random,
                     this.minecraft.gameRenderer.mainCamera(),
                     this.endFlashState.getXAngle(),
                     this.endFlashState.getYAngle()
                  ),
                  30
               );
         }
      }

      this.explosionTracker.tick(this);

      try (Zone ignored = Profiler.get().zone("blocks")) {
         this.chunkSource.tick(haveTime, true);
      }

      JvmProfiler.INSTANCE.onClientTick(this.minecraft.getFps());
      this.environmentAttributes().invalidateTickCache();
   }

   public void tickWeatherEffects() {
      ParticleStatus particleStatus = this.minecraft.options.particles().get();
      int weatherRadius = this.minecraft.options.weatherRadius().get();
      float rainLevel = this.getRainLevel(1.0F);
      if (!(rainLevel <= 0.0F)) {
         RandomSource random = RandomSource.createThreadLocalInstance(this.getGameTime() * 312987231L);
         BlockPos cameraPosition = BlockPos.containing(this.minecraft.gameRenderer.mainCamera().position());
         BlockPos rainParticlePosition = null;
         int weatherDiameter = 2 * weatherRadius + 1;
         int weatherArea = weatherDiameter * weatherDiameter;
         int rainParticles = (int)(0.225F * weatherArea * rainLevel * rainLevel) / (particleStatus == ParticleStatus.DECREASED ? 2 : 1);

         for (int ii = 0; ii < rainParticles; ii++) {
            int x = random.nextInt(weatherDiameter) - weatherRadius;
            int z = random.nextInt(weatherDiameter) - weatherRadius;
            BlockPos heightmapPosition = this.getHeightmapPos(Heightmap$Types.MOTION_BLOCKING, cameraPosition.offset(x, 0, z));
            if (heightmapPosition.getY() > this.getMinY()
               && heightmapPosition.getY() <= cameraPosition.getY() + 10
               && heightmapPosition.getY() >= cameraPosition.getY() - 10
               && this.getPrecipitationAt(heightmapPosition) == Biome$Precipitation.RAIN) {
               rainParticlePosition = heightmapPosition.below();
               if (particleStatus == ParticleStatus.MINIMAL) {
                  break;
               }

               double blockX = random.nextDouble();
               double blockZ = random.nextDouble();
               BlockState block = this.getBlockState(rainParticlePosition);
               FluidState fluid = this.getFluidState(rainParticlePosition);
               VoxelShape blockShape = block.getCollisionShape(this, rainParticlePosition);
               double blockTop = blockShape.max(Direction$Axis.Y, blockX, blockZ);
               double fluidTop = fluid.getHeight(this, rainParticlePosition);
               double particleY = Math.max(blockTop, fluidTop);
               ParticleOptions particleType = !fluid.is(FluidTags.LAVA) && !block.is(Blocks.MAGMA_BLOCK) && !CampfireBlock.isLitCampfire(block)
                  ? ParticleTypes.RAIN
                  : ParticleTypes.SMOKE;
               this.addParticle(
                  particleType,
                  rainParticlePosition.getX() + blockX,
                  rainParticlePosition.getY() + particleY,
                  rainParticlePosition.getZ() + blockZ,
                  0.0,
                  0.0,
                  0.0
               );
            }
         }

         if (rainParticlePosition != null && random.nextInt(3) < this.rainSoundTime++) {
            this.rainSoundTime = 0;
            if (rainParticlePosition.getY() > cameraPosition.getY() + 1
               && this.getHeightmapPos(Heightmap$Types.MOTION_BLOCKING, cameraPosition).getY() > Mth.floor((float)cameraPosition.getY())) {
               this.playLocalSound(rainParticlePosition, SoundEvents.WEATHER_RAIN_ABOVE, SoundSource.WEATHER, 0.1F, 0.5F, false);
            } else {
               this.playLocalSound(rainParticlePosition, SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.2F, 1.0F, false);
            }
         }
      }
   }

   public Biome$Precipitation getPrecipitationAt(final BlockPos pos) {
      if (!this.chunkSource.hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()))) {
         return Biome$Precipitation.NONE;
      } else {
         Biome biome = this.getBiome(pos).value();
         return biome.getPrecipitationAt(pos, this.getSeaLevel());
      }
   }

   private void removeBlockBreakingProgress() {
      long gameTime = this.getGameTime();
      if (gameTime % 20L == 0L) {
         Iterator<BlockDestructionProgress> iterator = this.destroyingBlocks.values().iterator();

         while (iterator.hasNext()) {
            BlockDestructionProgress block = iterator.next();
            long updatedRenderTick = block.getUpdatedRenderTick();
            if (gameTime - updatedRenderTick > 400L) {
               iterator.remove();
               this.removeProgress(block);
            }
         }
      }
   }

   private void removeProgress(final BlockDestructionProgress block) {
      long pos = block.getPos().asLong();
      Set<BlockDestructionProgress> progresses = (Set<BlockDestructionProgress>)this.destructionProgress.get(pos);
      progresses.remove(block);
      if (progresses.isEmpty()) {
         this.destructionProgress.remove(pos);
      }
   }

   public Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress() {
      return this.destructionProgress;
   }

   private void tickTime() {
      long gameTime = this.clientLevelData.getGameTime() + 1L;
      this.clientLevelData.setGameTime(gameTime);
      this.clockManager().tick(gameTime);
   }

   public void setTimeFromServer(final long gameTime) {
      this.clientLevelData.setGameTime(gameTime);
   }

   public Iterable<Entity> entitiesForRendering() {
      return this.getEntities().getAll();
   }

   public void tickEntities() {
      this.tickingEntities.forEach(entity -> {
         if (!entity.isRemoved() && !entity.isPassenger() && !this.tickRateManager.isEntityFrozen(entity)) {
            this.guardEntityTick(this::tickNonPassenger, entity);
         }
      });
   }

   public boolean isTickingEntity(final Entity entity) {
      return this.tickingEntities.contains(entity);
   }

   @Override
   public boolean shouldTickDeath(final Entity entity) {
      return entity.chunkPosition().getChessboardDistance(this.minecraft.player.chunkPosition()) <= this.serverSimulationDistance;
   }

   public void tickNonPassenger(final Entity entity) {
      Profiler.get().push(entity.typeHolder()::getRegisteredName);
      entity.commonTick();
      entity.tick();
      Profiler.get().pop();

      for (Entity passenger : entity.getPassengers()) {
         this.tickPassenger(entity, passenger);
      }
   }

   private void tickPassenger(final Entity vehicle, final Entity entity) {
      if (entity.isRemoved() || entity.getVehicle() != vehicle) {
         entity.stopRiding();
      } else if (entity instanceof Player || this.tickingEntities.contains(entity)) {
         entity.commonTick();
         entity.rideTick();

         for (Entity passenger : entity.getPassengers()) {
            this.tickPassenger(entity, passenger);
         }
      }
   }

   public void update() {
      ProfilerFiller profiler = Profiler.get();
      profiler.push("populateLightUpdates");
      this.pollLightUpdates();
      profiler.popPush("runLightUpdates");
      this.getChunkSource().getLightEngine().runLightUpdates();
      profiler.pop();
   }

   public void unload(final LevelChunk levelChunk) {
      levelChunk.clearAllBlockEntities();
      this.chunkSource.getLightEngine().setLightEnabled(levelChunk.getPos(), false);
      this.entityStorage.stopTicking(levelChunk.getPos());
   }

   public void onChunkLoaded(final ChunkPos pos) {
      this.tintCaches.forEach((resolver, cache) -> cache.invalidateForChunk(pos.x(), pos.z()));
      this.entityStorage.startTicking(pos);
   }

   public void clearTintCaches() {
      this.tintCaches.forEach((resolver, cache) -> cache.invalidateAll());
   }

   @Override
   public boolean hasChunk(final int chunkX, final int chunkZ) {
      return true;
   }

   public int getEntityCount() {
      return this.entityStorage.count();
   }

   public void addEntity(final Entity entity) {
      this.removeEntity(entity.getId(), Entity$RemovalReason.DISCARDED);
      this.entityStorage.addEntity(entity);
   }

   public void removeEntity(final int id, final Entity$RemovalReason reason) {
      Entity entity = this.getEntities().get(id);
      if (entity != null) {
         entity.setRemoved(reason);
         entity.onClientRemoval();
      }
   }

   @Override
   public List<Entity> getPushableEntities(final Entity pusher, final AABB boundingBox) {
      LocalPlayer player = this.minecraft.player;
      return player != null && player != pusher && player.getBoundingBox().intersects(boundingBox) && EntitySelector.pushableBy(pusher).test(player)
         ? List.of(player)
         : List.of();
   }

   @Nullable
   @Override
   public Entity getEntity(final int id) {
      return this.getEntities().get(id);
   }

   public void disconnect(final Component message) {
      this.connection.getConnection().disconnect(message);
      this.minecraft.getPlayerSocialManager().getPresenceHandler().tryUpdatePresence();
   }

   public void animateTick(final int xt, final int yt, final int zt) {
      int r = 32;
      RandomSource animateRandom = RandomSource.createThreadLocalInstance();
      Block markerParticleTarget = this.getMarkerParticleTarget();
      BlockPos$MutableBlockPos pos = new BlockPos$MutableBlockPos();

      for (int i = 0; i < 667; i++) {
         this.doAnimateTick(xt, yt, zt, 16, animateRandom, markerParticleTarget, pos);
         this.doAnimateTick(xt, yt, zt, 32, animateRandom, markerParticleTarget, pos);
      }
   }

   @Nullable
   private Block getMarkerParticleTarget() {
      if (this.minecraft.gameMode.getPlayerMode() == GameType.CREATIVE) {
         ItemStack carriedItemStack = this.minecraft.player.getMainHandItem();
         Item carriedItem = carriedItemStack.getItem();
         if (MARKER_PARTICLE_ITEMS.contains(carriedItem) && carriedItem instanceof BlockItem blockItem) {
            return blockItem.getBlock();
         }
      }

      return null;
   }

   public void doAnimateTick(
      final int xt,
      final int yt,
      final int zt,
      final int r,
      final RandomSource animateRandom,
      @Nullable final Block markerParticleTarget,
      final BlockPos$MutableBlockPos pos
   ) {
      int x = xt + this.random.nextInt(r) - this.random.nextInt(r);
      int y = yt + this.random.nextInt(r) - this.random.nextInt(r);
      int z = zt + this.random.nextInt(r) - this.random.nextInt(r);
      pos.set(x, y, z);
      BlockState state = this.getBlockState(pos);
      state.getBlock().animateTick(state, this, pos, animateRandom);
      FluidState fluidState = this.getFluidState(pos);
      if (!fluidState.isEmpty()) {
         fluidState.animateTick(this, pos, animateRandom);
         ParticleOptions dripParticle = fluidState.getDripParticle();
         if (dripParticle != null && this.random.nextInt(10) == 0) {
            boolean hasWatertightBottom = state.isFaceSturdy(this, pos, Direction.DOWN);
            BlockPos below = pos.below();
            this.trySpawnDripParticles(below, this.getBlockState(below), dripParticle, hasWatertightBottom);
         }
      }

      if (markerParticleTarget == state.getBlock()) {
         this.addParticle(new BlockParticleOption(ParticleTypes.BLOCK_MARKER, state), x + 0.5, y + 0.5, z + 0.5, 0.0, 0.0, 0.0);
      }

      if (!state.isCollisionShapeFullBlock(this, pos)) {
         for (AmbientParticle particle : this.environmentAttributes().getValue(EnvironmentAttributes.AMBIENT_PARTICLES, pos)) {
            if (particle.canSpawn(this.random)) {
               this.addParticle(
                  particle.particle(),
                  pos.getX() + this.random.nextDouble(),
                  pos.getY() + this.random.nextDouble(),
                  pos.getZ() + this.random.nextDouble(),
                  0.0,
                  0.0,
                  0.0
               );
            }
         }
      }
   }

   private void trySpawnDripParticles(final BlockPos pos, final BlockState state, final ParticleOptions dripParticle, final boolean isTopSolid) {
      if (state.getFluidState().isEmpty()) {
         VoxelShape collisionShape = state.getCollisionShape(this, pos);
         double topSideHeight = collisionShape.max(Direction$Axis.Y);
         if (topSideHeight < 1.0) {
            if (isTopSolid) {
               this.spawnFluidParticle(pos.getX(), pos.getX() + 1, pos.getZ(), pos.getZ() + 1, pos.getY() + 1 - 0.05, dripParticle);
            }
         } else if (!state.is(BlockTags.IMPERMEABLE)) {
            double bottomSideHeight = collisionShape.min(Direction$Axis.Y);
            if (bottomSideHeight > 0.0) {
               this.spawnParticle(pos, dripParticle, collisionShape, pos.getY() + bottomSideHeight - 0.05);
            } else {
               BlockPos below = pos.below();
               BlockState belowState = this.getBlockState(below);
               VoxelShape belowShape = belowState.getCollisionShape(this, below);
               double belowTopSideHeight = belowShape.max(Direction$Axis.Y);
               if (belowTopSideHeight < 1.0 && belowState.getFluidState().isEmpty()) {
                  this.spawnParticle(pos, dripParticle, collisionShape, pos.getY() - 0.05);
               }
            }
         }
      }
   }

   private void spawnParticle(final BlockPos pos, final ParticleOptions dripParticle, final VoxelShape dripShape, final double height) {
      this.spawnFluidParticle(
         pos.getX() + dripShape.min(Direction$Axis.X),
         pos.getX() + dripShape.max(Direction$Axis.X),
         pos.getZ() + dripShape.min(Direction$Axis.Z),
         pos.getZ() + dripShape.max(Direction$Axis.Z),
         height,
         dripParticle
      );
   }

   private void spawnFluidParticle(final double x1, final double x2, final double z1, final double z2, final double y, final ParticleOptions dripParticle) {
      this.addParticle(dripParticle, Mth.lerp(this.random.nextDouble(), x1, x2), y, Mth.lerp(this.random.nextDouble(), z1, z2), 0.0, 0.0, 0.0);
   }

   @Override
   public CrashReportCategory fillReportDetails(final CrashReport report) {
      CrashReportCategory category = super.fillReportDetails(report);
      category.setDetail("Server brand", () -> this.minecraft.player.connection.serverBrand());
      category.setDetail(
         "Server type", () -> this.minecraft.getSingleplayerServer() == null ? "Non-integrated multiplayer server" : "Integrated singleplayer server"
      );
      category.setDetail("Tracked entity count", () -> String.valueOf(this.getEntityCount()));
      category.setDetail("Client weather", () -> String.format(Locale.ROOT, "Raining: %b, thundering: %b", this.isRaining(), this.isThundering()));
      return category;
   }

   @Override
   public void playSeededSound(
      @Nullable final Entity except,
      final double x,
      final double y,
      final double z,
      final Holder<SoundEvent> sound,
      final SoundSource source,
      final float volume,
      final float pitch,
      final long seed
   ) {
      if (except == this.minecraft.player) {
         this.playSound(x, y, z, sound.value(), source, volume, pitch, false, seed);
      }
   }

   @Override
   public void playSeededSound(
      @Nullable final Entity except,
      final Entity sourceEntity,
      final Holder<SoundEvent> sound,
      final SoundSource source,
      final float volume,
      final float pitch,
      final long seed
   ) {
      if (except == this.minecraft.player) {
         this.minecraft.getSoundManager().play(new EntityBoundSoundInstance(sound.value(), source, volume, pitch, sourceEntity, seed));
      }
   }

   @Override
   public void playLocalSound(final Entity sourceEntity, final SoundEvent sound, final SoundSource source, final float volume, final float pitch) {
      this.minecraft.getSoundManager().play(new EntityBoundSoundInstance(sound, source, volume, pitch, sourceEntity, this.random.nextLong()));
   }

   @Override
   public void playPlayerSound(final SoundEvent sound, final SoundSource source, final float volume, final float pitch) {
      if (this.minecraft.player != null) {
         this.minecraft.getSoundManager().play(new EntityBoundSoundInstance(sound, source, volume, pitch, this.minecraft.player, this.random.nextLong()));
      }
   }

   @Override
   public void playLocalSound(
      final double x,
      final double y,
      final double z,
      final SoundEvent sound,
      final SoundSource source,
      final float volume,
      final float pitch,
      final boolean distanceDelay
   ) {
      this.playSound(x, y, z, sound, source, volume, pitch, distanceDelay, this.random.nextLong());
   }

   private void playSound(
      final double x,
      final double y,
      final double z,
      final SoundEvent sound,
      final SoundSource source,
      final float volume,
      final float pitch,
      final boolean distanceDelay,
      final long seed
   ) {
      double distanceToSqr = this.minecraft.gameRenderer.mainCamera().position().distanceToSqr(x, y, z);
      SimpleSoundInstance instance = new SimpleSoundInstance(sound, source, volume, pitch, RandomSource.create(seed), x, y, z);
      if (distanceDelay && distanceToSqr > 100.0) {
         double delayInSeconds = Math.sqrt(distanceToSqr) / 40.0;
         this.minecraft.getSoundManager().playDelayed(instance, (int)(delayInSeconds * 20.0));
      } else {
         this.minecraft.getSoundManager().play(instance);
      }
   }

   @Override
   public void createFireworks(
      final double x,
      final double y,
      final double z,
      final double xd,
      final double yd,
      final double zd,
      final List<FireworkExplosion> explosions,
      final boolean playSound
   ) {
      if (explosions.isEmpty()) {
         for (int i = 0; i < this.random.nextInt(3) + 2; i++) {
            this.addParticle(ParticleTypes.POOF, x, y, z, this.random.nextGaussian() * 0.05, 0.005, this.random.nextGaussian() * 0.05);
         }
      } else {
         this.minecraft.particleEngine.add(new FireworkParticles$Starter(this, x, y, z, xd, yd, zd, this.minecraft.particleEngine, explosions, playSound));
      }
   }

   @Override
   public void sendPacketToServer(final Packet<?> packet) {
      this.connection.send(packet);
   }

   @Override
   public WorldBorder getWorldBorder() {
      return this.worldBorder;
   }

   @Override
   public RecipeAccess recipeAccess() {
      return this.connection.recipes();
   }

   @Override
   public TickRateManager tickRateManager() {
      return this.tickRateManager;
   }

   @Override
   public float getRelativeTickSpeed() {
      float tickrate = this.tickRateManager.tickrate();
      return tickrate > 20.0F ? tickrate / 20.0F : 1.0F;
   }

   public ClientClockManager clockManager() {
      return this.connection.clockManager();
   }

   @Override
   public EnvironmentAttributeSystem environmentAttributes() {
      return this.environmentAttributes;
   }

   @Override
   public LevelTickAccess<Block> getBlockTicks() {
      return BlackholeTickAccess.emptyLevelList();
   }

   @Override
   public LevelTickAccess<Fluid> getFluidTicks() {
      return BlackholeTickAccess.emptyLevelList();
   }

   public ClientChunkCache getChunkSource() {
      return this.chunkSource;
   }

   @Nullable
   @Override
   public MapItemSavedData getMapData(final MapId id) {
      return this.mapData.get(id);
   }

   public void overrideMapData(final MapId id, final MapItemSavedData data) {
      this.mapData.put(id, data);
   }

   @Override
   public Scoreboard getScoreboard() {
      return this.connection.scoreboard();
   }

   @Override
   public void sendBlockUpdated(final BlockPos pos, final BlockState old, final BlockState current, @Block$UpdateFlags final int updateFlags) {
      this.levelExtractor.blockChanged(pos, updateFlags);
   }

   @Override
   public void setBlocksDirty(final BlockPos pos, final BlockState oldState, final BlockState newState) {
      this.levelExtractor.setBlockDirty(pos, oldState, newState);
   }

   public void setSectionDirtyWithNeighbors(final int chunkX, final int chunkY, final int chunkZ) {
      this.levelExtractor.setSectionDirtyWithNeighbors(chunkX, chunkY, chunkZ);
   }

   public void setSectionRangeDirty(
      final int minSectionX, final int minSectionY, final int minSectionZ, final int maxSectionX, final int maxSectionY, final int maxSectionZ
   ) {
      this.levelExtractor.setSectionRangeDirty(minSectionX, minSectionY, minSectionZ, maxSectionX, maxSectionY, maxSectionZ);
   }

   @Override
   public void destroyBlockProgress(final int id, final BlockPos pos, final int progress) {
      if (progress >= 0 && progress < 10) {
         BlockDestructionProgress entry = (BlockDestructionProgress)this.destroyingBlocks.get(id);
         if (entry != null) {
            this.removeProgress(entry);
         }

         if (entry == null || entry.getPos().getX() != pos.getX() || entry.getPos().getY() != pos.getY() || entry.getPos().getZ() != pos.getZ()) {
            entry = new BlockDestructionProgress(id, pos);
            this.destroyingBlocks.put(id, entry);
         }

         entry.setProgress(progress);
         entry.updateTick(this.getGameTime());
         ((SortedSet)this.destructionProgress.computeIfAbsent(entry.getPos().asLong(), k -> Sets.newTreeSet())).add(entry);
      } else {
         BlockDestructionProgress removed = (BlockDestructionProgress)this.destroyingBlocks.remove(id);
         if (removed != null) {
            this.removeProgress(removed);
         }
      }
   }

   @Override
   public void globalLevelEvent(@LevelEvent$Value final int type, final BlockPos pos, final int data) {
      this.levelEventHandler.globalLevelEvent(type, pos, data);
   }

   @Override
   public void levelEvent(@Nullable final Entity source, @LevelEvent$Value final int type, final BlockPos pos, final int data) {
      try {
         this.levelEventHandler.levelEvent(type, pos, data);
      } catch (Throwable var8) {
         CrashReport report = CrashReport.forThrowable(var8, "Playing level event");
         CrashReportCategory category = report.addCategory("Level event being played");
         category.setDetail("Block coordinates", CrashReportCategory.formatLocation(this, pos));
         category.setDetail("Event source", source);
         category.setDetail("Event type", type);
         category.setDetail("Event data", data);
         throw new ReportedException(report);
      }
   }

   @Override
   public void addParticle(final ParticleOptions particle, final double x, final double y, final double z, final double xd, final double yd, final double zd) {
      this.doAddParticle(particle, particle.getType().getOverrideLimiter(), false, x, y, z, xd, yd, zd);
   }

   @Override
   public void addParticle(
      final ParticleOptions particle,
      final boolean overrideLimiter,
      final boolean alwaysShow,
      final double x,
      final double y,
      final double z,
      final double xd,
      final double yd,
      final double zd
   ) {
      this.doAddParticle(particle, particle.getType().getOverrideLimiter() || overrideLimiter, alwaysShow, x, y, z, xd, yd, zd);
   }

   @Override
   public void addAlwaysVisibleParticle(
      final ParticleOptions particle, final double x, final double y, final double z, final double xd, final double yd, final double zd
   ) {
      this.doAddParticle(particle, false, true, x, y, z, xd, yd, zd);
   }

   @Override
   public void addAlwaysVisibleParticle(
      final ParticleOptions particle,
      final boolean overrideLimiter,
      final double x,
      final double y,
      final double z,
      final double xd,
      final double yd,
      final double zd
   ) {
      this.doAddParticle(particle, particle.getType().getOverrideLimiter() || overrideLimiter, true, x, y, z, xd, yd, zd);
   }

   private void doAddParticle(
      final ParticleOptions particle,
      final boolean overrideLimiter,
      final boolean alwaysShowParticles,
      final double x,
      final double y,
      final double z,
      final double xd,
      final double yd,
      final double zd
   ) {
      try {
         Camera camera = this.minecraft.gameRenderer.mainCamera();
         ParticleStatus particleLevel = this.calculateParticleLevel(alwaysShowParticles);
         if (overrideLimiter) {
            this.minecraft.particleEngine.createParticle(particle, x, y, z, xd, yd, zd);
         } else if (!(camera.position().distanceToSqr(x, y, z) > 1024.0)) {
            if (particleLevel != ParticleStatus.MINIMAL) {
               this.minecraft.particleEngine.createParticle(particle, x, y, z, xd, yd, zd);
            }
         }
      } catch (Throwable var19) {
         CrashReport report = CrashReport.forThrowable(var19, "Exception while adding particle");
         CrashReportCategory category = report.addCategory("Particle being added");
         category.setDetail("ID", BuiltInRegistries.PARTICLE_TYPE.getKey(particle.getType()));
         category.setDetail(
            "Parameters", () -> ParticleTypes.CODEC.encodeStart(this.registryAccess().createSerializationContext(NbtOps.INSTANCE), particle).toString()
         );
         category.setDetail("Position", () -> CrashReportCategory.formatLocation(this, x, y, z));
         throw new ReportedException(report);
      }
   }

   private ParticleStatus calculateParticleLevel(final boolean alwaysShowParticles) {
      ParticleStatus particleLevel = this.minecraft.options.particles().get();
      if (alwaysShowParticles && particleLevel == ParticleStatus.MINIMAL && this.random.nextInt(10) == 0) {
         particleLevel = ParticleStatus.DECREASED;
      }

      if (particleLevel == ParticleStatus.DECREASED && this.random.nextInt(3) == 0) {
         particleLevel = ParticleStatus.MINIMAL;
      }

      return particleLevel;
   }

   @Override
   public List<AbstractClientPlayer> players() {
      return this.players;
   }

   public List<EnderDragonPart> dragonParts() {
      return this.dragonParts;
   }

   @Override
   public Holder<Biome> getUncachedNoiseBiome(final int quartX, final int quartY, final int quartZ) {
      return this.registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS);
   }

   private int getSkyFlashTime() {
      return this.minecraft.options.hideLightningFlash().get() ? 0 : this.skyFlashTime;
   }

   @Override
   public void setSkyFlashTime(final int skyFlashTime) {
      this.skyFlashTime = skyFlashTime;
   }

   @Override
   public CardinalLighting cardinalLighting() {
      return this.dimensionType().cardinalLightType().get();
   }

   @Override
   public int getBlockTint(final BlockPos pos, final ColorResolver resolver) {
      BlockTintCache cache = (BlockTintCache)this.tintCaches.get(resolver);
      return cache.getColor(pos);
   }

   public int calculateBlockTint(final BlockPos pos, final ColorResolver colorResolver) {
      int dist = Minecraft.getInstance().options.biomeBlendRadius().get();
      if (dist == 0) {
         return colorResolver.getColor(this.getBiome(pos).value(), pos.getX(), pos.getZ());
      } else {
         int count = (dist * 2 + 1) * (dist * 2 + 1);
         int totalRed = 0;
         int totalGreen = 0;
         int totalBlue = 0;
         Cursor3D cursor = new Cursor3D(pos.getX() - dist, pos.getY(), pos.getZ() - dist, pos.getX() + dist, pos.getY(), pos.getZ() + dist);
         BlockPos$MutableBlockPos nextPos = new BlockPos$MutableBlockPos();

         while (cursor.advance()) {
            nextPos.set(cursor.nextX(), cursor.nextY(), cursor.nextZ());
            int color = colorResolver.getColor(this.getBiome(nextPos).value(), nextPos.getX(), nextPos.getZ());
            totalRed += ARGB.red(color);
            totalGreen += ARGB.green(color);
            totalBlue += ARGB.blue(color);
         }

         return ARGB.color(totalRed / count, totalGreen / count, totalBlue / count);
      }
   }

   @Override
   public void setRespawnData(final LevelData$RespawnData respawnData) {
      this.levelData.setSpawn(this.getWorldBorderAdjustedRespawnData(respawnData));
   }

   @Override
   public LevelData$RespawnData getRespawnData() {
      return this.levelData.getRespawnData();
   }

   @Override
   public String toString() {
      return "ClientLevel";
   }

   public ClientLevel$ClientLevelData getLevelData() {
      return this.clientLevelData;
   }

   @Override
   public void gameEvent(final Holder<GameEvent> gameEvent, final Vec3 pos, final GameEvent$Context context) {
   }

   protected Map<MapId, MapItemSavedData> getAllMapData() {
      return ImmutableMap.copyOf(this.mapData);
   }

   protected void addMapData(final Map<MapId, MapItemSavedData> mapData) {
      this.mapData.putAll(mapData);
   }

   @Override
   protected LevelEntityGetter<Entity> getEntities() {
      return this.entityStorage.getEntityGetter();
   }

   @Override
   public String gatherChunkSourceStats() {
      return "Chunks[C] W: " + this.chunkSource.gatherStats() + " E: " + this.entityStorage.gatherStats();
   }

   @Override
   public void addDestroyBlockEffect(final BlockPos pos, final BlockState blockState) {
      if (!blockState.isAir() && blockState.shouldSpawnTerrainParticles()) {
         VoxelShape shape = blockState.getShape(this, pos);
         double density = 0.25;
         shape.forAllBoxes(
            (x1, y1, z1, x2, y2, z2) -> {
               double widthX = Math.min(1.0, x2 - x1);
               double widthY = Math.min(1.0, y2 - y1);
               double widthZ = Math.min(1.0, z2 - z1);
               int countX = Math.max(2, Mth.ceil(widthX / 0.25));
               int countY = Math.max(2, Mth.ceil(widthY / 0.25));
               int countZ = Math.max(2, Mth.ceil(widthZ / 0.25));

               for (int xx = 0; xx < countX; xx++) {
                  for (int yy = 0; yy < countY; yy++) {
                     for (int zz = 0; zz < countZ; zz++) {
                        double relX = (xx + 0.5) / countX;
                        double relY = (yy + 0.5) / countY;
                        double relZ = (zz + 0.5) / countZ;
                        double x = relX * widthX + x1;
                        double y = relY * widthY + y1;
                        double z = relZ * widthZ + z1;
                        this.minecraft
                           .particleEngine
                           .add(new TerrainParticle(this, pos.getX() + x, pos.getY() + y, pos.getZ() + z, relX - 0.5, relY - 0.5, relZ - 0.5, blockState, pos));
                     }
                  }
               }
            }
         );
      }
   }

   public void addBreakingBlockEffects(final BlockPos pos, final Direction direction, final boolean playSound) {
      BlockState blockState = this.getBlockState(pos);
      if (!blockState.isAir()) {
         if (playSound) {
            this.playBreakingSound(pos, blockState);
         }

         if (blockState.getRenderShape() != RenderShape.INVISIBLE && blockState.shouldSpawnTerrainParticles()) {
            this.addBreakingParticles(pos, direction, blockState);
         }
      }
   }

   private void addBreakingParticles(final BlockPos pos, final Direction direction, final BlockState blockState) {
      int x = pos.getX();
      int y = pos.getY();
      int z = pos.getZ();
      float r = 0.1F;
      AABB shape = blockState.getShape(this, pos).bounds();
      double xp = x + this.random.nextDouble() * (shape.maxX - shape.minX - 0.2F) + 0.1F + shape.minX;
      double yp = y + this.random.nextDouble() * (shape.maxY - shape.minY - 0.2F) + 0.1F + shape.minY;
      double zp = z + this.random.nextDouble() * (shape.maxZ - shape.minZ - 0.2F) + 0.1F + shape.minZ;
      if (direction == Direction.DOWN) {
         yp = y + shape.minY - 0.1F;
      }

      if (direction == Direction.UP) {
         yp = y + shape.maxY + 0.1F;
      }

      if (direction == Direction.NORTH) {
         zp = z + shape.minZ - 0.1F;
      }

      if (direction == Direction.SOUTH) {
         zp = z + shape.maxZ + 0.1F;
      }

      if (direction == Direction.WEST) {
         xp = x + shape.minX - 0.1F;
      }

      if (direction == Direction.EAST) {
         xp = x + shape.maxX + 0.1F;
      }

      this.minecraft.particleEngine.add(new TerrainParticle(this, xp, yp, zp, 0.0, 0.0, 0.0, blockState, pos).setPower(0.2F).scale(0.6F));
   }

   private void playBreakingSound(final BlockPos pos, final BlockState blockState) {
      SoundType soundType = blockState.getSoundType();
      this.minecraft
         .getSoundManager()
         .play(
            new SimpleSoundInstance(
               soundType.getHitSound(),
               SoundSource.BLOCKS,
               (soundType.getVolume() + 1.0F) / 8.0F,
               soundType.getPitch() * 0.5F,
               SoundInstance.createUnseededRandom(),
               pos
            )
         );
   }

   public void setServerSimulationDistance(final int serverSimulationDistance) {
      this.serverSimulationDistance = serverSimulationDistance;
   }

   public int getServerSimulationDistance() {
      return this.serverSimulationDistance;
   }

   @Override
   public FeatureFlagSet enabledFeatures() {
      return this.connection.enabledFeatures();
   }

   @Override
   public void explode(
      @Nullable final Entity source,
      @Nullable final DamageSource damageSource,
      @Nullable final ExplosionDamageCalculator damageCalculator,
      final double x,
      final double y,
      final double z,
      final float r,
      final boolean fire,
      final Level$ExplosionInteraction interactionType,
      final ParticleOptions smallExplosionParticles,
      final ParticleOptions largeExplosionParticles,
      final WeightedList<ExplosionParticleInfo> secondaryParticles,
      final Holder<SoundEvent> explosionSound
   ) {
   }

   @Override
   public int getSeaLevel() {
      return this.seaLevel;
   }

   @Override
   public int getClientLeafTintColor(final BlockPos pos) {
      BlockState state = this.getBlockState(pos);
      BlockTintSource tintSource = Minecraft.getInstance().getBlockColors().getTintSource(state, 0);
      return tintSource != null ? tintSource.colorInWorld(state, this, pos) : -1;
   }

   @Override
   public void registerForCleaning(final CacheSlot<ClientLevel, ?> slot) {
      this.connection.registerForCleaning(slot);
   }

   public void trackExplosionEffects(final Vec3 center, final float radius, final int blockCount, final WeightedList<ExplosionParticleInfo> blockParticles) {
      this.explosionTracker.track(center, radius, blockCount, blockParticles);
   }
}
