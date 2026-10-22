package net.minecraft.core.component.predicates;

import com.mojang.serialization.Codec;
import java.util.function.Predicate;
import net.minecraft.server.network.Filterable;

public record WritableBookPredicate$PagePredicate(String contents) implements Predicate<Filterable<String>> {
   public static final Codec<WritableBookPredicate$PagePredicate> CODEC = Codec.STRING
      .xmap(WritableBookPredicate$PagePredicate::new, WritableBookPredicate$PagePredicate::contents);

   public boolean test(final Filterable<String> value) {
      return value.raw().equals(this.contents);
   }
}
