package net.minecraft.commands;

import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;

class CommandSourceStack$NamesProvider$1 implements CommandSourceStack$NamesProvider {
   @Override
   public Component displayName(@Nullable final Entity entity) {
      return entity != null ? entity.getDisplayName() : CommonComponents.EMPTY;
   }

   @Override
   public String textName(@Nullable final Entity entity) {
      return entity != null ? entity.getPlainTextName() : "";
   }
}
