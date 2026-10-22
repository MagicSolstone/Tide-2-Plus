package net.minecraft.world.entity.animal.armadillo;

enum Armadillo$ArmadilloState$2 {
   private Armadillo$ArmadilloState$2(final String name, final boolean isThreatened, final int animationDuration, final int id) {
   }

   @Override
   public boolean shouldHideInShell(final long ticksInState) {
      return ticksInState > 5L;
   }
}
