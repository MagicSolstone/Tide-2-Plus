package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderLookup$Provider;
import net.minecraft.core.HolderLookup$RegistryLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.predicates.DataComponentPredicate$Type;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

class ItemPredicateArgument$Context
   implements ComponentPredicateParser$Context<Predicate<ItemStack>, ItemPredicateArgument$ComponentWrapper, ItemPredicateArgument$PredicateWrapper> {
   private final HolderLookup$Provider registries;
   private final HolderLookup$RegistryLookup<Item> items;
   private final HolderLookup$RegistryLookup<DataComponentType<?>> components;
   private final HolderLookup$RegistryLookup<DataComponentPredicate$Type<?>> predicates;

   private ItemPredicateArgument$Context(final HolderLookup$Provider registries) {
      this.registries = registries;
      this.items = registries.lookupOrThrow(Registries.ITEM);
      this.components = registries.lookupOrThrow(Registries.DATA_COMPONENT_TYPE);
      this.predicates = registries.lookupOrThrow(Registries.DATA_COMPONENT_PREDICATE_TYPE);
   }

   public Predicate<ItemStack> forElementType(final ImmutableStringReader reader, final Identifier id) throws CommandSyntaxException {
      Holder$Reference<Item> item = this.items
         .get(ResourceKey.create(Registries.ITEM, id))
         .orElseThrow(() -> ItemPredicateArgument.ERROR_UNKNOWN_ITEM.createWithContext(reader, id));
      return itemStack -> itemStack.is(item);
   }

   public Predicate<ItemStack> forTagType(final ImmutableStringReader reader, final Identifier id) throws CommandSyntaxException {
      HolderSet<Item> tag = this.items
         .get(TagKey.create(Registries.ITEM, id))
         .orElseThrow(() -> ItemPredicateArgument.ERROR_UNKNOWN_TAG.createWithContext(reader, id));
      return itemStack -> itemStack.is(tag);
   }

   public ItemPredicateArgument$ComponentWrapper lookupComponentType(final ImmutableStringReader reader, final Identifier componentId) throws CommandSyntaxException {
      ItemPredicateArgument$ComponentWrapper wrapper = ItemPredicateArgument.PSEUDO_COMPONENTS.get(componentId);
      if (wrapper != null) {
         return wrapper;
      } else {
         DataComponentType<?> componentType = this.components
            .get(ResourceKey.create(Registries.DATA_COMPONENT_TYPE, componentId))
            .map(Holder::value)
            .orElseThrow(() -> ItemPredicateArgument.ERROR_UNKNOWN_COMPONENT.createWithContext(reader, componentId));
         return ItemPredicateArgument$ComponentWrapper.create(reader, componentId, componentType);
      }
   }

   public Predicate<ItemStack> createComponentTest(
      final ImmutableStringReader reader, final ItemPredicateArgument$ComponentWrapper componentType, final Dynamic<?> value
   ) throws CommandSyntaxException {
      return componentType.decode(reader, RegistryOps.injectRegistryContext(value, this.registries));
   }

   public Predicate<ItemStack> createComponentTest(final ImmutableStringReader reader, final ItemPredicateArgument$ComponentWrapper componentType) {
      return componentType.presenceChecker;
   }

   public ItemPredicateArgument$PredicateWrapper lookupPredicateType(final ImmutableStringReader reader, final Identifier componentId) throws CommandSyntaxException {
      ItemPredicateArgument$PredicateWrapper wrapper = ItemPredicateArgument.PSEUDO_PREDICATES.get(componentId);
      return wrapper != null
         ? wrapper
         : this.predicates
            .get(ResourceKey.create(Registries.DATA_COMPONENT_PREDICATE_TYPE, componentId))
            .map(ItemPredicateArgument$PredicateWrapper::new)
            .or(
               () -> this.components
                  .get(ResourceKey.create(Registries.DATA_COMPONENT_TYPE, componentId))
                  .map(ItemPredicateArgument::createComponentExistencePredicate)
            )
            .orElseThrow(() -> ItemPredicateArgument.ERROR_UNKNOWN_PREDICATE.createWithContext(reader, componentId));
   }

   public Predicate<ItemStack> createPredicateTest(
      final ImmutableStringReader reader, final ItemPredicateArgument$PredicateWrapper predicateType, final Dynamic<?> value
   ) throws CommandSyntaxException {
      return predicateType.decode(reader, RegistryOps.injectRegistryContext(value, this.registries));
   }

   @Override
   public Stream<Identifier> listElementTypes() {
      return this.items.listElementIds().map(ResourceKey::identifier);
   }

   @Override
   public Stream<Identifier> listTagTypes() {
      return this.items.listTagIds().map(TagKey::location);
   }

   @Override
   public Stream<Identifier> listComponentTypes() {
      return Stream.concat(
         ItemPredicateArgument.PSEUDO_COMPONENTS.keySet().stream(),
         this.components.listElements().filter(e -> !e.value().isTransient()).map(e -> e.key().identifier())
      );
   }

   @Override
   public Stream<Identifier> listPredicateTypes() {
      return Stream.concat(ItemPredicateArgument.PSEUDO_PREDICATES.keySet().stream(), this.predicates.listElementIds().map(ResourceKey::identifier));
   }

   public Predicate<ItemStack> negate(final Predicate<ItemStack> value) {
      return value.negate();
   }

   public Predicate<ItemStack> anyOf(final List<Predicate<ItemStack>> alternatives) {
      return Util.anyOf(alternatives);
   }
}
