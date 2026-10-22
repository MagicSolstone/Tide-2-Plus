package net.minecraft.commands;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;

class CommandSourceStack$NamesProvider$2 implements CommandSourceStack$NamesProvider {
   CommandSourceStack$NamesProvider$2(final Component val$value) {
      this.val$value = val$valuex;
   }

   @Override
   public Component displayName(@Nullable final Entity entity) {
      return this.val$value;
   }

   @Override
   public String textName(@Nullable final Entity entity) {
      return this.val$value.getString();
   }
}
