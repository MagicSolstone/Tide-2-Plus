package net.minecraft.server.commands;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityAnchorArgument$Anchor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public record LookAt$LookAtEntity(Entity entity, EntityAnchorArgument$Anchor anchor) implements LookAt {
   @Override
   public void perform(final CommandSourceStack source, final Entity target) {
      if (target instanceof ServerPlayer targetPlayer) {
         targetPlayer.lookAt(source.getAnchor(), this.entity, this.anchor);
      } else {
         target.lookAt(source.getAnchor(), this.anchor.apply(this.entity));
      }
   }
}
