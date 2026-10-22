package net.minecraft.world.entity.monster.illager;

import java.util.function.IntFunction;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;

public enum SpellcasterIllager$IllagerSpell {
   NONE(0, 0.0, 0.0, 0.0),
   SUMMON_VEX(1, 0.7, 0.7, 0.8),
   FANGS(2, 0.4, 0.3, 0.35),
   WOLOLO(3, 0.7, 0.5, 0.2),
   DISAPPEAR(4, 0.3, 0.3, 0.8),
   BLINDNESS(5, 0.1, 0.1, 0.2);

   private static final IntFunction<SpellcasterIllager$IllagerSpell> BY_ID = ByIdMap.continuous(e -> e.id, values(), ByIdMap$OutOfBoundsStrategy.ZERO);
   private final int id;
   private final double[] spellColor;

   private SpellcasterIllager$IllagerSpell(final int id, final double red, final double green, final double blue) {
      this.id = id;
      this.spellColor = new double[]{red, green, blue};
   }

   public static SpellcasterIllager$IllagerSpell byId(final int id) {
      return BY_ID.apply(id);
   }
}
