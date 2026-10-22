package net.minecraft.client.gui.screens.reporting;

import com.mojang.authlib.GameProfile;
import java.util.Objects;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.PlayerSkin;

@Environment(EnvType.CLIENT)
public class ChatSelectionScreen$ChatSelectionList$MessageHeadingEntry extends ChatSelectionScreen$ChatSelectionList$Entry {
   private static final int FACE_SIZE = 12;
   private static final int PADDING = 4;
   private final Component heading;
   private final Supplier<PlayerSkin> skin;
   private final boolean canReport;

   public ChatSelectionScreen$ChatSelectionList$MessageHeadingEntry(
      final ChatSelectionScreen$ChatSelectionList this$1, final GameProfile profile, final Component heading, final boolean canReport
   ) {
      Objects.requireNonNull(this$1);
      this.this$1 = this$1;
      super();
      this.heading = heading;
      this.canReport = canReport;
      this.skin = ChatSelectionScreen$ChatSelectionList.access$800(this$1).getSkinManager().createLookup(profile, true);
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      int faceX = this.getContentX() - 12 + 4;
      int faceY = this.getContentY() + (this.getContentHeight() - 12) / 2;
      PlayerFaceExtractor.extractRenderState(graphics, this.skin.get(), faceX, faceY, 12);
      int textY = this.getContentY() + 1 + (this.getContentHeight() - 9) / 2;
      graphics.text(ChatSelectionScreen.access$1000(this.this$1.this$0), this.heading, faceX + 12 + 4, textY, this.canReport ? -1 : -1593835521);
   }
}
