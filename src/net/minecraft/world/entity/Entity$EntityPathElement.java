package net.minecraft.world.entity;

import net.minecraft.util.ProblemReporter$PathElement;

record Entity$EntityPathElement(Entity entity) implements ProblemReporter$PathElement {
   @Override
   public String get() {
      return this.entity.toString();
   }
}
