package com.li64.tide.data.loot;

import com.li64.tide.Tide;
import com.li64.tide.config.TideServerConfig;
import com.li64.tide.data.FishLengthHolder;
import com.li64.tide.data.fishing.FishData;
import com.li64.tide.data.item.TideItemData;
import com.li64.tide.registries.TideLootFunctions;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ApplyFishEntityLengthFunction implements LootItemFunction {
    public static final MapCodec<ApplyFishEntityLengthFunction> CODEC = MapCodec.unit(ApplyFishEntityLengthFunction::new);

    public ApplyFishEntityLengthFunction() {}

    @Override
    public @NotNull MapCodec<ApplyFishEntityLengthFunction> codec() {
        return CODEC;
    }

    @Override
    public @NotNull Set<ContextKey<?>> getReferencedContextParams() {
        return Set.of(LootContextParams.THIS_ENTITY, LootContextParams.LAST_DAMAGE_PLAYER);
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext context) {
        if (Tide.SERVER_CONFIG.items.fishItemSizes != TideServerConfig.Items.SizeMode.ALWAYS
                || FishData.get(stack).map(FishData::size).isEmpty()) return stack;
        Entity entity = context.getOptional(LootContextParams.THIS_ENTITY);
        if (entity == null) return stack;
        if (entity instanceof FishLengthHolder lengthHolder) {
            TideItemData.FISH_LENGTH.set(stack, lengthHolder.tide$getLength());
            lengthHolder.tide$applyExtras(stack);
            if (lengthHolder.tide$getQuality() < 0) grade(stack, context, lengthHolder.tide$getLength());
        }
        return stack;
    }

    /**
     * A wild fish (never caught on a rod) killed for its drop gets a quality and a weight like a catch, rolled at the
     * killer's fishing level. Capped at Great: Trophy and Mythic fish come from the rod. No XP or journal entry.
     */
    private static void grade(ItemStack stack, LootContext context, double length) {
        int level = context.getOptional(LootContextParams.LAST_DAMAGE_PLAYER) instanceof net.minecraft.server.level.ServerPlayer p
                ? com.li64.tide.data.angler.Angler.effectiveLevel(p) : 1;
        grade(stack, context.getRandom(), level, length);
    }

    public static void grade(ItemStack stack, net.minecraft.util.RandomSource rng, int level, double length) {
        if (!com.li64.tide.data.angler.CatchQuality.enabled()) return;
        var data = FishData.get(stack).orElse(null);
        if (data == null) return;
        var q = com.li64.tide.data.angler.CatchQuality.roll(rng, level);
        if (q.ordinal() > com.li64.tide.data.angler.CatchQuality.GREAT.ordinal()) q = com.li64.tide.data.angler.CatchQuality.GREAT;
        if (length <= 0 && data.size().isPresent()) {
            length = q.sampleLength(data.size().get(), rng);
            TideItemData.FISH_LENGTH.set(stack, length);
        }
        if (length > 0) TideItemData.FISH_WEIGHT.set(stack, com.li64.tide.data.angler.FishWeight.grams(
                net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath(), length, q, rng));
        com.li64.tide.data.angler.CatchQuality.mark(stack, q);
    }
}
