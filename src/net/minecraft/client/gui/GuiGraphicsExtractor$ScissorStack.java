package net.minecraft.client.gui;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class GuiGraphicsExtractor$ScissorStack {
   private final ScreenRectangle screenSize;
   private final Deque<ScreenRectangle> stack = new ArrayDeque<>();

   private GuiGraphicsExtractor$ScissorStack(final ScreenRectangle screenSize) {
      this.screenSize = screenSize;
   }

   public void push(final ScreenRectangle rectangle) {
      ScreenRectangle lastRectangle = Objects.requireNonNullElse(this.stack.peekLast(), this.screenSize);
      ScreenRectangle intersection = Objects.requireNonNullElse(rectangle.intersection(lastRectangle), ScreenRectangle.empty());
      this.stack.addLast(intersection);
   }

   public void pop() {
      if (this.stack.isEmpty()) {
         throw new IllegalStateException("Scissor stack underflow");
      } else {
         this.stack.removeLast();
      }
   }

   @Nullable
   public ScreenRectangle peek() {
      return this.stack.peekLast();
   }

   public boolean containsPoint(final int x, final int y) {
      return this.stack.isEmpty() ? true : this.stack.peek().containsPoint(x, y);
   }
}
