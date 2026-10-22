package net.minecraft.data.advancements;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.worldgen.BootstrapContext;

@FunctionalInterface
public interface AdvancementSubProvider$Factory {
   AdvancementSubProvider create(BootstrapContext<Advancement> output);
}
