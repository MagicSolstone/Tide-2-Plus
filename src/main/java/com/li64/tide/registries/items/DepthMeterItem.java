package com.li64.tide.registries.items;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class DepthMeterItem extends AbstractInformationalItem {
    public DepthMeterItem(Properties properties) {
        super(properties, Component.translatable("item.tide.depth_meter.desc"));
    }

    @Override
    public String getResult(ServerLevel level, ServerPlayer player) {
        return Integer.toString(level.getSeaLevel() - player.blockPosition().getY());
    }

    @Override
    public Component parseResult(String result) {
        int depth = Integer.parseInt(result);
        char addition = depth > 0 ? '-' : '+';
        return Component.literal(addition + com.li64.tide.util.TideUtils.formatDepth(Math.abs(depth)));
    }

    @Override
    public int updatePeriod() {
        return 5;
    }
}
