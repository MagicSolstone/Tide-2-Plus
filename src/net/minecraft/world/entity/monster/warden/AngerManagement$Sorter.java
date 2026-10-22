package net.minecraft.world.entity.monster.warden;

import com.google.common.annotations.VisibleForTesting;
import java.util.Comparator;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

@VisibleForTesting
public record AngerManagement$Sorter(AngerManagement angerManagement) implements Comparator<Entity> {
   public int compare(final Entity entity1, final Entity entity2) {
      if (entity1.equals(entity2)) {
         return 0;
      } else {
         int anger1 = this.angerManagement.angerBySuspect.getOrDefault(entity1, 0);
         int anger2 = this.angerManagement.angerBySuspect.getOrDefault(entity2, 0);
         this.angerManagement.highestAnger = Math.max(this.angerManagement.highestAnger, Math.max(anger1, anger2));
         boolean angryAt1 = AngerLevel.byAnger(anger1).isAngry();
         boolean angryAt2 = AngerLevel.byAnger(anger2).isAngry();
         if (angryAt1 != angryAt2) {
            return angryAt1 ? -1 : 1;
         } else {
            boolean isPlayer1 = entity1 instanceof Player;
            boolean isPlayer2 = entity2 instanceof Player;
            if (isPlayer1 != isPlayer2) {
               return isPlayer1 ? -1 : 1;
            } else {
               return Integer.compare(anger2, anger1);
            }
         }
      }
   }
}
