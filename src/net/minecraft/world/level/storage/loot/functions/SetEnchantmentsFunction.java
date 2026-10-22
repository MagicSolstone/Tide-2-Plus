package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public class SetEnchantmentsFunction extends LootItemConditionalFunction {
   public static final MapCodec<SetEnchantmentsFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> commonFields(i)
         .and(
            i.group(
               Codec.unboundedMap(Enchantment.CODEC, ContextIntProviders.CODEC).optionalFieldOf("enchantments", Map.of()).forGetter(f -> f.enchantments),
               Codec.BOOL.optionalFieldOf("add", false).forGetter(f -> f.add)
            )
         )
         .apply(i, SetEnchantmentsFunction::new)
   );
   private final Map<Holder<Enchantment>, Holder<ContextIntProvider>> enchantments;
   private final boolean add;

   private SetEnchantmentsFunction(
      final Optional<Holder<LootItemCondition>> condition, final Map<Holder<Enchantment>, Holder<ContextIntProvider>> enchantments, final boolean add
   ) {
      super(condition);
      this.enchantments = Map.copyOf(enchantments);
      this.add = add;
   }

   @Override
   public MapCodec<SetEnchantmentsFunction> codec() {
      return MAP_CODEC;
   }

   @Override
   public void validate(final ValidationContext context) {
      super.validate(context);
      this.enchantments.forEach((enchantment, holder) -> holder.value().validate(context.forMapField("enchantments", enchantment.getRegisteredName())));
   }

   @Override
   public ItemStack run(ItemStack itemStack, final LootContext context) {
      if (itemStack.is(Items.BOOK)) {
         itemStack = itemStack.transmuteCopy(Items.ENCHANTED_BOOK);
      }

      EnchantmentHelper.updateEnchantments(
         itemStack,
         enchantments -> {
            if (this.add) {
               this.enchantments
                  .forEach(
                     (enchantment, levelProvider) -> enchantments.set(
                        (Holder<Enchantment>)enchantment,
                        Mth.clamp(enchantments.getLevel((Holder<Enchantment>)enchantment) + levelProvider.value().getInt(context), 0, 255)
                     )
                  );
            } else {
               this.enchantments
                  .forEach(
                     (enchantment, levelProvider) -> enchantments.set(
                        (Holder<Enchantment>)enchantment, Mth.clamp(levelProvider.value().getInt(context), 0, 255)
                     )
                  );
            }
         }
      );
      return itemStack;
   }
}
