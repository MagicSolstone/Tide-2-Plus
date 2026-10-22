package net.minecraft.world.scores;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent$ShowText;
import org.jspecify.annotations.Nullable;

public interface ScoreHolder {
   String WILDCARD_NAME = "*";
   ScoreHolder WILDCARD = new ScoreHolder$1();

   String getScoreboardName();

   @Nullable
   default Component getDisplayName() {
      return null;
   }

   default Component getFeedbackDisplayName() {
      Component displayName = this.getDisplayName();
      return displayName != null
         ? displayName.copy().withStyle(style -> style.withHoverEvent(new HoverEvent$ShowText(Component.literal(this.getScoreboardName()))))
         : Component.literal(this.getScoreboardName());
   }

   static ScoreHolder forNameOnly(final String name) {
      if (name.equals("*")) {
         return WILDCARD;
      } else {
         Component feedbackName = Component.literal(name);
         return new ScoreHolder$2(name, feedbackName);
      }
   }

   static ScoreHolder fromGameProfile(final GameProfile profile) {
      String name = profile.name();
      return new ScoreHolder$3(name);
   }
}
