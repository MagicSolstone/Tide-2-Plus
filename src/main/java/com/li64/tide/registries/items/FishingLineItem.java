package com.li64.tide.registries.items;

import com.li64.tide.data.rods.AccessoryData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class FishingLineItem extends AbstractTooltipItem {
    private final String description;

    public FishingLineItem(Properties properties) {
        this(properties, "");
    }

    public FishingLineItem(Properties properties, String description) {
        super(properties);
        this.description = description;
    }

    public static String getColor(ItemStack stack) {
        AccessoryData data = AccessoryData.get(stack);
        if (data == null || data.color().isEmpty()) return "#d6d6d6";
        return data.color().get();
    }

    /** A glowing line (slime) stays bright in the dark instead of dimming with the light. */
    public static boolean glows(ItemStack stack) {
        AccessoryData data = AccessoryData.get(stack);
        return data != null && data.glow().orElse(false);
    }

    /** How thick the line draws (1 = normal). */
    public static float width(ItemStack stack) {
        AccessoryData data = AccessoryData.get(stack);
        return data == null ? 1f : data.width().orElse(1f);
    }

    /** The particle a line gives off while cast, if any (simple particles only, like "minecraft:wax_on"). */
    public static net.minecraft.core.particles.ParticleOptions particle(ItemStack stack) {
        AccessoryData data = AccessoryData.get(stack);
        if (data == null || data.particle().isEmpty()) return null;
        net.minecraft.resources.Identifier id = net.minecraft.resources.Identifier.tryParse(data.particle().get());
        if (id == null) return null;
        return net.minecraft.core.registries.BuiltInRegistries.PARTICLE_TYPE.getOptional(id)
                .filter(t -> t instanceof net.minecraft.core.particles.SimpleParticleType)
                .map(t -> (net.minecraft.core.particles.ParticleOptions) t).orElse(null);
    }

    @Override
    public void addTooltip(ItemStack stack, Consumer<Component> tooltip) {
        if (description.isEmpty()) return;
        Style gray = Component.empty().getStyle().withColor(ChatFormatting.GRAY);
        Style blue = Component.empty().getStyle().withColor(ChatFormatting.BLUE);
        tooltip.accept(Component.empty());
        tooltip.accept(Component.translatable("text.tide.accessory_tooltip.prefix").setStyle(gray));
        tooltip.accept(Component.translatable(description).setStyle(blue));
    }
}
