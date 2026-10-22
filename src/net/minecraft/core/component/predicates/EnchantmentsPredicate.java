package net.minecraft.core.component.predicates;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.function.Function;
import net.minecraft.advancements.predicates.EnchantmentPredicate;
import net.minecraft.advancements.predicates.SingleComponentItemPredicate;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public abstract class EnchantmentsPredicate implements SingleComponentItemPredicate<ItemEnchantments> {
   private final List<EnchantmentPredicate> enchantments;

   protected EnchantmentsPredicate(final List<EnchantmentPredicate> enchantments) {
      this.enchantments = enchantments;
   }

   public static <T extends EnchantmentsPredicate> Codec<T> codec(final Function<List<EnchantmentPredicate>, T> constructor) {
      return EnchantmentPredicate.CODEC.listOf().xmap(constructor, EnchantmentsPredicate::enchantments);
   }

   protected List<EnchantmentPredicate> enchantments() {
      return this.enchantments;
   }

   public boolean matches(final ItemEnchantments appliedEnchantments) {
      for (EnchantmentPredicate enchantment : this.enchantments) {
         if (!enchantment.containedIn(appliedEnchantments)) {
            return false;
         }
      }

      return true;
   }

   public static EnchantmentsPredicate$Enchantments enchantments(final List<EnchantmentPredicate> predicates) {
      return new EnchantmentsPredicate$Enchantments(predicates);
   }

   public static EnchantmentsPredicate$StoredEnchantments storedEnchantments(final List<EnchantmentPredicate> predicates) {
      return new EnchantmentsPredicate$StoredEnchantments(predicates);
   }
}
