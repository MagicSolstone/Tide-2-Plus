package net.minecraft.world.entity.animal.parrot;

import java.util.function.Predicate;
import net.minecraft.world.entity.Mob;
import org.jspecify.annotations.Nullable;

class Parrot$1 implements Predicate<Mob> {
   public boolean test(@Nullable final Mob input) {
      return input != null && Parrot.MOB_SOUND_MAP.containsKey(input.getType());
   }
}
