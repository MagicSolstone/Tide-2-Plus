package com.li64.tide.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.li64.tide.Tide;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Tide's settings, kept as JSON in config/tide.json (client and server parts). Upstream used
 * Cloth Config / AutoConfig, which this fork drops so it has one dependency fewer to wait on
 * after each Minecraft update. Missing settings take their defaults; the file is rewritten
 * with every setting on load so it's easy to edit.
 */
public class TideConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    TideClientConfig client = new TideClientConfig();
    TideServerConfig server = new TideServerConfig();

    public TideClientConfig client() {
        return this.client;
    }

    public TideServerConfig server() {
        return this.server;
    }

    public static TideConfig load(Path dir) {
        Path file = dir.resolve(Tide.MOD_ID + ".json");
        TideConfig config = null;
        if (Files.exists(file)) {
            try (Reader r = Files.newBufferedReader(file)) {
                config = GSON.fromJson(r, TideConfig.class);
            } catch (Exception e) {
                Tide.LOG.error("Couldn't read {}, using defaults", file, e);
            }
        }
        if (config == null) config = new TideConfig();
        if (config.client == null) config.client = new TideClientConfig();
        if (config.server == null) config.server = new TideServerConfig();
        try {
            Files.createDirectories(dir);
            try (Writer w = Files.newBufferedWriter(file)) {
                GSON.toJson(config, w);
            }
        } catch (Exception e) {
            Tide.LOG.warn("Couldn't write {}", file, e);
        }
        return config;
    }
}
