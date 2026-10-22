package net.minecraft.world.effect;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.cubemob.Slime;

@FunctionalInterface
public interface OozingMobEffect$NearbySlimes {
   int count(final int maxResults);

   private static OozingMobEffect$NearbySlimes closeTo(final LivingEntity mob) {
      return maxResults -> {
         List<Slime> slimesNearby = new ArrayList<>();
         mob.level().getEntities(EntityTypes.SLIME, mob.getBoundingBox().inflate(2.0), slime -> slime != mob, slimesNearby, maxResults);
         return slimesNearby.size();
      };
   }
}
