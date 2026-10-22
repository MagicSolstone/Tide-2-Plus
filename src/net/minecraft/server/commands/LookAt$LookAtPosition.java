package net.minecraft.server.commands;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public record LookAt$LookAtPosition(Vec3 position) implements LookAt {
   @Override
   public void perform(final CommandSourceStack source, final Entity target) {
      target.lookAt(source.getAnchor(), this.position);
   }
}
