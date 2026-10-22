package net.minecraft.world.level.storage.loot.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.util.ProblemReporter$Problem;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public abstract class CompositeEntryBase extends LootPoolEntryContainer {
   public static final ProblemReporter$Problem NO_CHILDREN_PROBLEM = new CompositeEntryBase$1();
   protected final List<LootPoolEntryContainer> children;
   private final ComposableEntryContainer composedChildren;

   protected CompositeEntryBase(
      final List<LootPoolEntryContainer> children, final Optional<Holder<LootItemCondition>> condition, final Optional<Holder<LootItemFunction>> modifier
   ) {
      super(condition, modifier);
      this.children = children;
      this.composedChildren = this.compose(children);
   }

   @Override
   public abstract MapCodec<? extends CompositeEntryBase> codec();

   @Override
   public void validate(final ValidationContext context) {
      super.validate(context);
      if (this.children.isEmpty()) {
         context.reportProblem(NO_CHILDREN_PROBLEM);
      }

      Validatable.validate(context, "children", this.children);
   }

   protected abstract ComposableEntryContainer compose(List<? extends ComposableEntryContainer> entries);

   @Override
   public final boolean expandRaw(final LootContext context, final Consumer<LootPoolEntry> output) {
      return this.composedChildren.expand(context, output);
   }

   public static <T extends CompositeEntryBase> MapCodec<T> createCodec(final CompositeEntryBase$CompositeEntryConstructor<T> constructor) {
      return RecordCodecBuilder.mapCodec(
         i -> i.group(LootPoolEntries.CODEC.listOf().optionalFieldOf("children", List.of()).forGetter(e -> e.children))
            .and(commonFields(i))
            .apply(i, constructor::create)
      );
   }
}
