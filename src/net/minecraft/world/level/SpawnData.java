package net.minecraft.world.level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EquipmentTable;

public record SpawnData(CompoundTag entityToSpawn, Optional<SpawnData$CustomSpawnRules> customSpawnRules, Optional<EquipmentTable> equipment) {
   public static final String ENTITY_TAG = "entity";
   public static final Codec<SpawnData> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            CompoundTag.CODEC.fieldOf("entity").forGetter(s -> s.entityToSpawn),
            SpawnData$CustomSpawnRules.CODEC.optionalFieldOf("custom_spawn_rules").forGetter(o -> o.customSpawnRules),
            EquipmentTable.CODEC.optionalFieldOf("equipment").forGetter(o -> o.equipment)
         )
         .apply(i, SpawnData::new)
   );
   public static final Codec<WeightedList<SpawnData>> LIST_CODEC = WeightedList.codec(CODEC);

   public SpawnData() {
      this(new CompoundTag(), Optional.empty(), Optional.empty());
   }

   public SpawnData(CompoundTag entityToSpawn, Optional<SpawnData$CustomSpawnRules> customSpawnRules, Optional<EquipmentTable> equipment) {
      Optional<Identifier> id = entityToSpawn.read("id", Identifier.CODEC);
      if (id.isPresent()) {
         entityToSpawn.store("id", Identifier.CODEC, id.get());
      } else {
         entityToSpawn.remove("id");
      }

      this.entityToSpawn = entityToSpawn;
      this.customSpawnRules = customSpawnRules;
      this.equipment = equipment;
   }

   public CompoundTag getEntityToSpawn() {
      return this.entityToSpawn;
   }

   public Optional<SpawnData$CustomSpawnRules> getCustomSpawnRules() {
      return this.customSpawnRules;
   }

   public Optional<EquipmentTable> getEquipment() {
      return this.equipment;
   }
}
