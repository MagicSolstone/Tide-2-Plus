package net.minecraft.tags;

import java.util.Collection;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public interface TagEntry$Lookup<T> {
   @Nullable
   T element(Identifier key, boolean required);

   @Nullable
   Collection<T> tag(Identifier key);
}
