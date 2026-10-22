package com.li64.tide.data.fishing.mediums;

import com.li64.tide.registries.entities.misc.fishing.TideFishingHook;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;

public interface FishingMedium {
    List<FishingMedium> MEDIUMS = new ArrayList<>();

    WaterMedium WATER = register(new WaterMedium());
    LavaMedium LAVA = register(new LavaMedium());
    VoidMedium VOID = register(new VoidMedium());

    static <T extends FishingMedium> T register(T medium) {
        MEDIUMS.add(medium);
        return medium;
    }

    Identifier id();

    boolean canFishIn(TideFishingHook hook);

    boolean isAt(BlockPos pos, ServerLevel level);

    float getHeight(TideFishingHook hook);

    void drawAmbientSplash(TideFishingHook hook, float angleRad, float distance, double x, double y, double z);

    void drawFishTrail(TideFishingHook hook, float angleRad, float sin, float cos, double fishX, double fishY, double fishZ);

    void onFishBite(TideFishingHook hook);

    default double biteTimeMultiplier() {
        return 1.0;
    }
}