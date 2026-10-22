package net.minecraft.world.level.block.entity;

import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;

public class SignText$Mutable {
   private final Component[] messages;
   private final Component[] filteredMessages;
   private DyeColor color;
   private boolean isTextGlowing;

   private SignText$Mutable(final SignText original) {
      this.messages = original.messages.toArray(Component[]::new);
      this.filteredMessages = original.messages.toArray(Component[]::new);
      this.color = original.color;
      this.isTextGlowing = original.hasGlowingText;
   }

   public SignText asImmutable() {
      return new SignText(List.of(this.messages), List.of(this.filteredMessages), this.color, this.isTextGlowing);
   }

   public SignText$Mutable setLine(final int index, final Component raw, final Component filtered) {
      this.messages[index] = raw;
      this.filteredMessages[index] = filtered;
      return this;
   }

   public SignText$Mutable setLine(final int index, final Component text) {
      return this.setLine(index, text, text);
   }

   public SignText$Mutable modifyLine(final int index, final UnaryOperator<Component> modifier) {
      this.messages[index] = modifier.apply(this.messages[index]);
      this.filteredMessages[index] = modifier.apply(this.filteredMessages[index]);
      return this;
   }

   public SignText$Mutable modifyLines(final UnaryOperator<Component> modifier) {
      for (int i = 0; i < this.messages.length; i++) {
         this.modifyLine(i, modifier);
      }

      return this;
   }

   public SignText$Mutable setColor(final DyeColor color) {
      this.color = color;
      return this;
   }

   public SignText$Mutable setTextGlowing(final boolean flag) {
      this.isTextGlowing = flag;
      return this;
   }
}
