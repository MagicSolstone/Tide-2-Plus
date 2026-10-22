package net.minecraft.client.gui.components;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.ActiveTextCollector$Parameters;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.client.multiplayer.chat.GuiMessageTag$Icon;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix3x2f;

@Environment(EnvType.CLIENT)
class ChatComponent$ClickableTextOnlyGraphicsAccess implements ChatComponent$ChatGraphicsAccess {
   private final ActiveTextCollector output;

   public ChatComponent$ClickableTextOnlyGraphicsAccess(final ActiveTextCollector output) {
      this.output = output;
   }

   @Override
   public void updatePose(final Consumer<Matrix3x2f> updater) {
      ActiveTextCollector$Parameters defaultParameters = this.output.defaultParameters();
      Matrix3x2f newPose = new Matrix3x2f(defaultParameters.pose());
      updater.accept(newPose);
      this.output.defaultParameters(defaultParameters.withPose(newPose));
   }

   @Override
   public void fill(final int x0, final int y0, final int x1, final int y1, final int color) {
   }

   @Override
   public boolean handleMessage(final int textTop, final float opacity, final FormattedCharSequence message) {
      this.output.accept(TextAlignment.LEFT, 0, textTop, message);
      return false;
   }

   @Override
   public void handleTag(final int x0, final int y0, final int x1, final int y1, final float opacity, final GuiMessageTag tag) {
   }

   @Override
   public void handleTagIcon(final int left, final int bottom, final boolean forceVisible, final GuiMessageTag tag, final GuiMessageTag$Icon icon) {
   }
}
