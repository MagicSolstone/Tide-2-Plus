package com.li64.tide.config;

import com.google.gson.Gson;
import com.li64.tide.Tide;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
public final class TideServerConfig {
    public General general;
    public Items items;
    public Journal journal;
    public Minigame minigame;
    public Leveling leveling;

    public TideServerConfig() {
        this.general = new General();
        this.items = new Items();
        this.journal = new Journal();
        this.minigame = new Minigame();
        this.leveling = new Leveling();
    }

    /** The fishing level system (this fork). Off = everyone fishes at the baseline level. */
    public static class Leveling {
        public boolean enabled = true;
        /** RuneScape's XP table is divided by this (bigger = faster levels). */
        public double xpScale = 20.0;
        /** The level that fishes exactly like original Tide, and what "fair play" contests use. */
        public int baselineLevel = 40;
        public boolean announceLevelUps = true;
    }

    public static void writeToPacket(FriendlyByteBuf buf, Gson gson) {
        String json = gson.toJson(Tide.SERVER_CONFIG);
        buf.writeUtf(json);
    }

    public static void readFromPacket(FriendlyByteBuf buf, Gson gson) {
        String json = buf.readUtf();
        Tide.SERVER_CONFIG = gson.fromJson(json, TideServerConfig.class);
    }

    public static class General {
        public boolean overrideVanillaRod = true;
        public boolean holdToCast = true;
        public double rodDurabilityMultiplier = 1.0;
        public List<VoidHeightEntry> fishableVoidHeights = List.of(
                new VoidHeightEntry(Level.OVERWORLD, VoidHeightEntry.Type.RELATIVE_TO_BOTTOM, -6),
                new VoidHeightEntry(Level.NETHER, VoidHeightEntry.Type.RELATIVE_TO_BOTTOM, -6),
                new VoidHeightEntry(Level.END, VoidHeightEntry.Type.RELATIVE_TO_BOTTOM, 50)
        );

        public static class VoidHeightEntry {
            public String dimension = "minecraft:overworld";
            public Type type = Type.RELATIVE_TO_BOTTOM;
            public Integer height = -6;

            public VoidHeightEntry() {}

            public VoidHeightEntry(ResourceKey<Level> dimension, Type type, int height) {
                this.dimension = dimension.identifier().toString();
                this.type = type;
                this.height = height;
            }

            public enum Type {
                RELATIVE_TO_BOTTOM, RELATIVE_TO_TOP, ABSOLUTE;

                                public @NotNull String getKey() {
                    return "text.autoconfig.tide.option.VoidHeightEntry.Type." + name().toLowerCase();
                }
            }
        }
        public List<String> autoFishDataBlacklist = List.of();
        public double crateWeight = 4.0;
        public double crateQuality = 1.0;
        public boolean logDataErrors = false;

        @Override
        public String toString() {
            return "General{" +
                    "overrideVanillaRod=" + overrideVanillaRod +
                    ", holdToCast=" + holdToCast +
                    ", rodDurabilityMultiplier=" + rodDurabilityMultiplier +
                    ", fishableVoidHeights=" + fishableVoidHeights +
                    ", autoFishDataBlacklist=" + autoFishDataBlacklist +
                    ", crateWeight=" + crateWeight +
                    ", crateQuality=" + crateQuality +
                    ", logDataErrors=" + logDataErrors +
                    '}';
        }
    }

    public static class Items {
        public SizeMode fishItemSizes = SizeMode.ALWAYS;

        public enum SizeMode {
            ALWAYS, IN_JOURNAL, NEVER;

                        public @NotNull String getKey() {
                return "text.autoconfig.tide.option.server.items.fishItemSizes." + name().toLowerCase();
            }
        }
        public BucketableMode bucketableFishItems = BucketableMode.WHEN_LIVING;

        public enum BucketableMode {
            ALWAYS, WHEN_LIVING, NEVER;

                        public @NotNull String getKey() {
                return "text.autoconfig.tide.option.server.items.bucketableFishItems." + name().toLowerCase();
            }
        }
        public boolean enableBedrockBreakingItems = true;
        public List<String> pocketWatchBlacklist = List.of();

        @Override
        public String toString() {
            return "Items{" +
                    "fishItemSizes=" + fishItemSizes +
                    ", bucketableFishItems=" + bucketableFishItems +
                    ", enableBedrockBreakingItems=" + enableBedrockBreakingItems +
                    ", pocketWatchBlacklist=" + pocketWatchBlacklist +
                    '}';
        }
    }

    public static class Journal {
        public boolean giveJournal = true;
        public boolean showToasts = true;

        @Override
        public String toString() {
            return "Journal{" +
                    "giveJournal=" + giveJournal +
                    ", showToasts=" + showToasts +
                    '}';
        }
    }

    public static class Minigame {
        public boolean doMinigame = true;
        public float minigameDifficultyMultiplier = 1.0f;
        public boolean doSuccessSound = true;
        public boolean doFailSound = true;
        public boolean useThirdPartyMinigames = true;
        public boolean useThirdPartyFishData = false;

        @Override
        public String toString() {
            return "Minigame{" +
                    "doMinigame=" + doMinigame +
                    ", minigameDifficultyMultiplier=" + minigameDifficultyMultiplier +
                    ", doSuccessSound=" + doSuccessSound +
                    ", doFailSound=" + doFailSound +
                    ", useThirdPartyMinigames=" + useThirdPartyMinigames +
                    ", useThirdPartyFishData=" + useThirdPartyFishData +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TideConfig{" +
                "general=" + general +
                ", items=" + items +
                ", journal=" + journal +
                ", minigame=" + minigame +
                '}';
    }
}