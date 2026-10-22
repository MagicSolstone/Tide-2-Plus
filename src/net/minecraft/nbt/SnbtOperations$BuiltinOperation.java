package net.minecraft.nbt;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.DynamicOps;
import java.util.List;
import net.minecraft.util.parsing.packrat.ParseState;
import org.jspecify.annotations.Nullable;

public interface SnbtOperations$BuiltinOperation {
   @Nullable
   <T> T run(DynamicOps<T> ops, List<T> arguments, ParseState<StringReader> state);
}
