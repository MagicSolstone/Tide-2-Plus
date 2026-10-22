package net.minecraft.world.level.storage.loot.parameters;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.context.ContextKey;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.util.context.ContextKeySet$Builder;

public class LootContextParamSets {
   public static final Codec<ContextKeySet> CODEC = BuiltInRegistries.CONTEXT_KEY_SET.byNameCodec();
   public static final ContextKeySet EMPTY = register("empty", var0 -> {});
   public static final ContextKeySet ALL_PARAMS = register(
      "generic",
      builder -> builder.required(LootContextParams.THIS_ENTITY)
         .required(LootContextParams.LAST_DAMAGE_PLAYER)
         .required(LootContextParams.DAMAGE_SOURCE)
         .required(LootContextParams.ATTACKING_ENTITY)
         .required(LootContextParams.DIRECT_ATTACKING_ENTITY)
         .required(LootContextParams.ORIGIN)
         .required(LootContextParams.BLOCK_STATE)
         .required(LootContextParams.BLOCK_ENTITY)
         .required(LootContextParams.TOOL)
         .required(LootContextParams.EXPLOSION_RADIUS)
         .required(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED)
         .required(LootContextParams.CONTAINER)
         .required(LootContextParams.INTERACTING_ENTITY)
         .required(LootContextParams.TARGET_ENTITY)
         .required(LootContextParams.ENCHANTMENT_ACTIVE)
         .required(LootContextParams.ENCHANTMENT_LEVEL)
   );
   public static final ContextKeySet CHEST = register("chest", builder -> builder.required(LootContextParams.ORIGIN).optional(LootContextParams.THIS_ENTITY));
   public static final ContextKeySet COMMAND = register(
      "command", builder -> builder.required(LootContextParams.ORIGIN).optional(LootContextParams.THIS_ENTITY)
   );
   public static final ContextKeySet COMMAND_SLOT_SOURCE = register(
      "command_slot_source",
      builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.CONTAINER).optional(LootContextParams.THIS_ENTITY)
   );
   public static final ContextKeySet COMMAND_COMPUTE_DEFAULT = register(
      "command_compute_default", builder -> builder.optional(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN)
   );
   public static final ContextKeySet COMMAND_COMPUTE_POSITION = register(
      "command_compute_position",
      builder -> builder.optional(LootContextParams.THIS_ENTITY)
         .required(LootContextParams.ORIGIN)
         .required(LootContextParams.BLOCK_STATE)
         .optional(LootContextParams.BLOCK_ENTITY)
   );
   public static final ContextKeySet COMMAND_COMPUTE_ENTITY = register(
      "command_compute_entity",
      builder -> builder.optional(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN).required(LootContextParams.TARGET_ENTITY)
   );
   public static final ContextKeySet SELECTOR = register(
      "selector", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY)
   );
   public static final ContextKeySet VILLAGER_TRADE = register(
      "villager_trade",
      builder -> builder.required(LootContextParams.ORIGIN)
         .required(LootContextParams.THIS_ENTITY)
         .required(LootContextParams.ADDITIONAL_COST_COMPONENT_ALLOWED)
   );
   public static final ContextKeySet FISHING = register(
      "fishing", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.TOOL).optional(LootContextParams.THIS_ENTITY)
   );
   public static final ContextKeySet ENTITY = register(
      "entity",
      builder -> builder.required(LootContextParams.THIS_ENTITY)
         .required(LootContextParams.ORIGIN)
         .required(LootContextParams.DAMAGE_SOURCE)
         .optional(LootContextParams.ATTACKING_ENTITY)
         .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
         .optional(LootContextParams.LAST_DAMAGE_PLAYER)
   );
   public static final ContextKeySet EQUIPMENT = register(
      "equipment", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY)
   );
   public static final ContextKeySet ARCHAEOLOGY = register(
      "archaeology", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY).required(LootContextParams.TOOL)
   );
   public static final ContextKeySet GIFT = register("gift", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY));
   public static final ContextKeySet PIGLIN_BARTER = register("barter", builder -> builder.required(LootContextParams.THIS_ENTITY));
   public static final ContextKeySet VAULT = register(
      "vault", builder -> builder.required(LootContextParams.ORIGIN).optional(LootContextParams.THIS_ENTITY).optional(LootContextParams.TOOL)
   );
   public static final ContextKeySet ADVANCEMENT_REWARD = register(
      "advancement_reward", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN)
   );
   public static final ContextKeySet ADVANCEMENT_ENTITY = register(
      "advancement_entity", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN)
   );
   public static final ContextKeySet ADVANCEMENT_LOCATION = register(
      "advancement_location",
      builder -> builder.required(LootContextParams.THIS_ENTITY)
         .required(LootContextParams.ORIGIN)
         .required(LootContextParams.TOOL)
         .required(LootContextParams.BLOCK_STATE)
         .optional(LootContextParams.BLOCK_ENTITY)
   );
   public static final ContextKeySet BLOCK_USE = register(
      "block_use", builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN).required(LootContextParams.BLOCK_STATE)
   );
   public static final ContextKeySet BLOCK = register(
      "block",
      builder -> builder.required(LootContextParams.BLOCK_STATE)
         .required(LootContextParams.ORIGIN)
         .required(LootContextParams.TOOL)
         .optional(LootContextParams.THIS_ENTITY)
         .optional(LootContextParams.BLOCK_ENTITY)
         .optional(LootContextParams.EXPLOSION_RADIUS)
   );
   public static final ContextKeySet SHEARING = register(
      "shearing", builder -> builder.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY).required(LootContextParams.TOOL)
   );
   public static final ContextKeySet ENTITY_INTERACT = register(
      "entity_interact",
      builder -> builder.required(LootContextParams.TARGET_ENTITY).optional(LootContextParams.INTERACTING_ENTITY).required(LootContextParams.TOOL)
   );
   public static final ContextKeySet BLOCK_INTERACT = register(
      "block_interact",
      builder -> builder.required(LootContextParams.BLOCK_STATE)
         .required(LootContextParams.ORIGIN)
         .optional(LootContextParams.BLOCK_ENTITY)
         .optional(LootContextParams.INTERACTING_ENTITY)
         .optional(LootContextParams.TOOL)
   );
   public static final ContextKeySet CONTAINER_PROCESS = register(
      "container_process",
      builder -> builder.required(LootContextParams.BLOCK_ENTITY)
         .required(LootContextParams.BLOCK_STATE)
         .required(LootContextParams.CONTAINER)
         .required(LootContextParams.ORIGIN)
   );
   public static final ContextKeySet ENCHANTED_DAMAGE = register(
      "enchanted_damage",
      builder -> builder.required(LootContextParams.THIS_ENTITY)
         .required(LootContextParams.ENCHANTMENT_LEVEL)
         .required(LootContextParams.ORIGIN)
         .required(LootContextParams.DAMAGE_SOURCE)
         .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
         .optional(LootContextParams.ATTACKING_ENTITY)
   );
   public static final ContextKeySet ENCHANTED_ITEM = register(
      "enchanted_item", builder -> builder.required(LootContextParams.TOOL).required(LootContextParams.ENCHANTMENT_LEVEL)
   );
   public static final ContextKeySet ENCHANTED_LOCATION = register(
      "enchanted_location",
      builder -> builder.required(LootContextParams.THIS_ENTITY)
         .required(LootContextParams.ENCHANTMENT_LEVEL)
         .required(LootContextParams.ORIGIN)
         .required(LootContextParams.ENCHANTMENT_ACTIVE)
   );
   public static final ContextKeySet ENCHANTED_ENTITY = register(
      "enchanted_entity",
      builder -> builder.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ENCHANTMENT_LEVEL).required(LootContextParams.ORIGIN)
   );
   public static final ContextKeySet HIT_BLOCK = register(
      "hit_block",
      builder -> builder.required(LootContextParams.THIS_ENTITY)
         .required(LootContextParams.ENCHANTMENT_LEVEL)
         .required(LootContextParams.ORIGIN)
         .required(LootContextParams.BLOCK_STATE)
   );

   private static ContextKeySet register(final String name, final Consumer<ContextKeySet$Builder> consumer) {
      ResourceKey<ContextKeySet> key = ResourceKey.create(Registries.CONTEXT_KEY_SET, Identifier.withDefaultNamespace(name));
      return register(consumer, key);
   }

   private static ContextKeySet register(final Consumer<ContextKeySet$Builder> consumer, final ResourceKey<ContextKeySet> key) {
      ContextKeySet$Builder builder = new ContextKeySet$Builder();
      consumer.accept(builder);
      return Registry.register(BuiltInRegistries.CONTEXT_KEY_SET, key, builder.build());
   }

   public static ContextKeySet bootstrap(final Registry<ContextKeySet> registry) {
      return ALL_PARAMS;
   }

   public static void validate() {
      Set<ContextKey<?>> allParams = new HashSet<>();
      BuiltInRegistries.CONTEXT_KEY_SET.forEach(paramSet -> {
         if (paramSet != ALL_PARAMS) {
            allParams.addAll(paramSet.allowed());
         }
      });
      Set<ContextKey<?>> missingFromAllParams = Sets.difference(allParams, ALL_PARAMS.required());
      if (!missingFromAllParams.isEmpty()) {
         throw new IllegalStateException("Missing parameters from 'all_params': " + missingFromAllParams);
      }
   }
}
