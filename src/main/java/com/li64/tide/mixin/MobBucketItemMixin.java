package com.li64.tide.mixin;

import com.li64.tide.data.TideCriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(MobBucketItem.class)
public abstract class MobBucketItemMixin {
    @Shadow @Final private EntityType<?> type;

    @Inject(method = "checkExtraContent", at = @At("HEAD"))
    private void spawn(net.minecraft.world.entity.LivingEntity player, Level level, ItemStack stack, BlockPos pos, CallbackInfo ci) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (BuiltInRegistries.ENTITY_TYPE.getKey(type).getPath().equals("devils_hole_pupfish"))
                TideCriteriaTriggers.RELEASED_PUPFISH.trigger(serverPlayer);
        }
    }
}
