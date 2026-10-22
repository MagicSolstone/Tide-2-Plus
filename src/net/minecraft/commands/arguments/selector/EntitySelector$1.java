package net.minecraft.commands.arguments.selector;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTypeTest;

class EntitySelector$1 implements EntityTypeTest<Entity, Entity> {
   public Entity tryCast(final Entity entity) {
      return entity;
   }

   @Override
   public Class<? extends Entity> getBaseClass() {
      return Entity.class;
   }
}
