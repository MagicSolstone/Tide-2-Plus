package net.minecraft.world.level.levelgen.structure.placement;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum AbstractSpreadingStructurePlacement$FrequencyReductionMethod implements StringRepresentable {
   DEFAULT("default", AbstractSpreadingStructurePlacement::probabilityReducer),
   LEGACY_TYPE_1("legacy_type_1", AbstractSpreadingStructurePlacement::legacyPillagerOutpostReducer),
   LEGACY_TYPE_2("legacy_type_2", AbstractSpreadingStructurePlacement::legacyArbitrarySaltProbabilityReducer),
   LEGACY_TYPE_3("legacy_type_3", AbstractSpreadingStructurePlacement::legacyProbabilityReducerWithDouble);

   public static final Codec<AbstractSpreadingStructurePlacement$FrequencyReductionMethod> CODEC = StringRepresentable.fromEnum(
      AbstractSpreadingStructurePlacement$FrequencyReductionMethod::values
   );
   private final String name;
   private final AbstractSpreadingStructurePlacement$FrequencyReducer reducer;

   private AbstractSpreadingStructurePlacement$FrequencyReductionMethod(final String name, final AbstractSpreadingStructurePlacement$FrequencyReducer reducer) {
      this.name = name;
      this.reducer = reducer;
   }

   public boolean shouldGenerate(final long seed, final int salt, final int sourceX, final int sourceZ, final float probability) {
      return this.reducer.shouldGenerate(seed, salt, sourceX, sourceZ, probability);
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }
}
