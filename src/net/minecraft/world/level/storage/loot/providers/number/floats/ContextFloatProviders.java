package net.minecraft.world.level.storage.loot.providers.number.floats;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder$Reference;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootPredicates;

public class ContextFloatProviders {
   public static final Codec<ContextFloatProvider> DIRECT_CODEC = Codec.lazyInitialized(
      () -> {
         Codec<ContextFloatProvider> typedCodec = BuiltInRegistries.CONTEXT_FLOAT_PROVIDER_TYPE.byNameCodec().dispatch(ContextFloatProvider::codec, c -> c);
         return Codec.either(ConstantValue.INLINE_CODEC, typedCodec)
            .xmap(Either::unwrap, provider -> provider instanceof ConstantValue constant ? Either.left(constant) : Either.right(provider));
      }
   );
   public static final Codec<Holder<ContextFloatProvider>> CODEC = RegistryCodecs.holder(Registries.CONTEXT_FLOAT_PROVIDER, DIRECT_CODEC);
   public static final Codec<HolderSet<ContextFloatProvider>> LIST_CODEC = RegistryCodecs.holderSet(Registries.CONTEXT_FLOAT_PROVIDER, DIRECT_CODEC);
   public static final ResourceKey<ContextFloatProvider> COOKING_DEFAULT_SPEED_MULTIPLIER = createKey("cooking/speed_default");
   public static final ResourceKey<ContextFloatProvider> COOKING_NORMAL_SPEED_MULTIPLIER = createKey("cooking/normal_speed_multiplier");
   public static final ResourceKey<ContextFloatProvider> COOKING_FAST_SPEED_MULTIPLIER = createKey("cooking/fast_speed_multiplier");
   public static final ResourceKey<ContextFloatProvider> BREWING_DEFAULT_SPEED_MULTIPLIER = createKey("brewing/speed_default");

   private static ResourceKey<ContextFloatProvider> createKey(final String location) {
      return ResourceKey.create(Registries.CONTEXT_FLOAT_PROVIDER, Identifier.withDefaultNamespace(location));
   }

   public static void bootstrap(final BootstrapContext<ContextFloatProvider> context) {
      HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
      HolderGetter<LootItemCondition> predicates = context.lookup(Registries.PREDICATE);
      Holder$Reference<ContextFloatProvider> normalSpeed = context.register(COOKING_NORMAL_SPEED_MULTIPLIER, new ConstantValue(1.0F));
      Holder$Reference<ContextFloatProvider> fastSpeed = context.register(COOKING_FAST_SPEED_MULTIPLIER, new ConstantValue(2.0F));
      context.register(BREWING_DEFAULT_SPEED_MULTIPLIER, new ConstantValue(1.0F));
      Holder<LootItemCondition> fasterCookingBlocks = predicates.getOrThrow(LootPredicates.FAST_FURNACE);
      ConditionalValue cookingSpeed = new ConditionalValue(fasterCookingBlocks, fastSpeed, normalSpeed);
      context.register(COOKING_DEFAULT_SPEED_MULTIPLIER, cookingSpeed);
   }

   public static Holder<ContextFloatProvider> abs(final Holder<ContextFloatProvider> input) {
      return Holder.direct(new Absolute(input));
   }

   @SafeVarargs
   public static Holder<ContextFloatProvider> avg(final Holder<ContextFloatProvider>... inputs) {
      return Holder.direct(new Average(HolderSet.direct(inputs)));
   }

   public static Holder<ContextFloatProvider> ceiling(final Holder<ContextFloatProvider> input) {
      return Holder.direct(new Ceiling(input));
   }

   public static Holder<ContextFloatProvider> exactly(final float value) {
      return Holder.direct(new ConstantValue(value));
   }

   public static Holder<ContextFloatProvider> cos(final Holder<ContextFloatProvider> input) {
      return Holder.direct(new Cosine(input));
   }

   public static Holder<ContextFloatProvider> sub(final Holder<ContextFloatProvider> left, final Holder<ContextFloatProvider> right) {
      return Holder.direct(new Difference(left, right));
   }

   public static Holder<ContextFloatProvider> forEnchantmentLevel(final LevelBasedValue amount) {
      return Holder.direct(new EnchantmentLevelProvider(amount));
   }

   public static Holder<ContextFloatProvider> forEnvironmentAttribute(final EnvironmentAttribute<?> attribute) {
      return Holder.direct(new EnvironmentAttributeValue(attribute));
   }

   @SafeVarargs
   public static Holder<ContextFloatProvider> length(final Holder<ContextFloatProvider>... inputs) {
      return Holder.direct(new Length(HolderSet.direct(inputs)));
   }

   @SafeVarargs
   public static Holder<ContextFloatProvider> max(final Holder<ContextFloatProvider>... inputs) {
      return Holder.direct(new Maximum(HolderSet.direct(inputs)));
   }

   @SafeVarargs
   public static Holder<ContextFloatProvider> min(final Holder<ContextFloatProvider>... inputs) {
      return Holder.direct(new Minimum(HolderSet.direct(inputs)));
   }

   public static Holder<ContextFloatProvider> mod(final Holder<ContextFloatProvider> left, final Holder<ContextFloatProvider> right) {
      return Holder.direct(new Modulus(left, right));
   }

   public static Holder<ContextFloatProvider> negate(final Holder<ContextFloatProvider> input) {
      return Holder.direct(new Negate(input));
   }

   public static Holder<ContextFloatProvider> pow(final Holder<ContextFloatProvider> base, final Holder<ContextFloatProvider> exponent) {
      return Holder.direct(new Power(base, exponent));
   }

   @SafeVarargs
   public static Holder<ContextFloatProvider> mul(final Holder<ContextFloatProvider>... inputs) {
      return Holder.direct(new Product(HolderSet.direct(inputs)));
   }

   public static Holder<ContextFloatProvider> div(final Holder<ContextFloatProvider> left, final Holder<ContextFloatProvider> right) {
      return Holder.direct(new Quotient(left, right));
   }

   public static Holder<ContextFloatProvider> round(final Holder<ContextFloatProvider> input) {
      return Holder.direct(new Round(input));
   }

   public static Holder<ContextFloatProvider> sin(final Holder<ContextFloatProvider> input) {
      return Holder.direct(new Sine(input));
   }

   public static Holder<ContextFloatProvider> sqrt(final Holder<ContextFloatProvider> input) {
      return Holder.direct(new SquareRoot(input));
   }

   @SafeVarargs
   public static Holder<ContextFloatProvider> add(final Holder<ContextFloatProvider>... inputs) {
      return Holder.direct(new Sum(HolderSet.direct(inputs)));
   }

   public static Holder<ContextFloatProvider> trunc(final Holder<ContextFloatProvider> input) {
      return Holder.direct(new Truncate(input));
   }

   public static Holder<ContextFloatProvider> between(final float min, final float max) {
      return Holder.direct(new UniformGenerator(exactly(min), exactly(max)));
   }
}
