package net.minecraft.world.level.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.ProblemReporter$ScopedCollector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityProcessor;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueOutput;
import org.jspecify.annotations.Nullable;

public record BeehiveBlockEntity$Occupant(TypedEntityData<EntityType<?>> entityData, int ticksInHive, int minTicksInHive) {
   public static final Codec<BeehiveBlockEntity$Occupant> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            TypedEntityData.codec(EntityType.CODEC).fieldOf("entity_data").forGetter(BeehiveBlockEntity$Occupant::entityData),
            Codec.INT.fieldOf("ticks_in_hive").forGetter(BeehiveBlockEntity$Occupant::ticksInHive),
            Codec.INT.fieldOf("min_ticks_in_hive").forGetter(BeehiveBlockEntity$Occupant::minTicksInHive)
         )
         .apply(i, BeehiveBlockEntity$Occupant::new)
   );
   public static final Codec<List<BeehiveBlockEntity$Occupant>> LIST_CODEC = CODEC.listOf();
   public static final StreamCodec<RegistryFriendlyByteBuf, BeehiveBlockEntity$Occupant> STREAM_CODEC = StreamCodec.composite(
      TypedEntityData.streamCodec(EntityType.STREAM_CODEC),
      BeehiveBlockEntity$Occupant::entityData,
      ByteBufCodecs.VAR_INT,
      BeehiveBlockEntity$Occupant::ticksInHive,
      ByteBufCodecs.VAR_INT,
      BeehiveBlockEntity$Occupant::minTicksInHive,
      BeehiveBlockEntity$Occupant::new
   );

   public static BeehiveBlockEntity$Occupant of(final Entity entity) {
      BeehiveBlockEntity$Occupant var5;
      try (ProblemReporter$ScopedCollector reporter = new ProblemReporter$ScopedCollector(entity.problemPath(), BeehiveBlockEntity.LOGGER)) {
         TagValueOutput output = TagValueOutput.createWithContext(reporter, entity.registryAccess());
         entity.save(output);
         BeehiveBlockEntity.IGNORED_BEE_TAGS.forEach(output::discard);
         CompoundTag entityTag = output.buildResult();
         boolean hasNectar = entityTag.getBooleanOr("HasNectar", false);
         var5 = new BeehiveBlockEntity$Occupant(TypedEntityData.of(entity.getType(), entityTag), 0, hasNectar ? 2400 : 600);
      }

      return var5;
   }

   public static BeehiveBlockEntity$Occupant create(final int ticksInHive) {
      return new BeehiveBlockEntity$Occupant(TypedEntityData.of(EntityTypes.BEE, new CompoundTag()), ticksInHive, 600);
   }

   @Nullable
   public Entity createEntity(final Level level, final BlockPos hivePos) {
      CompoundTag entityTag = this.entityData.copyTagWithoutId();
      BeehiveBlockEntity.IGNORED_BEE_TAGS.forEach(entityTag::remove);
      Entity entity = EntityType.loadEntityRecursive(this.entityData.type(), entityTag, level, EntitySpawnReason.LOAD, EntityProcessor.NOP);
      if (entity != null && entity.is(EntityTypeTags.BEEHIVE_INHABITORS)) {
         entity.setNoGravity(true);
         if (entity instanceof Bee bee) {
            bee.setHivePos(hivePos);
            setBeeReleaseData(this.ticksInHive, bee);
         }

         return entity;
      } else {
         return null;
      }
   }

   private static void setBeeReleaseData(final int ticksInHive, final Bee bee) {
      updateBeeAge(ticksInHive, bee);
      bee.setInLoveTime(Math.max(0, bee.getInLoveTime() - ticksInHive));
   }

   private static void updateBeeAge(final int ticksInHive, final Bee bee) {
      if (!bee.isAgeLocked()) {
         int age = bee.getAge();
         if (age < 0) {
            bee.setAge(Math.min(0, age + ticksInHive));
         } else if (age > 0) {
            bee.setAge(Math.max(0, age - ticksInHive));
         }
      }
   }
}
