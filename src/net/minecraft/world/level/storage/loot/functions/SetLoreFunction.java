package net.minecraft.world.level.storage.loot.functions;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContext$EntityTarget;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jspecify.annotations.Nullable;

public class SetLoreFunction extends LootItemConditionalFunction {
   public static final MapCodec<SetLoreFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> commonFields(i)
         .and(
            i.group(
               ComponentSerialization.CODEC.sizeLimitedListOf(256).fieldOf("lore").forGetter(f -> f.lore),
               ListOperation.codec(256).forGetter(f -> f.mode),
               LootContext$EntityTarget.CODEC.optionalFieldOf("entity").forGetter(f -> f.resolutionContext)
            )
         )
         .apply(i, SetLoreFunction::new)
   );
   private final List<Component> lore;
   private final ListOperation mode;
   private final Optional<LootContext$EntityTarget> resolutionContext;

   public SetLoreFunction(
      final Optional<Holder<LootItemCondition>> condition,
      final List<Component> lore,
      final ListOperation mode,
      final Optional<LootContext$EntityTarget> resolutionContext
   ) {
      super(condition);
      this.lore = List.copyOf(lore);
      this.mode = mode;
      this.resolutionContext = resolutionContext;
   }

   @Override
   public MapCodec<SetLoreFunction> codec() {
      return MAP_CODEC;
   }

   @Override
   public Set<ContextKey<?>> getReferencedContextParams() {
      return (Set<ContextKey<?>>)DataFixUtils.orElse(this.resolutionContext.map(target -> Set.of(target.contextParam())), Set.of());
   }

   @Override
   public ItemStack run(final ItemStack itemStack, final LootContext context) {
      itemStack.update(DataComponents.LORE, ItemLore.EMPTY, oldLore -> new ItemLore(this.updateLore(oldLore, context)));
      return itemStack;
   }

   private List<Component> updateLore(@Nullable final ItemLore itemLore, final LootContext context) {
      if (itemLore == null && this.lore.isEmpty()) {
         return List.of();
      } else {
         UnaryOperator<Component> resolver = SetNameFunction.createResolver(context, this.resolutionContext.orElse(null));
         List<Component> resolvedLines = this.lore.stream().map(resolver).toList();
         return this.mode.apply(itemLore.lines(), resolvedLines, 256);
      }
   }

   public static SetLoreFunction$Builder setLore() {
      return new SetLoreFunction$Builder();
   }
}
