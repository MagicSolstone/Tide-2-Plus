package com.li64.tide.mixin;

import com.li64.tide.registries.TideItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(BeehiveBlock.class)
public class BeehiveBlockMixin {
    @Inject(method = "dropHoneycomb", at = @At("TAIL"))
    private static void dropHoneycomb(net.minecraft.server.level.ServerLevel level, ItemStack tool,
                                      net.minecraft.world.level.block.state.BlockState state,
                                      net.minecraft.world.level.block.entity.BlockEntity blockEntity,
                                      net.minecraft.world.entity.Entity entity, BlockPos pos, CallbackInfo ci) {
        if (new Random().nextFloat() < 0.05f) BeehiveBlock.popResource(
                level, pos, new ItemStack(TideItems.HONEYCOMB_FISHING_ROD));
    }
}
