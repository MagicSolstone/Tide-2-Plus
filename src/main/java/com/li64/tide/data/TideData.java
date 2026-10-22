package com.li64.tide.data;

import com.li64.tide.data.fishing.CrateData;
import com.li64.tide.data.fishing.FishingLootData;
import com.li64.tide.data.loaders.AbstractDataLoader;
import com.li64.tide.data.loaders.FishDataLoader;
import com.li64.tide.data.loaders.FlatDataLoader;
import com.li64.tide.data.loaders.LayeredDataLoader;
import com.li64.tide.data.rods.AccessoryData;
import com.li64.tide.data.rods.BaitData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class TideData {
    private static final List<AbstractDataLoader<?, ?>> LOADERS = new ArrayList<>();

    public static FishDataLoader FISH = registerDataLoader(new FishDataLoader("fishing/fish"));
    public static LayeredDataLoader<FishingLootData> FISHING_LOOT = registerDataLoader(LayeredDataLoader.of("fishing/loot", FishingLootData.CODEC));
    public static LayeredDataLoader<CrateData> CRATES = registerDataLoader(LayeredDataLoader.of("fishing/crates", CrateData.CODEC));
    public static FlatDataLoader<BaitData> BAIT = registerDataLoader(FlatDataLoader.of("bait", BaitData.CODEC));
    public static FlatDataLoader<AccessoryData> ACCESSORIES = registerDataLoader(FlatDataLoader.of("rod_accessories", AccessoryData.CODEC));

    private static <T, R, L extends AbstractDataLoader<T, R>> L registerDataLoader(L loader) {
        LOADERS.add(loader);
        return loader;
    }

    public static void onRegisterReloadListeners(BiConsumer<Identifier, PreparableReloadListener> registry) {
        LOADERS.forEach(loader -> registry.accept(loader.getDirectory(), loader));
    }

    public static void readFromPacket(FriendlyByteBuf buf) {
        LOADERS.forEach(loader -> loader.decode(buf));
    }

    public static void writeToPacket(FriendlyByteBuf buf) {
        LOADERS.forEach(loader -> loader.encode(buf));
    }
}
