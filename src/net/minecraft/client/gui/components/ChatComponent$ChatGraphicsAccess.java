package net.minecraft.client.gui.components;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.client.multiplayer.chat.GuiMessageTag$Icon;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix3x2f;

@Environment(EnvType.CLIENT)
public interface ChatComponent$ChatGraphicsAccess {
   void updatePose(final Consumer<Matrix3x2f> updater);

   void fill(int x0, int y0, int x1, int y1, int color);

   boolean handleMessage(int textTop, float opacity, FormattedCharSequence message);

   void handleTag(int x0, int y0, int x1, int y1, float opacity, GuiMessageTag tag);

   void handleTagIcon(int left, int bottom, boolean forceVisible, GuiMessageTag tag, GuiMessageTag$Icon icon);
}
