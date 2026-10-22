package net.minecraft.world.level.dimension.end;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;

enum DragonRespawnStage$1 {
   private DragonRespawnStage$1(final String name) {
   }

   @Override
   public void tick(final ServerLevel level, final EnderDragonFight fight, final List<EndCrystal> crystals, final int time) {
      BlockPos beamPos = new BlockPos(0, 128, 0);

      for (EndCrystal respawnCrystal : crystals) {
         respawnCrystal.setBeamTarget(beamPos);
      }

      fight.setRespawnStage(PREPARING_TO_SUMMON_PILLARS);
   }
}
