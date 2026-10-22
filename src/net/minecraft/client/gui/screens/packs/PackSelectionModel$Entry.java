package net.minecraft.client.gui.screens.packs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;

@Environment(EnvType.CLIENT)
public interface PackSelectionModel$Entry {
   Identifier getIconTexture();

   PackCompatibility getCompatibility();

   String getId();

   Component getTitle();

   Component getDescription();

   PackSource getPackSource();

   default Component getExtendedDescription() {
      return this.getPackSource().decorate(this.getDescription());
   }

   boolean isFixedPosition();

   boolean isRequired();

   void select();

   void unselect();

   void moveUp();

   void moveDown();

   boolean isSelected();

   default boolean canSelect() {
      return !this.isSelected();
   }

   default boolean canUnselect() {
      return this.isSelected() && !this.isRequired();
   }

   boolean canMoveUp();

   boolean canMoveDown();
}
