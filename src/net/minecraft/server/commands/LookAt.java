package net.minecraft.server.commands;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface LookAt {
   void perform(CommandSourceStack source, Entity target);
}
