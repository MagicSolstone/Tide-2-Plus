package net.minecraft.util.debug;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.DebugEntityNameGenerator;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.schedule.Activity;
import org.jspecify.annotations.Nullable;

public record DebugBrainDump(
   String name,
   String profession,
   int xp,
   float health,
   float maxHealth,
   String inventory,
   boolean wantsGolem,
   int angerLevel,
   List<String> activities,
   List<String> behaviors,
   List<String> memories,
   List<String> gossips,
   Set<BlockPos> pois,
   Set<BlockPos> potentialPois
) {
   public static final StreamCodec<ByteBuf, DebugBrainDump> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8,
      DebugBrainDump::name,
      ByteBufCodecs.STRING_UTF8,
      DebugBrainDump::profession,
      ByteBufCodecs.INT,
      DebugBrainDump::xp,
      ByteBufCodecs.FLOAT,
      DebugBrainDump::health,
      ByteBufCodecs.FLOAT,
      DebugBrainDump::maxHealth,
      ByteBufCodecs.STRING_UTF8,
      DebugBrainDump::inventory,
      ByteBufCodecs.BOOL,
      DebugBrainDump::wantsGolem,
      ByteBufCodecs.INT,
      DebugBrainDump::angerLevel,
      ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
      DebugBrainDump::activities,
      ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
      DebugBrainDump::behaviors,
      ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
      DebugBrainDump::memories,
      ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
      DebugBrainDump::gossips,
      BlockPos.STREAM_CODEC.apply(ByteBufCodecs.collection(HashSet::new)),
      DebugBrainDump::pois,
      BlockPos.STREAM_CODEC.apply(ByteBufCodecs.collection(HashSet::new)),
      DebugBrainDump::potentialPois,
      DebugBrainDump::new
   );

   public static DebugBrainDump takeBrainDump(final ServerLevel serverLevel, final LivingEntity entity) {
      String name = DebugEntityNameGenerator.getEntityName(entity);
      String profession;
      int xp;
      if (entity instanceof Villager villager) {
         profession = villager.getVillagerData().profession().getRegisteredName();
         xp = villager.getVillagerXp();
      } else {
         profession = "";
         xp = 0;
      }

      float health = entity.getHealth();
      float maxHealth = entity.getMaxHealth();
      Brain<?> brain = entity.getBrain();
      long gameTime = entity.level().getGameTime();
      String inventoryStr;
      if (entity instanceof InventoryCarrier inventoryCarrier) {
         Container inventory = inventoryCarrier.getInventory();
         inventoryStr = inventory.isEmpty() ? "" : inventory.toString();
      } else {
         inventoryStr = "";
      }

      boolean wantsGolem = entity instanceof Villager villager && villager.wantsToSpawnGolem(gameTime);
      int angerLevel = entity instanceof Warden warden ? warden.getClientAngerLevel() : -1;
      List<String> activities = brain.getActiveActivities().stream().map(Activity::getName).toList();
      List<String> behaviors = brain.getRunningBehaviors().stream().map(BehaviorControl::debugString).toList();
      List<String> memories = getMemoryDescriptions(serverLevel, entity, gameTime);
      Set<BlockPos> pois = getKnownBlockPositions(brain, MemoryModuleType.JOB_SITE, MemoryModuleType.HOME, MemoryModuleType.MEETING_POINT);
      Set<BlockPos> potentialPois = getKnownBlockPositions(brain, MemoryModuleType.POTENTIAL_JOB_SITE);
      List<String> gossips = entity instanceof Villager villagerx ? getVillagerGossips(villagerx) : List.of();
      return new DebugBrainDump(
         name, profession, xp, health, maxHealth, inventoryStr, wantsGolem, angerLevel, activities, behaviors, memories, gossips, pois, potentialPois
      );
   }

   @SafeVarargs
   private static Set<BlockPos> getKnownBlockPositions(final Brain<?> brain, final MemoryModuleType<GlobalPos>... memories) {
      return Stream.of(memories).filter(brain::hasMemoryValue).map(brain::getMemory).flatMap(Optional::stream).map(GlobalPos::pos).collect(Collectors.toSet());
   }

   private static List<String> getVillagerGossips(final Villager villager) {
      List<String> gossips = new ArrayList<>();
      villager.getGossips().getGossipEntries().forEach((uuid, entries) -> {
         String gossipeeName = DebugEntityNameGenerator.getEntityName(uuid);
         entries.forEach((gossipType, value) -> gossips.add(gossipeeName + ": " + gossipType + ": " + value));
      });
      return gossips;
   }

   private static List<String> getMemoryDescriptions(final ServerLevel level, final LivingEntity body, final long timestamp) {
      List<String> result = new ArrayList<>();
      body.getBrain().forEach(new DebugBrainDump$1(timestamp, level, result));
      Collections.sort(result);
      return result;
   }

   private static String getMemoryDescription(
      final ServerLevel level, final long timestamp, final MemoryModuleType<?> memoryType, final Optional<?> maybeValue, final OptionalLong ttl
   ) {
      String description;
      if (maybeValue.isPresent()) {
         Object value = maybeValue.get();
         if (memoryType == MemoryModuleType.HEARD_BELL_TIME) {
            long timeSince = timestamp - (Long)value;
            description = timeSince + " ticks ago";
         } else if (ttl.isPresent()) {
            description = getShortDescription(level, value) + " (ttl: " + ttl.getAsLong() + ")";
         } else {
            description = getShortDescription(level, value);
         }
      } else {
         description = "-";
      }

      return BuiltInRegistries.MEMORY_MODULE_TYPE.getKey(memoryType).getPath() + ": " + description;
   }

   private static String getShortDescription(final ServerLevel level, @Nullable final Object obj) {
      return switch (obj) {
         case null -> "-";
         case UUID uuid -> getShortDescription(level, level.getEntity(uuid));
         case Entity entity -> DebugEntityNameGenerator.getEntityName(entity);
         case WalkTarget walkTarget -> getShortDescription(level, walkTarget.getTarget());
         case EntityTracker entityTracker -> getShortDescription(level, entityTracker.getEntity());
         case GlobalPos globalPos -> getShortDescription(level, globalPos.pos());
         case BlockPosTracker tracker -> getShortDescription(level, tracker.currentBlockPosition());
         case DamageSource damageSource -> {
            Entity entity = damageSource.getEntity();
            yield entity == null ? obj.toString() : getShortDescription(level, entity);
         }
         case NearestVisibleLivingEntities visibleEntities -> getShortDescription(level, visibleEntities.nearbyEntities());
         case Collection<?> collection -> "["
            + (String)collection.stream().map(element -> getShortDescription(level, element)).collect(Collectors.joining(", "))
            + "]";
         default -> obj.toString();
      };
   }

   public boolean hasPoi(final BlockPos poiPos) {
      return this.pois.contains(poiPos);
   }

   public boolean hasPotentialPoi(final BlockPos poiPos) {
      return this.potentialPois.contains(poiPos);
   }
}
