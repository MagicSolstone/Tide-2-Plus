package net.minecraft.world.level.dimension.end;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.Level$ExplosionInteraction;

enum DragonRespawnStage$4 {
   private DragonRespawnStage$4(final String name) {
   }

   @Override
   public void tick(final ServerLevel level, final EnderDragonFight fight, final List<EndCrystal> crystals, final int time) {
      if (time >= 100) {
         fight.setRespawnStage(END);
         fight.resetSpikeCrystals();

         for (EndCrystal crystal : crystals) {
            crystal.setBeamTarget(null);
            level.explode(crystal, crystal.getX(), crystal.getY(), crystal.getZ(), 6.0F, Level$ExplosionInteraction.NONE);
            crystal.discard();
         }
      } else if (time >= 80) {
         level.levelEvent(3001, new BlockPos(0, 128, 0), 0);
      } else if (time == 0) {
         for (EndCrystal crystal : crystals) {
            crystal.setBeamTarget(new BlockPos(0, 128, 0));
         }
      } else if (time < 5) {
         level.levelEvent(3001, new BlockPos(0, 128, 0), 0);
      }
   }
}
