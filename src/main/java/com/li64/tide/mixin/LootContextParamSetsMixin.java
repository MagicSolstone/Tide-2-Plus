package com.li64.tide.mixin;

import com.google.common.collect.BiMap;
import com.li64.tide.Tide;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(LootContextParamSets.class)
public class LootContextParamSetsMixin {
    @Shadow @Final private static BiMap<Identifier, LootContextParamSet> REGISTRY;

    @Inject(at = @At(value = "HEAD"), method = "register", cancellable = true)
    private static void register(String registryName, Consumer<LootContextParamSet.Builder> builderConsumer, CallbackInfoReturnable<LootContextParamSet> cir) {
        if (registryName.matches("fishing")) {
            builderConsumer = builder -> {
                builder.required(LootContextParams.ORIGIN)
                        .required(LootContextParams.TOOL)
                        .optional(LootContextParams.THIS_ENTITY)
                        .optional(LootContextParams.BLOCK_STATE); // This entire mixin is just to add this line :(
            };
            LootContextParamSet.Builder builder = new LootContextParamSet.Builder();
            builderConsumer.accept(builder);
            LootContextParamSet paramSet = builder.build();
            Identifier registry = Tide.resource("minecraft", registryName);
            LootContextParamSet newParamSet = REGISTRY.put(registry, paramSet);
            if (newParamSet != null) {
                throw new IllegalStateException("Loot table parameter set " + registry + " is already registered");
            } else {
                cir.setReturnValue(paramSet);
            }
        }
    }
}