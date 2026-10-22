package net.minecraft.world.attribute.modifier;

import java.util.Map;
import java.util.Set;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings$Builder;
import net.minecraft.world.level.biome.MobSpawnSettings$MobSpawnCost;
import net.minecraft.world.level.biome.MobSpawnSettings$SpawnerData;

public record MobSpawnSettingsModifier$Overlay() implements MobSpawnSettingsModifier {
   private static final MobSpawnSettingsModifier$Overlay INSTANCE = new MobSpawnSettingsModifier$Overlay();
   private static final MobCategory[] ALL_CATEGORIES = MobCategory.values();

   public MobSpawnSettings apply(final MobSpawnSettings first, final MobSpawnSettings second) {
      Set<MobCategory> firstCategories = first.definedCategories();
      Set<MobCategory> secondCategories = second.definedCategories();
      Map<EntityType<?>, MobSpawnSettings$MobSpawnCost> firstSpawnCosts = first.allSpawnCosts();
      Map<EntityType<?>, MobSpawnSettings$MobSpawnCost> secondSpawnCosts = second.allSpawnCosts();
      if (firstCategories.isEmpty() && firstSpawnCosts.isEmpty()) {
         return second;
      } else if (secondCategories.isEmpty() && secondSpawnCosts.isEmpty()) {
         return first;
      } else if (secondCategories.containsAll(firstCategories) && secondSpawnCosts.keySet().containsAll(firstSpawnCosts.keySet())) {
         return second;
      } else {
         MobSpawnSettings$Builder builder = new MobSpawnSettings$Builder();

         for (MobCategory category : ALL_CATEGORIES) {
            WeightedList<MobSpawnSettings$SpawnerData> secondSpawns = second.getMobsInCategory(category);
            if (secondSpawns != null) {
               builder.addAllSpawns(category, secondSpawns);
            } else {
               WeightedList<MobSpawnSettings$SpawnerData> firstSpawns = first.getMobsInCategory(category);
               if (firstSpawns != null) {
                  builder.addAllSpawns(category, firstSpawns);
               }
            }
         }

         return builder.addAllCosts(firstSpawnCosts).addAllCosts(secondSpawnCosts).build();
      }
   }
}
