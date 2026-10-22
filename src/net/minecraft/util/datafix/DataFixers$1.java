package net.minecraft.util.datafix;

import com.mojang.datafixers.DSL.TypeReference;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.BlockEntityCustomNameToComponentFix;
import net.minecraft.util.datafix.fixes.NamedEntityWriteReadFix;

class DataFixers$1 extends NamedEntityWriteReadFix {
   DataFixers$1(final Schema outputSchema, final boolean changesType, final String name, final TypeReference type, final String entityName) {
      super(outputSchema, changesType, name, type, entityName);
   }

   @Override
   protected <T> Dynamic<T> fix(final Dynamic<T> input) {
      return BlockEntityCustomNameToComponentFix.fixTagCustomName(input);
   }
}
