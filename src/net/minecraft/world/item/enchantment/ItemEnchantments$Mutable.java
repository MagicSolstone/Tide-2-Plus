package net.minecraft.world.item.enchantment;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.Holder;

public class ItemEnchantments$Mutable {
   private final Object2IntOpenHashMap<Holder<Enchantment>> enchantments = new Object2IntOpenHashMap();

   public ItemEnchantments$Mutable(final ItemEnchantments enchantments) {
      this.enchantments.putAll(enchantments.enchantments);
   }

   public void set(final Holder<Enchantment> enchantment, final int level) {
      if (level <= 0) {
         this.enchantments.removeInt(enchantment);
      } else {
         this.enchantments.put(enchantment, Math.min(level, 255));
      }
   }

   public void upgrade(final Holder<Enchantment> enchantment, final int level) {
      if (level > 0) {
         this.enchantments.merge(enchantment, Math.min(level, 255), Integer::max);
      }
   }

   public void removeIf(final Predicate<Holder<Enchantment>> predicate) {
      this.enchantments.keySet().removeIf(predicate);
   }

   public int getLevel(final Holder<Enchantment> enchantment) {
      return this.enchantments.getOrDefault(enchantment, 0);
   }

   public Set<Holder<Enchantment>> keySet() {
      return this.enchantments.keySet();
   }

   public ItemEnchantments toImmutable() {
      return new ItemEnchantments(this.enchantments);
   }
}
