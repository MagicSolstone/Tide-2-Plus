package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Util;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextArg;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class CopyComponentsFunction extends LootItemConditionalFunction {
   private static final Codec<LootContextArg<DataComponentGetter>> GETTER_CODEC = LootContextArg.createArgCodec(
      builder -> builder.anyEntity(CopyComponentsFunction$DirectSource::new)
         .anyBlockEntity(CopyComponentsFunction$BlockEntitySource::new)
         .anyItemStack(CopyComponentsFunction$DirectSource::new)
   );
   public static final MapCodec<CopyComponentsFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> commonFields(i)
         .and(
            i.group(
               GETTER_CODEC.fieldOf("source").forGetter(f -> f.source),
               DataComponentType.CODEC.listOf().optionalFieldOf("include").forGetter(f -> f.include),
               DataComponentType.CODEC.listOf().optionalFieldOf("exclude").forGetter(f -> f.exclude)
            )
         )
         .apply(i, CopyComponentsFunction::new)
   );
   private final LootContextArg<DataComponentGetter> source;
   private final Optional<List<DataComponentType<?>>> include;
   private final Optional<List<DataComponentType<?>>> exclude;
   private final Predicate<DataComponentType<?>> bakedPredicate;

   private CopyComponentsFunction(
      final Optional<Holder<LootItemCondition>> condition,
      final LootContextArg<DataComponentGetter> source,
      final Optional<List<DataComponentType<?>>> include,
      final Optional<List<DataComponentType<?>>> exclude
   ) {
      super(condition);
      this.source = source;
      this.include = include.map(List::copyOf);
      this.exclude = exclude.map(List::copyOf);
      List<Predicate<DataComponentType<?>>> componentPredicates = new ArrayList<>(2);
      exclude.ifPresent(s -> componentPredicates.add(e -> !s.contains(e)));
      include.ifPresent(s -> componentPredicates.add(s::contains));
      this.bakedPredicate = Util.allOf(componentPredicates);
   }

   @Override
   public MapCodec<CopyComponentsFunction> codec() {
      return MAP_CODEC;
   }

   @Override
   public Set<ContextKey<?>> getReferencedContextParams() {
      return Set.of(this.source.contextParam());
   }

   @Override
   public ItemStack run(final ItemStack itemStack, final LootContext context) {
      DataComponentGetter data = this.source.get(context);
      if (data != null) {
         if (data instanceof DataComponentMap sourceComponents) {
            itemStack.applyComponents(sourceComponents.filter(this.bakedPredicate));
         } else {
            Collection<DataComponentType<?>> exclude = this.exclude.orElse(List.of());
            this.include.map(Collection::stream).orElse(BuiltInRegistries.DATA_COMPONENT_TYPE.listElements().map(Holder::value)).forEach(componentType -> {
               if (!exclude.contains(componentType)) {
                  TypedDataComponent<?> value = data.getTyped(componentType);
                  if (value != null) {
                     itemStack.set(value);
                  }
               }
            });
         }
      }

      return itemStack;
   }

   public static CopyComponentsFunction$Builder copyComponentsFromEntity(final ContextKey<? extends Entity> source) {
      return new CopyComponentsFunction$Builder(new CopyComponentsFunction$DirectSource<>(source));
   }

   public static CopyComponentsFunction$Builder copyComponentsFromBlockEntity(final ContextKey<? extends BlockEntity> source) {
      return new CopyComponentsFunction$Builder(new CopyComponentsFunction$BlockEntitySource(source));
   }
}
