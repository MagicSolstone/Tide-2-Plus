package com.li64.tide.data.fishing.mediums;

import com.li64.tide.Tide;
import com.li64.tide.registries.entities.misc.fishing.TideFishingHook;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class WaterMedium implements FishingMedium {
    private final Identifier id = Tide.resource("water");

    @Override
    public Identifier id() {
        return id;
    }

    @Override
    public boolean isAt(BlockPos pos, ServerLevel level) {
        return level.getFluidState(pos).is(FluidTags.WATER);
    }

    @Override
    public boolean canFishIn(TideFishingHook hook) {
        return hook.getFluidState().is(FluidTags.WATER);
    }

    @Override
    public float getHeight(TideFishingHook hook) {
        return hook.getFluidState().getHeight(hook.level(), hook.blockPosition());
    }

    @Override
    public void drawAmbientSplash(TideFishingHook hook, float angleRad, float distance, double x, double y, double z) {
        if (!(hook.level() instanceof ServerLevel level)) return;

        BlockState state = level.getBlockState(BlockPos.containing(x, y - 1.0, z));
        if (!state.getFluidState().is(FluidTags.WATER)) return;

        level.sendParticles(ParticleTypes.SPLASH, x, y, z,2 + hook.getRandom().nextInt(2),
                0.1, 0.0, 0.1, 0.0);
    }

    @Override
    public void drawFishTrail(TideFishingHook hook, float angleRad, float sin, float cos, double fishX, double fishY, double fishZ) {
        if (!(hook.level() instanceof ServerLevel level)) return;

        RandomSource random = hook.getRandom();
        BlockState state = level.getBlockState(BlockPos.containing(fishX, fishY - 1.0, fishZ));

        if (state.getFluidState().is(FluidTags.WATER)) {
            if (random.nextFloat() < 0.15f) level.sendParticles(ParticleTypes.BUBBLE,
                    fishX, fishY - 0.1, fishZ, 1, sin, 0.1, cos, 0.0);

            float scaledSin = sin * 0.04f;
            float scaledCos = cos * 0.04f;
            level.sendParticles(ParticleTypes.FISHING, fishX, fishY, fishZ, 0, scaledCos, 0.01, -scaledSin, 1.0);
            level.sendParticles(ParticleTypes.FISHING, fishX, fishY, fishZ, 0, -scaledCos, 0.01, scaledSin, 1.0);
        }
    }

    @Override
    public void onFishBite(TideFishingHook hook) {
        if (!(hook.level() instanceof ServerLevel level)) return;

        RandomSource random = hook.getRandom();
        hook.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25f, 1.0f + (random.nextFloat() - random.nextFloat()) * 0.4f);

        double y = hook.getY() + 0.5;
        level.sendParticles(ParticleTypes.BUBBLE, hook.getX(), y, hook.getZ(), (int) (1.0f + hook.getBbWidth() * 20.0f), hook.getBbWidth(), 0.0, hook.getBbWidth(), 0.2);
        level.sendParticles(ParticleTypes.FISHING, hook.getX(), y, hook.getZ(), (int) (1.0f + hook.getBbWidth() * 20.0f), hook.getBbWidth(), 0.0, hook.getBbWidth(), 0.2);
    }
}
