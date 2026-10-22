package net.minecraft.world.level.block.entity.vault;

enum VaultState$LightLevel {
   HALF_LIT(6),
   LIT(12);

   private final int value;

   private VaultState$LightLevel(final int value) {
      this.value = value;
   }
}
