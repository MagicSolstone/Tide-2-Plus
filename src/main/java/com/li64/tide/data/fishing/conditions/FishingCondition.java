package com.li64.tide.data.fishing.conditions;

import com.li64.tide.data.fishing.FishingContext;
import com.li64.tide.registries.TideRegistries;
import com.mojang.serialization.Codec;

public abstract class FishingCondition {
    public static final Codec<FishingCondition> CODEC = TideRegistries.FISHING_CONDITIONS.byNameCodec()
            .dispatch(FishingCondition::type, FishingConditionType::codec);

    public abstract FishingConditionType<?> type();

    public abstract boolean test(FishingContext context);
}