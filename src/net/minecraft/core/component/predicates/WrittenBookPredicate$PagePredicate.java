package net.minecraft.core.component.predicates;

import com.mojang.serialization.Codec;
import java.util.function.Predicate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.network.Filterable;

public record WrittenBookPredicate$PagePredicate(Component contents) implements Predicate<Filterable<Component>> {
   public static final Codec<WrittenBookPredicate$PagePredicate> CODEC = ComponentSerialization.CODEC
      .xmap(WrittenBookPredicate$PagePredicate::new, WrittenBookPredicate$PagePredicate::contents);

   public boolean test(final Filterable<Component> value) {
      return value.raw().equals(this.contents);
   }
}
