package com.li64.tide.mixin;

import com.li64.tide.Tide;
import com.li64.tide.registries.items.TideFishingRodItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import java.util.function.Function;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Items.class)
public abstract class ItemsMixin {
    /** The vanilla fishing rod becomes a Tide rod (unless the config says not to). */
    @ModifyArg(method = "<clinit>",
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/references/ItemIds;FISHING_ROD:Lnet/minecraft/resources/ResourceKey;")),
            at = @At(value = "INVOKE", ordinal = 0,
                    target = "Lnet/minecraft/world/item/Items;registerItem(Lnet/minecraft/resources/ResourceKey;Ljava/util/function/Function;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;"),
            index = 1)
    private static Function<Item.Properties, Item> tide$fishingRod(Function<Item.Properties, Item> factory) {
        if (Tide.SERVER_CONFIG == null) Tide.setupConfigs();
        if (!Tide.SERVER_CONFIG.general.overrideVanillaRod) return factory;
        return properties -> new TideFishingRodItem(1, 32, properties);
    }
}
