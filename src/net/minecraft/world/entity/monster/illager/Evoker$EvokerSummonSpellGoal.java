package net.minecraft.world.entity.monster.illager;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent$Context;
import net.minecraft.world.scores.PlayerTeam;

class Evoker$EvokerSummonSpellGoal extends SpellcasterIllager$SpellcasterUseSpellGoal {
   private final TargetingConditions vexCountTargeting;

   private Evoker$EvokerSummonSpellGoal(final Evoker this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
      this.vexCountTargeting = TargetingConditions.forNonCombat().range(16.0).ignoreLineOfSight().ignoreInvisibilityTesting();
   }

   @Override
   public boolean canUse() {
      if (!super.canUse()) {
         return false;
      } else {
         int vexes = getServerLevel(this.this$0.level())
            .getNearbyEntities(Vex.class, this.vexCountTargeting, this.this$0, this.this$0.getBoundingBox().inflate(16.0))
            .size();
         return Evoker.access$000(this.this$0).nextInt(8) + 1 > vexes;
      }
   }

   @Override
   protected int getCastingTime() {
      return 100;
   }

   @Override
   protected int getCastingInterval() {
      return 340;
   }

   @Override
   protected void performSpellCasting() {
      ServerLevel serverLevel = (ServerLevel)this.this$0.level();
      PlayerTeam evokerTeam = this.this$0.getTeam();

      for (int i = 0; i < 3; i++) {
         BlockPos pos = this.this$0.blockPosition().offset(-2 + Evoker.access$100(this.this$0).nextInt(5), 1, -2 + Evoker.access$200(this.this$0).nextInt(5));
         Vex vex = EntityTypes.VEX.create(this.this$0.level(), EntitySpawnReason.MOB_SUMMONED);
         if (vex != null) {
            vex.snapTo(pos, 0.0F, 0.0F);
            vex.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(pos), EntitySpawnReason.MOB_SUMMONED, null);
            vex.setOwner(this.this$0);
            vex.setBoundOrigin(pos);
            vex.setLimitedLife(20 * (30 + Evoker.access$300(this.this$0).nextInt(90)));
            if (evokerTeam != null) {
               serverLevel.getScoreboard().addPlayerToTeam(vex.getScoreboardName(), evokerTeam);
            }

            serverLevel.addFreshEntityWithPassengers(vex);
            serverLevel.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent$Context.of(this.this$0));
         }
      }
   }

   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.EVOKER_PREPARE_SUMMON;
   }

   @Override
   protected SpellcasterIllager$IllagerSpell getSpell() {
      return SpellcasterIllager$IllagerSpell.SUMMON_VEX;
   }
}
