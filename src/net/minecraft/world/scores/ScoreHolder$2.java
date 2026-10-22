package net.minecraft.world.scores;

import net.minecraft.network.chat.Component;

class ScoreHolder$2 implements ScoreHolder {
   ScoreHolder$2(final String val$name, final Component val$feedbackName) {
      this.val$name = val$namex;
      this.val$feedbackName = val$feedbackNamex;
   }

   @Override
   public String getScoreboardName() {
      return this.val$name;
   }

   @Override
   public Component getFeedbackDisplayName() {
      return this.val$feedbackName;
   }
}
