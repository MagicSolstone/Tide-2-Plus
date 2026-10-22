package com.li64.tide.mixin;

import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Consumer;

/** Lets fishing loot tables also see the block state (what the hook is in). */
@Mixin(LootContextParamSets.class)
public class LootContextParamSetsMixin {
    @ModifyVariable(method = "register(Ljava/lang/String;Ljava/util/function/Consumer;)Lnet/minecraft/util/context/ContextKeySet;",
            at = @At("HEAD"), argsOnly = true)
    private static Consumer<ContextKeySet.Builder> tide$fishingParams(Consumer<ContextKeySet.Builder> consumer, String name) {
        if (!name.equals("fishing")) return consumer;
        return builder -> builder.required(LootContextParams.ORIGIN)
                .required(LootContextParams.TOOL)
                .optional(LootContextParams.THIS_ENTITY)
                .optional(LootContextParams.BLOCK_STATE);
    }
}
