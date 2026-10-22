package net.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface Font$PreparedText {
   void visit(Font$GlyphVisitor visitor);

   @Nullable
   ScreenRectangle bounds();
}
