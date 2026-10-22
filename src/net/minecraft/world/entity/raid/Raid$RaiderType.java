package net.minecraft.world.entity.raid;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;

enum Raid$RaiderType {
   VINDICATOR(EntityTypes.VINDICATOR, new int[]{0, 0, 2, 0, 1, 4, 2, 5}),
   EVOKER(EntityTypes.EVOKER, new int[]{0, 0, 0, 0, 0, 1, 1, 2}),
   PILLAGER(EntityTypes.PILLAGER, new int[]{0, 4, 3, 3, 4, 4, 4, 2}),
   WITCH(EntityTypes.WITCH, new int[]{0, 0, 0, 0, 3, 0, 0, 1}),
   RAVAGER(EntityTypes.RAVAGER, new int[]{0, 0, 0, 1, 0, 1, 0, 2});

   private static final Raid$RaiderType[] VALUES = values();
   private final EntityType<? extends Raider> entityType;
   private final int[] spawnsPerWaveBeforeBonus;

   private Raid$RaiderType(final EntityType<? extends Raider> entityType, final int[] spawnsPerWaveBeforeBonus) {
      this.entityType = entityType;
      this.spawnsPerWaveBeforeBonus = spawnsPerWaveBeforeBonus;
   }
}
