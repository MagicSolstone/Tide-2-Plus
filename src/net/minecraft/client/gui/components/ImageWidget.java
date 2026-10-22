package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class ImageWidget extends AbstractWidget {
   private ImageWidget(final int x, final int y, final int width, final int height) {
      super(x, y, width, height, CommonComponents.EMPTY);
   }

   public static ImageWidget texture(final int width, final int height, final Identifier texture, final int textureWidth, final int textureHeight) {
      return new ImageWidget$Texture(0, 0, width, height, texture, textureWidth, textureHeight);
   }

   public static ImageWidget sprite(final int width, final int height, final Identifier sprite) {
      return new ImageWidget$Sprite(0, 0, width, height, sprite);
   }

   @Override
   protected void updateWidgetNarration(final NarrationElementOutput output) {
   }

   @Override
   public void playDownSound(final SoundManager soundManager) {
   }

   @Override
   public boolean isActive() {
      return false;
   }

   public abstract void updateResource(Identifier identifier);

   @Nullable
   @Override
   public ComponentPath nextFocusPath(final FocusNavigationEvent navigationEvent) {
      return null;
   }
}
