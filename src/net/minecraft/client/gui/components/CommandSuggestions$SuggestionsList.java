package net.minecraft.client.gui.components;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.suggestion.Suggestion;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

@Environment(EnvType.CLIENT)
public class CommandSuggestions$SuggestionsList {
   private final Rect2i rect;
   private final String originalContents;
   private final List<Suggestion> suggestionList;
   private int offset;
   private int current;
   private Vec2 lastMouse;
   private boolean tabCycles;
   private int lastNarratedEntry;

   private CommandSuggestions$SuggestionsList(
      final int this$0, final int x, final int y, final List<Suggestion> width, final boolean suggestionList, final boolean immediateNarration
   ) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.lastMouse = Vec2.ZERO;
      int listX = x - (this$0.input.isBordered() ? 0 : 1);
      int listY = this$0.anchorToBottom ? y - 3 - Math.min(suggestionList.size(), this$0.suggestionLineLimit) * 12 : y - (this$0.input.isBordered() ? 1 : 0);
      this.rect = new Rect2i(listX, listY, width + 1, Math.min(suggestionList.size(), this$0.suggestionLineLimit) * 12);
      this.originalContents = this$0.input.getValue();
      this.lastNarratedEntry = immediateNarration ? -1 : 0;
      this.suggestionList = suggestionList;
      this.select(0);
   }

   public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY) {
      int limit = Math.min(this.suggestionList.size(), this.this$0.suggestionLineLimit);
      int unselectedColor = -5592406;
      boolean hasPrevious = this.offset > 0;
      boolean hasNext = this.suggestionList.size() > this.offset + limit;
      boolean limited = hasPrevious || hasNext;
      boolean mouseMoved = this.lastMouse.x != mouseX || this.lastMouse.y != mouseY;
      if (mouseMoved) {
         this.lastMouse = new Vec2(mouseX, mouseY);
      }

      if (limited) {
         graphics.fill(this.rect.getX(), this.rect.getY() - 1, this.rect.getX() + this.rect.getWidth(), this.rect.getY(), this.this$0.fillColor);
         graphics.fill(
            this.rect.getX(),
            this.rect.getY() + this.rect.getHeight(),
            this.rect.getX() + this.rect.getWidth(),
            this.rect.getY() + this.rect.getHeight() + 1,
            this.this$0.fillColor
         );
         if (hasPrevious) {
            for (int x = 0; x < this.rect.getWidth(); x++) {
               if (x % 2 == 0) {
                  graphics.fill(this.rect.getX() + x, this.rect.getY() - 1, this.rect.getX() + x + 1, this.rect.getY(), -1);
               }
            }
         }

         if (hasNext) {
            for (int xx = 0; xx < this.rect.getWidth(); xx++) {
               if (xx % 2 == 0) {
                  graphics.fill(
                     this.rect.getX() + xx,
                     this.rect.getY() + this.rect.getHeight(),
                     this.rect.getX() + xx + 1,
                     this.rect.getY() + this.rect.getHeight() + 1,
                     -1
                  );
               }
            }
         }
      }

      boolean hovered = false;

      for (int i = 0; i < limit; i++) {
         Suggestion suggestion = this.suggestionList.get(i + this.offset);
         graphics.fill(
            this.rect.getX(), this.rect.getY() + 12 * i, this.rect.getX() + this.rect.getWidth(), this.rect.getY() + 12 * i + 12, this.this$0.fillColor
         );
         if (mouseX > this.rect.getX()
            && mouseX < this.rect.getX() + this.rect.getWidth()
            && mouseY > this.rect.getY() + 12 * i
            && mouseY < this.rect.getY() + 12 * i + 12) {
            if (mouseMoved) {
               this.select(i + this.offset);
            }

            hovered = true;
         }

         graphics.text(
            this.this$0.font, suggestion.getText(), this.rect.getX() + 1, this.rect.getY() + 2 + 12 * i, i + this.offset == this.current ? -256 : -5592406
         );
      }

      if (hovered) {
         Message tooltip = this.suggestionList.get(this.current).getTooltip();
         if (tooltip != null) {
            graphics.setTooltipForNextFrame(this.this$0.font, ComponentUtils.fromMessage(tooltip), mouseX, mouseY);
         }
      }

      if (this.rect.contains(mouseX, mouseY)) {
         graphics.requestCursor(CursorTypes.POINTING_HAND);
      }
   }

   public boolean mouseClicked(final int x, final int y) {
      if (!this.rect.contains(x, y)) {
         return false;
      } else {
         int line = (y - this.rect.getY()) / 12 + this.offset;
         if (line >= 0 && line < this.suggestionList.size()) {
            this.select(line);
            this.useSuggestion();
         }

         return true;
      }
   }

   public boolean mouseScrolled(final double scroll) {
      int mouseX = (int)this.this$0.minecraft.mouseHandler.getScaledXPos(this.this$0.minecraft.getWindow());
      int mouseY = (int)this.this$0.minecraft.mouseHandler.getScaledYPos(this.this$0.minecraft.getWindow());
      if (this.rect.contains(mouseX, mouseY)) {
         this.offset = Mth.clamp((int)(this.offset - scroll), 0, Math.max(this.suggestionList.size() - this.this$0.suggestionLineLimit, 0));
         return true;
      } else {
         return false;
      }
   }

   public boolean keyPressed(final KeyEvent event) {
      if (event.isUp()) {
         this.cycle(-1);
         this.tabCycles = false;
         return true;
      } else if (event.isDown()) {
         this.cycle(1);
         this.tabCycles = false;
         return true;
      } else if (event.isCycleFocus()) {
         if (this.tabCycles) {
            this.cycle(event.hasShiftDown() ? -1 : 1);
         }

         this.useSuggestion();
         return true;
      } else if (event.isEscape()) {
         this.this$0.hide();
         this.this$0.input.setSuggestion(null);
         return true;
      } else {
         return false;
      }
   }

   public void cycle(final int direction) {
      this.select(this.current + direction);
      int first = this.offset;
      int last = this.offset + this.this$0.suggestionLineLimit - 1;
      if (this.current < first) {
         this.offset = Mth.clamp(this.current, 0, Math.max(this.suggestionList.size() - this.this$0.suggestionLineLimit, 0));
      } else if (this.current > last) {
         this.offset = Mth.clamp(
            this.current + this.this$0.lineStartOffset - this.this$0.suggestionLineLimit,
            0,
            Math.max(this.suggestionList.size() - this.this$0.suggestionLineLimit, 0)
         );
      }
   }

   public void select(final int index) {
      this.current = index;
      if (this.current < 0) {
         this.current = this.current + this.suggestionList.size();
      }

      if (this.current >= this.suggestionList.size()) {
         this.current = this.current - this.suggestionList.size();
      }

      Suggestion suggestion = this.suggestionList.get(this.current);
      this.this$0.input.setSuggestion(CommandSuggestions.calculateSuggestionSuffix(this.this$0.input.getValue(), suggestion.apply(this.originalContents)));
      if (this.lastNarratedEntry != this.current) {
         this.this$0.minecraft.getNarrator().saySystemNow(this.getNarrationMessage());
      }
   }

   public void useSuggestion() {
      Suggestion suggestion = this.suggestionList.get(this.current);
      this.this$0.keepSuggestions = true;
      this.this$0.input.setValue(suggestion.apply(this.originalContents));
      int end = suggestion.getRange().getStart() + suggestion.getText().length();
      this.this$0.input.setCursorPosition(end);
      this.this$0.input.setHighlightPos(end);
      this.select(this.current);
      this.this$0.keepSuggestions = false;
      this.tabCycles = true;
   }

   private Component getNarrationMessage() {
      this.lastNarratedEntry = this.current;
      Suggestion suggestion = this.suggestionList.get(this.current);
      Message tooltip = suggestion.getTooltip();
      return tooltip != null
         ? Component.translatable(
            "narration.suggestion.tooltip", this.current + 1, this.suggestionList.size(), suggestion.getText(), Component.translationArg(tooltip)
         )
         : Component.translatable("narration.suggestion", this.current + 1, this.suggestionList.size(), suggestion.getText());
   }
}
