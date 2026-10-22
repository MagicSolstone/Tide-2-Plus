package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class ApplyBonusCount extends LootItemConditionalFunction {
   private static final Map<Identifier, ApplyBonusCount$FormulaType> FORMULAS = Stream.of(
         ApplyBonusCount$BinomialWithBonusCount.TYPE, ApplyBonusCount$OreDrops.TYPE, ApplyBonusCount$UniformBonusCount.TYPE
      )
      .collect(Collectors.toMap(ApplyBonusCount$FormulaType::id, Function.identity()));
   private static final Codec<ApplyBonusCount$FormulaType> FORMULA_TYPE_CODEC = Identifier.CODEC.comapFlatMap(location -> {
      ApplyBonusCount$FormulaType type = FORMULAS.get(location);
      return type != null ? DataResult.success(type) : DataResult.error(() -> "No formula type with id: '" + location + "'");
   }, ApplyBonusCount$FormulaType::id);
   private static final MapCodec<ApplyBonusCount$Formula> FORMULA_CODEC = ExtraCodecs.dispatchOptionalValue(
      "formula", "parameters", FORMULA_TYPE_CODEC, ApplyBonusCount$Formula::getType, ApplyBonusCount$FormulaType::codec
   );
   public static final MapCodec<ApplyBonusCount> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> commonFields(i)
         .and(i.group(Enchantment.CODEC.fieldOf("enchantment").forGetter(f -> f.enchantment), FORMULA_CODEC.forGetter(f -> f.formula)))
         .apply(i, ApplyBonusCount::new)
   );
   private final Holder<Enchantment> enchantment;
   private final ApplyBonusCount$Formula formula;

   private ApplyBonusCount(final Optional<Holder<LootItemCondition>> condition, final Holder<Enchantment> enchantment, final ApplyBonusCount$Formula formula) {
      super(condition);
      this.enchantment = enchantment;
      this.formula = formula;
   }

   @Override
   public MapCodec<ApplyBonusCount> codec() {
      return MAP_CODEC;
   }

   @Override
   public Set<ContextKey<?>> getReferencedContextParams() {
      return Set.of(LootContextParams.TOOL);
   }

   @Override
   public ItemStack run(final ItemStack itemStack, final LootContext context) {
      ItemInstance tool = context.getOptional(LootContextParams.TOOL);
      if (tool != null) {
         int level = EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, tool);
         int newCount = this.formula.calculateNewCount(context.getRandom(), itemStack.getCount(), level);
         itemStack.setCount(newCount);
      }

      return itemStack;
   }

   public static LootItemConditionalFunction$Builder<?> addBonusBinomialDistributionCount(
      final Holder<Enchantment> enchantment, final float probability, final int extraRounds
   ) {
      return simpleBuilder(conditions -> new ApplyBonusCount(conditions, enchantment, new ApplyBonusCount$BinomialWithBonusCount(extraRounds, probability)));
   }

   public static LootItemConditionalFunction$Builder<?> addOreBonusCount(final Holder<Enchantment> enchantment) {
      return simpleBuilder(conditions -> new ApplyBonusCount(conditions, enchantment, ApplyBonusCount$OreDrops.INSTANCE));
   }

   public static LootItemConditionalFunction$Builder<?> addUniformBonusCount(final Holder<Enchantment> enchantment) {
      return simpleBuilder(conditions -> new ApplyBonusCount(conditions, enchantment, new ApplyBonusCount$UniformBonusCount(1)));
   }

   public static LootItemConditionalFunction$Builder<?> addUniformBonusCount(final Holder<Enchantment> enchantment, final int bonusMultiplier) {
      return simpleBuilder(conditions -> new ApplyBonusCount(conditions, enchantment, new ApplyBonusCount$UniformBonusCount(bonusMultiplier)));
   }
}
