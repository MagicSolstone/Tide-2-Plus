package net.minecraft.gametest.framework;

import java.util.Objects;
import net.minecraft.network.chat.Component;

public class GameTestSequence$Condition {
   private static final int NOT_TRIGGERED = -1;
   private int triggerTime;

   public GameTestSequence$Condition(final GameTestSequence this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.triggerTime = -1;
   }

   public void trigger(final int time) {
      if (this.triggerTime != -1) {
         throw new IllegalStateException("Condition already triggered at " + this.triggerTime);
      } else {
         this.triggerTime = time;
      }
   }

   public void assertTriggeredThisTick() {
      int tick = this.this$0.parent.getTick();
      if (this.triggerTime != tick) {
         if (this.triggerTime == -1) {
            throw new GameTestAssertException(Component.translatable("test.error.sequence.condition_not_triggered"), tick);
         } else {
            throw new GameTestAssertException(Component.translatable("test.error.sequence.condition_already_triggered", this.triggerTime), tick);
         }
      }
   }
}
