package net.minecraft.world.scores;

import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.NumberFormat;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jspecify.annotations.Nullable;

class Scoreboard$1 implements ScoreAccess {
   Scoreboard$1(
      final Scoreboard this$0,
      final Score val$score,
      final boolean val$canModify,
      final MutableBoolean val$requiresSync,
      final Objective val$objective,
      final ScoreHolder val$scoreHolder
   ) {
      this.val$score = val$scorex;
      this.val$canModify = val$canModifyx;
      this.val$requiresSync = val$requiresSyncx;
      this.val$objective = val$objectivex;
      this.val$scoreHolder = val$scoreHolderx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public int get() {
      return this.val$score.value();
   }

   @Override
   public void set(final int value) {
      if (!this.val$canModify) {
         throw new IllegalStateException("Cannot modify read-only score");
      } else {
         boolean hasChanged = this.val$requiresSync.isTrue();
         if (this.val$objective.displayAutoUpdate()) {
            Component newDisplay = this.val$scoreHolder.getDisplayName();
            if (newDisplay != null && !newDisplay.equals(this.val$score.display())) {
               this.val$score.display(newDisplay);
               hasChanged = true;
            }
         }

         if (value != this.val$score.value()) {
            this.val$score.value(value);
            hasChanged = true;
         }

         if (hasChanged) {
            this.sendScoreToPlayers();
         }
      }
   }

   @Nullable
   @Override
   public Component display() {
      return this.val$score.display();
   }

   @Override
   public void display(@Nullable final Component display) {
      if (this.val$requiresSync.isTrue() || !Objects.equals(display, this.val$score.display())) {
         this.val$score.display(display);
         this.sendScoreToPlayers();
      }
   }

   @Override
   public void numberFormatOverride(@Nullable final NumberFormat numberFormat) {
      this.val$score.numberFormat(numberFormat);
      this.sendScoreToPlayers();
   }

   @Override
   public boolean locked() {
      return this.val$score.isLocked();
   }

   @Override
   public void unlock() {
      this.setLocked(false);
   }

   @Override
   public void lock() {
      this.setLocked(true);
   }

   private void setLocked(final boolean locked) {
      this.val$score.setLocked(locked);
      if (this.val$requiresSync.isTrue()) {
         this.sendScoreToPlayers();
      }

      this.this$0.onScoreLockChanged(this.val$scoreHolder, this.val$objective);
   }

   private void sendScoreToPlayers() {
      this.this$0.onScoreChanged(this.val$scoreHolder, this.val$objective, this.val$score);
      this.val$requiresSync.setFalse();
   }
}
