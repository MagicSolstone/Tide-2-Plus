package net.minecraft.gametest.framework;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

record GameTestBatchFactory$BatchKey(Holder<TestEnvironmentDefinition<?>> environment, ResourceKey<Level> dimension) {
}
