package com.li64.tide.data;

import com.li64.tide.Tide;
import com.li64.tide.data.loot.LootTableRef;

public class TideLootTables {
    public static class Fishing {
        public static final LootTableRef TRIAL_CHAMBER_TREASURE = create("gameplay/fishing/trial_chamber");
        public static final LootTableRef OCEAN_MONUMENT_TREASURE = create("gameplay/fishing/ocean_monument");
        public static final LootTableRef DESERT_WELL_TREASURE = create("gameplay/fishing/desert_well");
        public static final LootTableRef SUNFLOWER_ROD = create("gameplay/fishing/sunflower_rod");

        public static final LootTableRef JUNK_UNDERGROUND = create("gameplay/fishing/junk_underground");
        public static final LootTableRef JUNK_LAVA = create("gameplay/fishing/junk_lava");

        public static final LootTableRef TREASURE_UNDERGROUND = create("gameplay/fishing/treasure_underground");
        public static final LootTableRef TREASURE_NETHER = create("gameplay/fishing/treasure_nether");

        public static final LootTableRef BONUS_GOLD = create("gameplay/fishing/bonus_gold");
        public static final LootTableRef BONUS_VILLAGE = create("gameplay/fishing/bonus_village");
    }

    public static class Chests {
        public static final LootTableRef FISHING_BOAT = create("chests/fishing_boat");

        public static class Crates {
            public static final LootTableRef SURFACE_SALTWATER = create("chests/crates/surface_saltwater");
            public static final LootTableRef SURFACE_FRESHWATER = create("chests/crates/surface_freshwater");
            public static final LootTableRef UNDERGROUND = create("chests/crates/underground");
            public static final LootTableRef DEEP = create("chests/crates/deep");
            public static final LootTableRef SURFACE_LAVA = create("chests/crates/surface_lava");
            public static final LootTableRef UNDERGROUND_LAVA = create("chests/crates/underground_lava");
            public static final LootTableRef DEEP_LAVA = create("chests/crates/deep_lava");
            public static final LootTableRef NETHER = create("chests/crates/nether");
            public static final LootTableRef END = create("chests/crates/end");
        }
    }

    private static LootTableRef create(String path) {
        return LootTableRef.createNew(Tide.resource(path));
    }
}
