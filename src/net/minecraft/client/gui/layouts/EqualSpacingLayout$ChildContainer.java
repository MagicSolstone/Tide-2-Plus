package net.minecraft.client.gui.layouts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class EqualSpacingLayout$ChildContainer extends AbstractLayout$AbstractChildWrapper {
   protected EqualSpacingLayout$ChildContainer(final LayoutElement child, final LayoutSettings layoutSettings) {
      super(child, layoutSettings);
   }
}
