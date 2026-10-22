package net.minecraft.network;

import java.util.function.Function;
import net.minecraft.core.component.TypedDataComponent;

@FunctionalInterface
public interface HashedPatchMap$HashGenerator extends Function<TypedDataComponent<?>, Integer> {
}
