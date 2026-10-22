package net.minecraft.client.data.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.FrontAndTop;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.minecraft.world.level.block.entity.vault.VaultState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.RailShape;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class BlockModelGenerators$1 {
   static {
      try {
         $SwitchMap$net$minecraft$core$FrontAndTop[FrontAndTop.DOWN_NORTH.ordinal()] = 1;
      } catch (NoSuchFieldError var30) {
      }

      try {
         $SwitchMap$net$minecraft$core$FrontAndTop[FrontAndTop.DOWN_SOUTH.ordinal()] = 2;
      } catch (NoSuchFieldError var29) {
      }

      try {
         $SwitchMap$net$minecraft$core$FrontAndTop[FrontAndTop.DOWN_WEST.ordinal()] = 3;
      } catch (NoSuchFieldError var28) {
      }

      try {
         $SwitchMap$net$minecraft$core$FrontAndTop[FrontAndTop.DOWN_EAST.ordinal()] = 4;
      } catch (NoSuchFieldError var27) {
      }

      try {
         $SwitchMap$net$minecraft$core$FrontAndTop[FrontAndTop.UP_NORTH.ordinal()] = 5;
      } catch (NoSuchFieldError var26) {
      }

      try {
         $SwitchMap$net$minecraft$core$FrontAndTop[FrontAndTop.UP_SOUTH.ordinal()] = 6;
      } catch (NoSuchFieldError var25) {
      }

      try {
         $SwitchMap$net$minecraft$core$FrontAndTop[FrontAndTop.UP_WEST.ordinal()] = 7;
      } catch (NoSuchFieldError var24) {
      }

      try {
         $SwitchMap$net$minecraft$core$FrontAndTop[FrontAndTop.UP_EAST.ordinal()] = 8;
      } catch (NoSuchFieldError var23) {
      }

      try {
         $SwitchMap$net$minecraft$core$FrontAndTop[FrontAndTop.NORTH_UP.ordinal()] = 9;
      } catch (NoSuchFieldError var22) {
      }

      try {
         $SwitchMap$net$minecraft$core$FrontAndTop[FrontAndTop.SOUTH_UP.ordinal()] = 10;
      } catch (NoSuchFieldError var21) {
      }

      try {
         $SwitchMap$net$minecraft$core$FrontAndTop[FrontAndTop.WEST_UP.ordinal()] = 11;
      } catch (NoSuchFieldError var20) {
      }

      try {
         $SwitchMap$net$minecraft$core$FrontAndTop[FrontAndTop.EAST_UP.ordinal()] = 12;
      } catch (NoSuchFieldError var19) {
      }

      $SwitchMap$net$minecraft$world$level$block$entity$vault$VaultState = new int[VaultState.values().length];

      try {
         $SwitchMap$net$minecraft$world$level$block$entity$vault$VaultState[VaultState.INACTIVE.ordinal()] = 1;
      } catch (NoSuchFieldError var18) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$entity$vault$VaultState[VaultState.ACTIVE.ordinal()] = 2;
      } catch (NoSuchFieldError var17) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$entity$vault$VaultState[VaultState.UNLOCKING.ordinal()] = 3;
      } catch (NoSuchFieldError var16) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$entity$vault$VaultState[VaultState.EJECTING.ordinal()] = 4;
      } catch (NoSuchFieldError var15) {
      }

      $SwitchMap$net$minecraft$world$level$block$entity$trialspawner$TrialSpawnerState = new int[TrialSpawnerState.values().length];

      try {
         $SwitchMap$net$minecraft$world$level$block$entity$trialspawner$TrialSpawnerState[TrialSpawnerState.INACTIVE.ordinal()] = 1;
      } catch (NoSuchFieldError var14) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$entity$trialspawner$TrialSpawnerState[TrialSpawnerState.COOLDOWN.ordinal()] = 2;
      } catch (NoSuchFieldError var13) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$entity$trialspawner$TrialSpawnerState[TrialSpawnerState.WAITING_FOR_PLAYERS.ordinal()] = 3;
      } catch (NoSuchFieldError var12) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$entity$trialspawner$TrialSpawnerState[TrialSpawnerState.ACTIVE.ordinal()] = 4;
      } catch (NoSuchFieldError var11) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$entity$trialspawner$TrialSpawnerState[TrialSpawnerState.WAITING_FOR_REWARD_EJECTION.ordinal()] = 5;
      } catch (NoSuchFieldError var10) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$entity$trialspawner$TrialSpawnerState[TrialSpawnerState.EJECTING_REWARD.ordinal()] = 6;
      } catch (NoSuchFieldError var9) {
      }

      $SwitchMap$net$minecraft$world$level$block$state$properties$RailShape = new int[RailShape.values().length];

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$RailShape[RailShape.NORTH_SOUTH.ordinal()] = 1;
      } catch (NoSuchFieldError var8) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$RailShape[RailShape.EAST_WEST.ordinal()] = 2;
      } catch (NoSuchFieldError var7) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$RailShape[RailShape.ASCENDING_EAST.ordinal()] = 3;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$RailShape[RailShape.ASCENDING_WEST.ordinal()] = 4;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$RailShape[RailShape.ASCENDING_NORTH.ordinal()] = 5;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$RailShape[RailShape.ASCENDING_SOUTH.ordinal()] = 6;
      } catch (NoSuchFieldError var3) {
      }

      $SwitchMap$net$minecraft$world$level$block$state$properties$DoubleBlockHalf = new int[DoubleBlockHalf.values().length];

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$DoubleBlockHalf[DoubleBlockHalf.UPPER.ordinal()] = 1;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$net$minecraft$world$level$block$state$properties$DoubleBlockHalf[DoubleBlockHalf.LOWER.ordinal()] = 2;
      } catch (NoSuchFieldError var1) {
      }
   }
}
