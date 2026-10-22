package net.minecraft.world.scores;

import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.NumberFormat;
import org.jspecify.annotations.Nullable;

public class Score implements ReadOnlyScoreInfo {
   private int value;
   private boolean locked = true;
   @Nullable
   private Component display;
   @Nullable
   private NumberFormat numberFormat;

   public Score() {
   }

   public Score(final Score$Packed packed) {
      this.value = packed.value;
      this.locked = packed.locked;
      this.display = packed.display.orElse(null);
      this.numberFormat = packed.numberFormat.orElse(null);
   }

   public Score$Packed pack() {
      return new Score$Packed(this.value, this.locked, Optional.ofNullable(this.display), Optional.ofNullable(this.numberFormat));
   }

   @Override
   public int value() {
      return this.value;
   }

   public void value(final int score) {
      this.value = score;
   }

   @Override
   public boolean isLocked() {
      return this.locked;
   }

   public void setLocked(final boolean locked) {
      this.locked = locked;
   }

   @Nullable
   public Component display() {
      return this.display;
   }

   public void display(@Nullable final Component display) {
      this.display = display;
   }

   @Nullable
   @Override
   public NumberFormat numberFormat() {
      return this.numberFormat;
   }

   public void numberFormat(@Nullable final NumberFormat numberFormat) {
      this.numberFormat = numberFormat;
   }
}
