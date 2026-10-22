package net.minecraft.core.component.predicates;

import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.advancements.predicates.EnchantmentPredicate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class EnchantmentsPredicate$StoredEnchantments extends EnchantmentsPredicate {
   public static final Codec<EnchantmentsPredicate$StoredEnchantments> CODEC = codec(EnchantmentsPredicate$StoredEnchantments::new);

   protected EnchantmentsPredicate$StoredEnchantments(final List<EnchantmentPredicate> enchantments) {
      super(enchantments);
   }

   @Override
   public DataComponentType<ItemEnchantments> componentType() {
      return DataComponents.STORED_ENCHANTMENTS;
   }
}
