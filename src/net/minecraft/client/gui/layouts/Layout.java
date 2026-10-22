package net.minecraft.client.gui.layouts;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;

@Environment(EnvType.CLIENT)
public interface Layout extends LayoutElement {
   void visitChildren(Consumer<LayoutElement> layoutElementVisitor);

   @Override
   default void visitWidgets(final Consumer<AbstractWidget> widgetVisitor) {
      this.visitChildren(child -> child.visitWidgets(widgetVisitor));
   }

   default void arrangeElements() {
      this.visitChildren(child -> {
         if (child instanceof Layout layout) {
            layout.arrangeElements();
         }
      });
   }

   void removeChildren();
}
