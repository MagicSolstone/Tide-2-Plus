package net.minecraft.world.entity.animal.panda;

import com.mojang.serialization.Codec;
import java.util.function.IntFunction;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;

public enum Panda$Gene implements StringRepresentable {
   NORMAL(0, "normal", false),
   LAZY(1, "lazy", false),
   WORRIED(2, "worried", false),
   PLAYFUL(3, "playful", false),
   BROWN(4, "brown", true),
   WEAK(5, "weak", true),
   AGGRESSIVE(6, "aggressive", false);

   public static final Codec<Panda$Gene> CODEC = StringRepresentable.fromEnum(Panda$Gene::values);
   private static final IntFunction<Panda$Gene> BY_ID = ByIdMap.continuous(Panda$Gene::getId, values(), ByIdMap$OutOfBoundsStrategy.ZERO);
   private static final int MAX_GENE = 6;
   private final int id;
   private final String name;
   private final boolean isRecessive;

   private Panda$Gene(final int id, final String name, final boolean isRecessive) {
      this.id = id;
      this.name = name;
      this.isRecessive = isRecessive;
   }

   public int getId() {
      return this.id;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   public boolean isRecessive() {
      return this.isRecessive;
   }

   private static Panda$Gene getVariantFromGenes(final Panda$Gene mainGene, final Panda$Gene hiddenGene) {
      if (mainGene.isRecessive()) {
         return mainGene == hiddenGene ? mainGene : NORMAL;
      } else {
         return mainGene;
      }
   }

   public static Panda$Gene byId(final int id) {
      return BY_ID.apply(id);
   }

   public static Panda$Gene getRandom(final RandomSource random) {
      int nextInt = random.nextInt(16);
      if (nextInt == 0) {
         return LAZY;
      } else if (nextInt == 1) {
         return WORRIED;
      } else if (nextInt == 2) {
         return PLAYFUL;
      } else if (nextInt == 4) {
         return AGGRESSIVE;
      } else if (nextInt < 9) {
         return WEAK;
      } else {
         return nextInt < 11 ? BROWN : NORMAL;
      }
   }
}
