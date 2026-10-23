package com.li64.tide.data.fishing.conditions.types;

import com.li64.tide.data.fishing.FishingContext;
import com.li64.tide.data.fishing.conditions.FishingCondition;
import com.li64.tide.data.fishing.conditions.FishingConditionType;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Arrays;
import java.util.List;

// TODO: add visuals for weather condition in journal
public class WeatherCondition extends FishingCondition {
    public static final MapCodec<WeatherCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            WeatherType.CODEC.listOf().optionalFieldOf("types", List.of()).forGetter(WeatherCondition::getWeatherTypes)
    ).apply(instance, WeatherCondition::new));

    private final List<WeatherType> weatherTypes;

    public static WeatherCondition anyOf(WeatherType... phases) {
        return new WeatherCondition(Arrays.stream(phases).toList());
    }

    public WeatherCondition(List<WeatherType> weatherTypes) {
        this.weatherTypes = weatherTypes;
    }

    public List<WeatherType> getWeatherTypes() {
        return weatherTypes;
    }

    @Override
    public FishingConditionType<?> type() {
        return FishingConditionType.WEATHER;
    }

    @Override
    public boolean test(FishingContext context) {
        if (com.li64.tide.data.rods.Unlocks.has(context, "weather")) return true;
        if (weatherTypes.contains(WeatherType.CLEAR) && !context.level().isRaining()) return true;
        if (weatherTypes.contains(WeatherType.RAIN) && context.level().isRaining()) return true;
        return weatherTypes.contains(WeatherType.STORM) && context.level().isThundering();
    }
}
