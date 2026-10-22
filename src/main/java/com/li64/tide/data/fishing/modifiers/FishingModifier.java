package com.li64.tide.data.fishing.modifiers;

import com.li64.tide.data.fishing.FishingContext;
import com.li64.tide.registries.TideRegistries;
import com.mojang.serialization.Codec;

public abstract class FishingModifier {
    public static final Codec<FishingModifier> CODEC = TideRegistries.FISHING_MODIFIERS.byNameCodec()
            .dispatch(FishingModifier::type, FishingModifierType::codec);

    public abstract FishingModifierType<?> type();

    public abstract double apply(double weight, FishingContext context);
}