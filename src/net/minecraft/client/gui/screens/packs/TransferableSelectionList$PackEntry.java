package net.minecraft.client.gui.screens.packs;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.SelectableEntry;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.server.packs.repository.PackCompatibility;

@Environment(EnvType.CLIENT)
public class TransferableSelectionList$PackEntry extends TransferableSelectionList$Entry implements SelectableEntry {
   private static final int MAX_DESCRIPTION_WIDTH_PIXELS = 157;
   public static final int ICON_SIZE = 32;
   private final TransferableSelectionList parent;
   protected final Minecraft minecraft;
   private final PackSelectionModel$Entry pack;
   private final StringWidget nameWidget;
   private final MultiLineTextWidget descriptionWidget;

   public TransferableSelectionList$PackEntry(
      final TransferableSelectionList this$0, final Minecraft minecraft, final TransferableSelectionList parent, final PackSelectionModel$Entry pack
   ) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(this$0);
      this.minecraft = minecraft;
      this.pack = pack;
      this.parent = parent;
      this.nameWidget = new StringWidget(pack.getTitle(), minecraft.font);
      this.descriptionWidget = new MultiLineTextWidget(
         ComponentUtils.mergeStyles(pack.getExtendedDescription(), Style.EMPTY.withColor(-8355712)), minecraft.font
      );
      this.descriptionWidget.setMaxRows(2);
   }

   @Override
   public Component getNarration() {
      return Component.translatable("narrator.select", this.pack.getTitle());
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      PackCompatibility compatibility = this.pack.getCompatibility();
      if (!compatibility.isCompatible()) {
         int x0 = this.getContentX() - 1;
         int y0 = this.getContentY() - 1;
         int x1 = this.getContentRight() + 1;
         int y1 = this.getContentBottom() + 1;
         graphics.fill(x0, y0, x1, y1, -8978432);
      }

      graphics.blit(RenderPipelines.GUI_TEXTURED, this.pack.getIconTexture(), this.getContentX(), this.getContentY(), 0.0F, 0.0F, 32, 32, 32, 32);
      if (!this.nameWidget.getMessage().equals(this.pack.getTitle())) {
         this.nameWidget.setMessage(this.pack.getTitle());
      }

      if (!this.descriptionWidget.getMessage().getContents().equals(this.pack.getExtendedDescription().getContents())) {
         this.descriptionWidget.setMessage(ComponentUtils.mergeStyles(this.pack.getExtendedDescription(), Style.EMPTY.withColor(-8355712)));
      }

      if (this.showHoverOverlay() && (hovered || this.parent.getSelected() == this && this.parent.isFocused())) {
         graphics.fill(this.getContentX(), this.getContentY(), this.getContentX() + 32, this.getContentY() + 32, -1601138544);
         int relX = mouseX - this.getContentX();
         int relY = mouseY - this.getContentY();
         if (!this.pack.getCompatibility().isCompatible()) {
            this.nameWidget.setMessage(TransferableSelectionList.INCOMPATIBLE_TITLE);
            this.descriptionWidget.setMessage(this.pack.getCompatibility().getDescription());
         }

         if (this.pack.canSelect()) {
            if (this.mouseOverIcon(relX, relY, 32)) {
               graphics.blitSprite(
                  RenderPipelines.GUI_TEXTURED, TransferableSelectionList.SELECT_HIGHLIGHTED_SPRITE, this.getContentX(), this.getContentY(), 32, 32
               );
               TransferableSelectionList.access$100(this.this$0, graphics);
            } else {
               graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TransferableSelectionList.SELECT_SPRITE, this.getContentX(), this.getContentY(), 32, 32);
            }
         } else {
            if (this.pack.canUnselect()) {
               if (this.mouseOverLeftHalf(relX, relY, 32)) {
                  graphics.blitSprite(
                     RenderPipelines.GUI_TEXTURED, TransferableSelectionList.UNSELECT_HIGHLIGHTED_SPRITE, this.getContentX(), this.getContentY(), 32, 32
                  );
                  TransferableSelectionList.access$200(this.this$0, graphics);
               } else {
                  graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TransferableSelectionList.UNSELECT_SPRITE, this.getContentX(), this.getContentY(), 32, 32);
               }
            }

            if (this.pack.canMoveUp()) {
               if (this.mouseOverTopRightQuarter(relX, relY, 32)) {
                  graphics.blitSprite(
                     RenderPipelines.GUI_TEXTURED, TransferableSelectionList.MOVE_UP_HIGHLIGHTED_SPRITE, this.getContentX(), this.getContentY(), 32, 32
                  );
                  TransferableSelectionList.access$300(this.this$0, graphics);
               } else {
                  graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TransferableSelectionList.MOVE_UP_SPRITE, this.getContentX(), this.getContentY(), 32, 32);
               }
            }

            if (this.pack.canMoveDown()) {
               if (this.mouseOverBottomRightQuarter(relX, relY, 32)) {
                  graphics.blitSprite(
                     RenderPipelines.GUI_TEXTURED, TransferableSelectionList.MOVE_DOWN_HIGHLIGHTED_SPRITE, this.getContentX(), this.getContentY(), 32, 32
                  );
                  TransferableSelectionList.access$400(this.this$0, graphics);
               } else {
                  graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TransferableSelectionList.MOVE_DOWN_SPRITE, this.getContentX(), this.getContentY(), 32, 32);
               }
            }
         }
      }

      this.nameWidget.setMaxWidth(157 - (TransferableSelectionList.access$500(this.this$0) ? 6 : 0));
      this.nameWidget.setPosition(this.getContentX() + 32 + 2, this.getContentY() + 1);
      this.nameWidget.extractRenderState(graphics, mouseX, mouseY, a);
      this.descriptionWidget.setMaxWidth(157 - (TransferableSelectionList.access$600(this.this$0) ? 6 : 0));
      this.descriptionWidget.setPosition(this.getContentX() + 32 + 2, this.getContentY() + 12);
      this.descriptionWidget.extractRenderState(graphics, mouseX, mouseY, a);
   }

   @Override
   public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
      if (this.showHoverOverlay()) {
         int relX = (int)event.x() - this.getContentX();
         int relY = (int)event.y() - this.getContentY();
         if (this.pack.canSelect() && this.mouseOverIcon(relX, relY, 32)) {
            this.handlePackSelection();
            return true;
         }

         if (this.pack.canUnselect() && this.mouseOverLeftHalf(relX, relY, 32)) {
            this.pack.unselect();
            return true;
         }

         if (this.pack.canMoveUp() && this.mouseOverTopRightQuarter(relX, relY, 32)) {
            this.pack.moveUp();
            return true;
         }

         if (this.pack.canMoveDown() && this.mouseOverBottomRightQuarter(relX, relY, 32)) {
            this.pack.moveDown();
            return true;
         }
      }

      return super.mouseClicked(event, doubleClick);
   }

   @Override
   public boolean keyPressed(final KeyEvent event) {
      if (event.isConfirmation()) {
         this.keyboardSelection();
         return true;
      } else {
         if (event.hasShiftDown()) {
            if (event.isUp()) {
               this.keyboardMoveUp();
               return true;
            }

            if (event.isDown()) {
               this.keyboardMoveDown();
               return true;
            }
         }

         return super.keyPressed(event);
      }
   }

   private boolean showHoverOverlay() {
      return !this.pack.isFixedPosition() || !this.pack.isRequired();
   }

   public void keyboardSelection() {
      if (this.pack.canSelect()) {
         this.handlePackSelection();
      } else if (this.pack.canUnselect()) {
         this.pack.unselect();
      }
   }

   private void keyboardMoveUp() {
      if (this.pack.canMoveUp()) {
         this.pack.moveUp();
      }
   }

   private void keyboardMoveDown() {
      if (this.pack.canMoveDown()) {
         this.pack.moveDown();
      }
   }

   private void handlePackSelection() {
      if (this.pack.getCompatibility().isCompatible()) {
         this.pack.select();
      } else {
         Component reason = this.pack.getCompatibility().getConfirmation();
         this.minecraft.gui.setScreen(new ConfirmScreen(result -> {
            this.minecraft.gui.setScreen(this.parent.screen);
            if (result) {
               this.pack.select();
            }
         }, TransferableSelectionList.INCOMPATIBLE_CONFIRM_TITLE, reason));
      }
   }

   @Override
   public String getPackId() {
      return this.pack.getId();
   }

   @Override
   public boolean shouldTakeFocusAfterInteraction() {
      return this.this$0.children().stream().anyMatch(entry -> entry.getPackId().equals(this.getPackId()));
   }
}
