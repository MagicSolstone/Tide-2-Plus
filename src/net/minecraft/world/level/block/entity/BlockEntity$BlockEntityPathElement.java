package net.minecraft.world.level.block.entity;

import net.minecraft.util.ProblemReporter$PathElement;

record BlockEntity$BlockEntityPathElement(BlockEntity blockEntity) implements ProblemReporter$PathElement {
   @Override
   public String get() {
      return this.blockEntity.getNameForReporting() + "@" + this.blockEntity.getBlockPos();
   }
}
