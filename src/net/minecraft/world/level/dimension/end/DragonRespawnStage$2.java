package net.minecraft.world.level.dimension.end;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;

enum DragonRespawnStage$2 {
   private DragonRespawnStage$2(final String name) {
   }

   @Override
   public void tick(final ServerLevel level, final EnderDragonFight fight, final List<EndCrystal> crystals, final int time) {
      if (time < 100) {
         if (time == 0 || time == 50 || time == 51 || time == 52 || time >= 95) {
            level.levelEvent(3001, new BlockPos(0, 128, 0), 0);
         }
      } else {
         fight.setRespawnStage(SUMMONING_PILLARS);
      }
   }
}
