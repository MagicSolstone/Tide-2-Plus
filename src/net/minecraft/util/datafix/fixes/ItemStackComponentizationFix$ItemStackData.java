package net.minecraft.util.datafix.fixes;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.OptionalDynamic;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

class ItemStackComponentizationFix$ItemStackData {
   private final String item;
   private final int count;
   private Dynamic<?> components;
   private final Dynamic<?> remainder;
   private Dynamic<?> tag;

   private ItemStackComponentizationFix$ItemStackData(final String item, final int count, final Dynamic<?> remainder) {
      this.item = NamespacedSchema.ensureNamespaced(item);
      this.count = count;
      this.components = remainder.emptyMap();
      this.tag = remainder.get("tag").orElseEmptyMap();
      this.remainder = remainder.remove("tag");
   }

   public static Optional<ItemStackComponentizationFix$ItemStackData> read(final Dynamic<?> dynamic) {
      return dynamic.get("id")
         .asString()
         .apply2stable(
            (item, count) -> new ItemStackComponentizationFix$ItemStackData(item, count.intValue(), dynamic.remove("id").remove("Count")),
            dynamic.get("Count").asNumber()
         )
         .result();
   }

   public OptionalDynamic<?> removeTag(final String key) {
      OptionalDynamic<?> value = this.tag.get(key);
      this.tag = this.tag.remove(key);
      return value;
   }

   public void setComponent(final String type, final Dynamic<?> value) {
      this.components = this.components.set(type, value);
   }

   public void setComponent(final String type, final OptionalDynamic<?> optionalValue) {
      optionalValue.result().ifPresent(value -> this.components = this.components.set(type, value));
   }

   public Dynamic<?> moveTagInto(final String fromKey, final Dynamic<?> target, final String toKey) {
      Optional<? extends Dynamic<?>> value = this.removeTag(fromKey).result();
      return value.isPresent() ? target.set(toKey, value.get()) : target;
   }

   public void moveTagToComponent(final String key, final String type, final Dynamic<?> defaultValue) {
      Optional<? extends Dynamic<?>> value = this.removeTag(key).result();
      if (value.isPresent() && !value.get().equals(defaultValue)) {
         this.setComponent(type, (Dynamic<?>)value.get());
      }
   }

   public void moveTagToComponent(final String key, final String type) {
      this.removeTag(key).result().ifPresent(value -> this.setComponent(type, (Dynamic<?>)value));
   }

   public void fixSubTag(final String key, final boolean dontFixWhenFieldIsMissing, final UnaryOperator<Dynamic<?>> function) {
      OptionalDynamic<?> value = this.tag.get(key);
      if (!dontFixWhenFieldIsMissing || !value.result().isEmpty()) {
         Dynamic<?> map = value.orElseEmptyMap();
         map = function.apply(map);
         if (map.equals(map.emptyMap())) {
            this.tag = this.tag.remove(key);
         } else {
            this.tag = this.tag.set(key, map);
         }
      }
   }

   public Dynamic<?> write() {
      Dynamic<?> result = this.tag.emptyMap().set("id", this.tag.createString(this.item)).set("count", this.tag.createInt(this.count));
      if (!this.tag.equals(this.tag.emptyMap())) {
         this.components = this.components.set("minecraft:custom_data", this.tag);
      }

      if (!this.components.equals(this.tag.emptyMap())) {
         result = result.set("components", this.components);
      }

      return mergeRemainder(result, this.remainder);
   }

   private static <T> Dynamic<T> mergeRemainder(final Dynamic<T> itemStack, final Dynamic<?> remainder) {
      DynamicOps<T> ops = itemStack.getOps();
      return ops.getMap(itemStack.getValue())
         .flatMap(itemStackMap -> ops.mergeToMap(remainder.convert(ops).getValue(), itemStackMap))
         .map(merged -> new Dynamic(ops, merged))
         .result()
         .orElse(itemStack);
   }

   public boolean is(final String id) {
      return this.item.equals(id);
   }

   public boolean is(final Set<String> ids) {
      return ids.contains(this.item);
   }

   public boolean hasComponent(final String id) {
      return this.components.get(id).result().isPresent();
   }
}
