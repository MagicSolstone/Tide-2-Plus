package com.li64.tide.data.fishing.conditions.types;

import com.li64.tide.data.TideTags;
import com.li64.tide.data.fishing.FishingContext;
import com.li64.tide.data.fishing.conditions.FishingCondition;
import com.li64.tide.data.fishing.conditions.FishingConditionType;
import com.mojang.serialization.MapCodec;

public class FreshwaterCondition extends FishingCondition {
    public static final MapCodec<FreshwaterCondition> CODEC = MapCodec.unit(FreshwaterCondition::new);

    public FreshwaterCondition() {}

    @Override
    public FishingConditionType<?> type() {
        return FishingConditionType.FRESHWATER;
    }

    @Override
    public boolean test(FishingContext context) {
        return !context.exactBiome().is(TideTags.Biomes.IS_SALTWATER) || com.li64.tide.data.rods.Unlocks.has(context, "water");
    }
}
