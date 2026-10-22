package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
import net.minecraft.world.level.storage.loot.ContainerComponentManipulators;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetContainerContents extends LootItemConditionalFunction {
   public static final MapCodec<SetContainerContents> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> commonFields(i)
         .and(
            i.group(
               ContainerComponentManipulators.CODEC.fieldOf("component").forGetter(f -> f.component),
               LootPoolEntries.CODEC.listOf().fieldOf("entries").forGetter(f -> f.entries)
            )
         )
         .apply(i, SetContainerContents::new)
   );
   private final ContainerComponentManipulator<?> component;
   private final List<LootPoolEntryContainer> entries;

   private SetContainerContents(
      final Optional<Holder<LootItemCondition>> condition, final ContainerComponentManipulator<?> component, final List<LootPoolEntryContainer> entries
   ) {
      super(condition);
      this.component = component;
      this.entries = List.copyOf(entries);
   }

   @Override
   public MapCodec<SetContainerContents> codec() {
      return MAP_CODEC;
   }

   @Override
   public ItemStack run(final ItemStack itemStack, final LootContext context) {
      if (itemStack.isEmpty()) {
         return itemStack;
      } else {
         Stream.Builder<ItemStack> contents = Stream.builder();
         this.entries
            .forEach(e -> e.expand(context, entry -> entry.createItemStack(LootTable.createStackSplitter(context.getLevel(), contents::add), context)));
         this.component.setContents(itemStack, contents.build());
         return itemStack;
      }
   }

   @Override
   public void validate(final ValidationContext context) {
      super.validate(context);
      Validatable.validate(context, "entries", this.entries);
   }

   public static SetContainerContents$Builder setContents(final ContainerComponentManipulator<?> component) {
      return new SetContainerContents$Builder(component);
   }
}
