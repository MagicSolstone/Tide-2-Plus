package net.minecraft.world.level.dimension.end;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.Level$ExplosionInteraction;
import net.minecraft.world.level.levelgen.feature.EndSpikeFeature;
import net.minecraft.world.level.levelgen.feature.EndSpikeFeature$EndSpike;

enum DragonRespawnStage$3 {
   private DragonRespawnStage$3(final String name) {
   }

   @Override
   public void tick(final ServerLevel level, final EnderDragonFight fight, final List<EndCrystal> crystals, final int time) {
      int interval = 40;
      boolean startOfBeam = time % 40 == 0;
      boolean endOfBeam = time % 40 == 39;
      if (startOfBeam || endOfBeam) {
         List<EndSpikeFeature$EndSpike> spikes = EndSpikeFeature.getSpikesForLevel(level);
         int index = time / 40;
         if (index < spikes.size()) {
            EndSpikeFeature$EndSpike spike = spikes.get(index);
            if (startOfBeam) {
               for (EndCrystal respawnCrystal : crystals) {
                  respawnCrystal.setBeamTarget(new BlockPos(spike.getCenterX(), spike.getHeight() + 1, spike.getCenterZ()));
               }
            } else {
               int radius = 10;

               for (BlockPos pos : BlockPos.betweenClosed(
                  new BlockPos(spike.getCenterX() - 10, spike.getHeight() - 10, spike.getCenterZ() - 10),
                  new BlockPos(spike.getCenterX() + 10, spike.getHeight() + 10, spike.getCenterZ() + 10)
               )) {
                  level.removeBlock(pos, false);
               }

               level.explode(null, spike.getCenterX() + 0.5F, spike.getHeight(), spike.getCenterZ() + 0.5F, 5.0F, Level$ExplosionInteraction.BLOCK);
               EndSpikeFeature feature = new EndSpikeFeature(List.of(spike), true, Optional.of(new BlockPos(0, 128, 0)));
               feature.place(level, level.getChunkSource().getGenerator(), RandomSource.create(), new BlockPos(spike.getCenterX(), 45, spike.getCenterZ()));
            }
         } else if (startOfBeam) {
            fight.setRespawnStage(SUMMONING_DRAGON);
         }
      }
   }
}
