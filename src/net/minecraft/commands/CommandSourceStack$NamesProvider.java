package net.minecraft.commands;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;

public interface CommandSourceStack$NamesProvider {
   CommandSourceStack$NamesProvider FOR_ENTITY = new CommandSourceStack$NamesProvider$1();

   Component displayName(@Nullable Entity entity);

   String textName(@Nullable Entity entity);

   static CommandSourceStack$NamesProvider constant(final Component value) {
      return new CommandSourceStack$NamesProvider$2(value);
   }
}
