package com.li64.tide.registries.items;

import com.li64.tide.data.fishing.conditions.types.WeatherType;
import com.li64.tide.registries.TideBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;

import java.util.List;
import java.util.function.Consumer;

public class WeatherRadioItem extends BlockItem implements InformationalItem, TooltipItem {
    public WeatherRadioItem(Properties properties) {
        super(TideBlocks.WEATHER_RADIO, properties);
    }

    @Override
    public void addTooltip(ItemStack stack, Consumer<Component> tooltip) {
        Style gray = Component.empty().getStyle().withColor(ChatFormatting.GRAY);
        tooltip.accept(Component.translatable("item.tide.weather_radio.desc").setStyle(gray));
    }

    @Override
    public String getResult(ServerLevel level, ServerPlayer player) {
        net.minecraft.world.level.saveddata.WeatherData data = level.getWeatherData();

        int clearTime = data.getClearWeatherTime();
        int rainTime = data.getRainTime();
        int thunderTime = data.getThunderTime();

        boolean isRaining = data.isRaining();
        boolean isThundering = data.isThundering();

        WeatherType next;
        int nextTicks;

        if (clearTime > 0) {
            next = WeatherType.STORM;
            nextTicks = clearTime;
        }
        else if ((thunderTime < rainTime && isRaining)
                || (thunderTime == rainTime && !isRaining)) {
            next = isThundering ? WeatherType.RAIN : WeatherType.STORM;
            nextTicks = thunderTime;
        }
        else {
            next = isRaining ? WeatherType.CLEAR : WeatherType.RAIN;
            nextTicks = rainTime;
        }

        return next.ordinal() + "-" + nextTicks;
    }

    @Override
    public Component parseResult(String result) {
        String[] splits = result.split("-");
        if (splits.length != 2) return Component.literal("Couldn't get weather info...");

        WeatherType next = WeatherType.values()[Integer.parseInt(splits[0])];
        int nextTicks = Integer.parseInt(splits[1]);

        int seconds = Mth.positiveCeilDiv(nextTicks, 20);
        int minutes = Mth.positiveCeilDiv(seconds, 60);
        int hours = Mth.positiveCeilDiv(minutes, 60);

        Component weatherForecast = Component.translatable("journal.info.weather." + next.getSerializedName());

        Component timeForecast = Component.translatable("item.tide.weather_radio.forecast.hours", hours);
        if (minutes <= 1) timeForecast = Component.translatable("item.tide.weather_radio.forecast.seconds", seconds);
        else if (hours <= 1) timeForecast = Component.translatable("item.tide.weather_radio.forecast.minutes", minutes);

        return Component.translatable("item.tide.weather_radio.forecast", weatherForecast, timeForecast);
    }

    @Override
    public int updatePeriod() {
        return 20;
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> lines, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, lines, flag);
        this.addTooltip(stack, lines);
    }
}
