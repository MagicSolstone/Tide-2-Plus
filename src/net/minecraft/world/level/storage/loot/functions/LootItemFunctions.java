package net.minecraft.world.level.storage.loot.functions;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;

public class LootItemFunctions {
   public static final Codec<LootItemFunction> TYPED_CODEC = BuiltInRegistries.LOOT_FUNCTION_TYPE.byNameCodec().dispatch(LootItemFunction::codec, c -> c);
   public static final Codec<LootItemFunction> DIRECT_CODEC = Codec.lazyInitialized(
      () -> Codec.either(TYPED_CODEC, SequenceFunction.INLINE_CODEC)
         .xmap(
            typedOrList -> (LootItemFunction)typedOrList.map(f -> f, f -> f),
            function -> function instanceof SequenceFunction sequence && sequence.canUseInlineCodec() ? Either.right(sequence) : Either.left(function)
         )
   );
   public static final Codec<Holder<LootItemFunction>> CODEC = RegistryCodecs.holder(Registries.ITEM_MODIFIER, DIRECT_CODEC);
   public static final Codec<HolderSet<LootItemFunction>> LIST_CODEC = RegistryCodecs.holderSet(Registries.ITEM_MODIFIER, TYPED_CODEC);

   public static MapCodec<? extends LootItemFunction> bootstrap(final Registry<MapCodec<? extends LootItemFunction>> registry) {
      Registry.register(registry, "set_count", SetItemCountFunction.MAP_CODEC);
      Registry.register(registry, "set_item", SetItemFunction.MAP_CODEC);
      Registry.register(registry, "enchant_with_levels", EnchantWithLevelsFunction.MAP_CODEC);
      Registry.register(registry, "enchant_randomly", EnchantRandomlyFunction.MAP_CODEC);
      Registry.register(registry, "set_enchantments", SetEnchantmentsFunction.MAP_CODEC);
      Registry.register(registry, "set_custom_data", SetCustomDataFunction.MAP_CODEC);
      Registry.register(registry, "set_components", SetComponentsFunction.MAP_CODEC);
      Registry.register(registry, "furnace_smelt", SmeltItemFunction.MAP_CODEC);
      Registry.register(registry, "enchanted_count_increase", EnchantedCountIncreaseFunction.MAP_CODEC);
      Registry.register(registry, "set_damage", SetItemDamageFunction.MAP_CODEC);
      Registry.register(registry, "set_attributes", SetAttributesFunction.MAP_CODEC);
      Registry.register(registry, "set_name", SetNameFunction.MAP_CODEC);
      Registry.register(registry, "exploration_map", ExplorationMapFunction.MAP_CODEC);
      Registry.register(registry, "set_stew_effect", SetStewEffectFunction.MAP_CODEC);
      Registry.register(registry, "copy_name", CopyNameFunction.MAP_CODEC);
      Registry.register(registry, "set_contents", SetContainerContents.MAP_CODEC);
      Registry.register(registry, "modify_contents", ModifyContainerContents.MAP_CODEC);
      Registry.register(registry, "filtered", FilteredFunction.MAP_CODEC);
      Registry.register(registry, "limit_count", LimitCount.MAP_CODEC);
      Registry.register(registry, "apply_bonus", ApplyBonusCount.MAP_CODEC);
      Registry.register(registry, "set_loot_table", SetContainerLootTable.MAP_CODEC);
      Registry.register(registry, "explosion_decay", ApplyExplosionDecay.MAP_CODEC);
      Registry.register(registry, "set_lore", SetLoreFunction.MAP_CODEC);
      Registry.register(registry, "fill_player_head", FillPlayerHead.MAP_CODEC);
      Registry.register(registry, "copy_custom_data", CopyCustomDataFunction.MAP_CODEC);
      Registry.register(registry, "copy_state", CopyBlockState.MAP_CODEC);
      Registry.register(registry, "set_banner_pattern", SetBannerPatternFunction.MAP_CODEC);
      Registry.register(registry, "set_potion", SetPotionFunction.MAP_CODEC);
      Registry.register(registry, "set_random_dyes", SetRandomDyesFunction.MAP_CODEC);
      Registry.register(registry, "set_random_potion", SetRandomPotionFunction.MAP_CODEC);
      Registry.register(registry, "set_instrument", SetInstrumentFunction.MAP_CODEC);
      Registry.register(registry, "sequence", SequenceFunction.MAP_CODEC);
      Registry.register(registry, "copy_components", CopyComponentsFunction.MAP_CODEC);
      Registry.register(registry, "set_fireworks", SetFireworksFunction.MAP_CODEC);
      Registry.register(registry, "set_firework_explosion", SetFireworkExplosionFunction.MAP_CODEC);
      Registry.register(registry, "set_book_cover", SetBookCoverFunction.MAP_CODEC);
      Registry.register(registry, "set_written_book_pages", SetWrittenBookPagesFunction.MAP_CODEC);
      Registry.register(registry, "set_writable_book_pages", SetWritableBookPagesFunction.MAP_CODEC);
      Registry.register(registry, "toggle_tooltips", ToggleTooltips.MAP_CODEC);
      Registry.register(registry, "set_ominous_bottle_amplifier", SetOminousBottleAmplifierFunction.MAP_CODEC);
      Registry.register(registry, "set_custom_model_data", SetCustomModelDataFunction.MAP_CODEC);
      return Registry.register(registry, "discard", DiscardItem.MAP_CODEC);
   }
}
