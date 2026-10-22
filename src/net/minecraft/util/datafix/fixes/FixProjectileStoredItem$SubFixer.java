package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.Typed;
import com.mojang.datafixers.types.Type;

interface FixProjectileStoredItem$SubFixer<F> {
   Typed<F> fix(final Typed<?> input, final Type<F> outputType);
}
