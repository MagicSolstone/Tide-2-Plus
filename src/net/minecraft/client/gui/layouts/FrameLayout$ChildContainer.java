package net.minecraft.client.gui.layouts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class FrameLayout$ChildContainer extends AbstractLayout$AbstractChildWrapper {
   protected FrameLayout$ChildContainer(final LayoutElement child, final LayoutSettings layoutSettings) {
      super(child, layoutSettings);
   }
}
