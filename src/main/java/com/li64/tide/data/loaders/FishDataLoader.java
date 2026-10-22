package com.li64.tide.data.loaders;

import com.li64.tide.data.SendableDataMap;
import com.li64.tide.data.fishing.FishData;
import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class FishDataLoader extends LayeredDataLoader<FishData> {
    protected boolean hasGenerated;

    public FishDataLoader(String directory) {
        super(directory);
    }

    @Override
    protected void apply(@NotNull SendableDataMap<FishData> data, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        this.hasGenerated = false;
        super.apply(data, manager, profiler);
    }

    @Override
    protected Codec<FishData> getEntryCodec() {
        return FishData.CODEC;
    }

    @Override
    public void onDataUpdated() {
        FishData.buildMaps();
    }

    public Map<Identifier, FishData> acceptGenerated(Map<Identifier, FishData> entries) {
        this.hasGenerated = true;
        this.data = SendableDataMap.merge(this.data, entries);
        return entries;
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        this.hasGenerated = true;
        super.decode(buf);
    }

    public boolean hasGenerated() {
        return this.hasGenerated;
    }

    public int journalEntryCount() {
        return Math.toIntExact(this.get().valueStream().filter(FishData::isOriginal).count());
    }
}
