package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.narration.NarrationSupplier;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public abstract class ObjectSelectionList$Entry<E extends ObjectSelectionList$Entry<E>> extends AbstractSelectionList$Entry<E> implements NarrationSupplier {
   public abstract Component getNarration();

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      return true;
   }

   @Override
   public void updateNarration(final NarrationElementOutput output) {
      output.add(NarratedElementType.TITLE, this.getNarration());
   }
}
