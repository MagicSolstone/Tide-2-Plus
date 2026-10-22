package com.li64.tide.data.fishing.modifiers;

import com.li64.tide.Tide;
import com.li64.tide.data.fishing.modifiers.types.ConditionalModifier;
import com.li64.tide.data.fishing.modifiers.types.TemperatureModifier;
import com.li64.tide.registries.TideRegistries;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public interface FishingModifierType<T extends FishingModifier> {
    FishingModifierType<ConditionalModifier> CONDITIONAL = register("conditional", ConditionalModifier.CODEC);
    FishingModifierType<TemperatureModifier> TEMPERATURE = register("temperature", TemperatureModifier.CODEC);

    static void register() {
        Tide.LOG.info("Registering fishing modifiers");
    }

    MapCodec<T> codec();

    static <T extends FishingModifier> FishingModifierType<T> register(String name, MapCodec<T> codec) {
        return register(Tide.resource(name), codec);
    }

    static <T extends FishingModifier> FishingModifierType<T> register(Identifier name, MapCodec<T> codec) {
        return Registry.register(TideRegistries.FISHING_MODIFIERS, name, () -> codec);
    }
}