package com.li64.tide.data.fishing.conditions.types;

import com.li64.tide.data.fishing.FishingContext;
import com.li64.tide.data.fishing.conditions.FishingCondition;
import com.li64.tide.data.fishing.conditions.FishingConditionType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Arrays;
import java.util.List;

public class MoonPhaseCondition extends FishingCondition {
    public static final MapCodec<MoonPhaseCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.listOf().optionalFieldOf("any_of", List.of()).forGetter(MoonPhaseCondition::getPhases)
    ).apply(instance, MoonPhaseCondition::new));

    private final List<Integer> phases;

    public static MoonPhaseCondition anyOf(int... phases) {
        return new MoonPhaseCondition(Arrays.stream(phases).boxed().toList());
    }

    public MoonPhaseCondition(List<Integer> phases) {
        this.phases = phases;
    }

    public List<Integer> getPhases() {
        return phases;
    }

    @Override
    public FishingConditionType<?> type() {
        return FishingConditionType.MOON_PHASE;
    }

    @Override
    public boolean test(FishingContext context) {
        return phases.contains(context.moonPhase()) || com.li64.tide.data.rods.Unlocks.has(context, "moon");
    }
}
