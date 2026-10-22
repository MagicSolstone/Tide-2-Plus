package com.li64.tide.data.loaders;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class VoidHeightLoader extends SimpleJsonResourceReloadListener {
    public VoidHeightLoader(Gson gson) {
        super(gson, "");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {

    }
}
