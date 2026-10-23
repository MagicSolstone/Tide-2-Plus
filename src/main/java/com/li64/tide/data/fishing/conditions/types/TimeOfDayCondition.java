package com.li64.tide.data.fishing.conditions.types;

import com.li64.tide.data.fishing.FishingContext;
import com.li64.tide.data.fishing.conditions.FishingCondition;
import com.li64.tide.data.fishing.conditions.FishingConditionType;
import com.li64.tide.registries.TideItems;
import com.li64.tide.util.TideUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public class TimeOfDayCondition extends FishingCondition {
    public static final MapCodec<TimeOfDayCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TimeRange.CODEC.listOf().fieldOf("ranges").forGetter(TimeOfDayCondition::getRanges)
    ).apply(instance, TimeOfDayCondition::new));

    private final List<TimeRange> ranges;

    public TimeOfDayCondition(List<TimeRange> ranges) {
        this.ranges = ranges;
    }

    public List<TimeRange> getRanges() {
        return ranges;
    }

    @Override
    public FishingConditionType<?> type() {
        return FishingConditionType.TIME_OF_DAY;
    }

    @Override
    public boolean test(FishingContext context) {
        if (com.li64.tide.data.rods.Unlocks.has(context, "time")) return true;
        long time = TideUtils.getTimeOfDay(context.level());
        if (context.hook() != null && context.hook().getHook().is(TideItems.TWILIGHT_HOOK)) {
            return ranges.stream().anyMatch(tr -> tr.contains((time + 12000) % 24000));
        }
        return ranges.stream().anyMatch(tr -> tr.contains(time));
    }
}
