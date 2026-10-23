package com.li64.tide.data.fishing.conditions.types;

import com.li64.tide.data.TideTags;
import com.li64.tide.data.fishing.FishingContext;
import com.li64.tide.data.fishing.conditions.FishingCondition;
import com.li64.tide.data.fishing.conditions.FishingConditionType;
import com.mojang.serialization.MapCodec;

public class SaltwaterCondition extends FishingCondition {
    public static final MapCodec<SaltwaterCondition> CODEC = MapCodec.unit(SaltwaterCondition::new);

    public SaltwaterCondition() {}

    @Override
    public FishingConditionType<?> type() {
        return FishingConditionType.SALTWATER;
    }

    @Override
    public boolean test(FishingContext context) {
        return context.exactBiome().is(TideTags.Biomes.IS_SALTWATER) || com.li64.tide.data.rods.Unlocks.has(context, "water");
    }
}
