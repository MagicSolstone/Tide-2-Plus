package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

class ChunkPalettedStorageFix$MappingConstants {
   private static final BitSet VIRTUAL = new BitSet(256);
   private static final BitSet FIX = new BitSet(256);
   private static final Dynamic<?> PUMPKIN = ChunkPalettedStorageFix.legacyBlockState("minecraft:pumpkin");
   private static final Dynamic<?> SNOWY_PODZOL = ChunkPalettedStorageFix.legacyBlockState("minecraft:podzol", Map.of("snowy", "true"));
   private static final Dynamic<?> SNOWY_GRASS = ChunkPalettedStorageFix.legacyBlockState("minecraft:grass_block", Map.of("snowy", "true"));
   private static final Dynamic<?> SNOWY_MYCELIUM = ChunkPalettedStorageFix.legacyBlockState("minecraft:mycelium", Map.of("snowy", "true"));
   private static final Dynamic<?> UPPER_SUNFLOWER = ChunkPalettedStorageFix.legacyBlockState("minecraft:sunflower", Map.of("half", "upper"));
   private static final Dynamic<?> UPPER_LILAC = ChunkPalettedStorageFix.legacyBlockState("minecraft:lilac", Map.of("half", "upper"));
   private static final Dynamic<?> UPPER_TALL_GRASS = ChunkPalettedStorageFix.legacyBlockState("minecraft:tall_grass", Map.of("half", "upper"));
   private static final Dynamic<?> UPPER_LARGE_FERN = ChunkPalettedStorageFix.legacyBlockState("minecraft:large_fern", Map.of("half", "upper"));
   private static final Dynamic<?> UPPER_ROSE_BUSH = ChunkPalettedStorageFix.legacyBlockState("minecraft:rose_bush", Map.of("half", "upper"));
   private static final Dynamic<?> UPPER_PEONY = ChunkPalettedStorageFix.legacyBlockState("minecraft:peony", Map.of("half", "upper"));
   private static final Map<String, Dynamic<?>> FLOWER_POT_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
      map.put("minecraft:air0", ChunkPalettedStorageFix.legacyBlockState("minecraft:flower_pot"));
      map.put("minecraft:red_flower0", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_poppy"));
      map.put("minecraft:red_flower1", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_blue_orchid"));
      map.put("minecraft:red_flower2", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_allium"));
      map.put("minecraft:red_flower3", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_azure_bluet"));
      map.put("minecraft:red_flower4", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_red_tulip"));
      map.put("minecraft:red_flower5", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_orange_tulip"));
      map.put("minecraft:red_flower6", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_white_tulip"));
      map.put("minecraft:red_flower7", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_pink_tulip"));
      map.put("minecraft:red_flower8", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_oxeye_daisy"));
      map.put("minecraft:yellow_flower0", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_dandelion"));
      map.put("minecraft:sapling0", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_oak_sapling"));
      map.put("minecraft:sapling1", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_spruce_sapling"));
      map.put("minecraft:sapling2", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_birch_sapling"));
      map.put("minecraft:sapling3", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_jungle_sapling"));
      map.put("minecraft:sapling4", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_acacia_sapling"));
      map.put("minecraft:sapling5", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_dark_oak_sapling"));
      map.put("minecraft:red_mushroom0", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_red_mushroom"));
      map.put("minecraft:brown_mushroom0", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_brown_mushroom"));
      map.put("minecraft:deadbush0", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_dead_bush"));
      map.put("minecraft:tallgrass2", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_fern"));
      map.put("minecraft:cactus0", ChunkPalettedStorageFix.legacyBlockState("minecraft:potted_cactus"));
   });
   private static final Map<String, Dynamic<?>> SKULL_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
      mapSkull(map, 0, "skeleton", "skull");
      mapSkull(map, 1, "wither_skeleton", "skull");
      mapSkull(map, 2, "zombie", "head");
      mapSkull(map, 3, "player", "head");
      mapSkull(map, 4, "creeper", "head");
      mapSkull(map, 5, "dragon", "head");
   });
   private static final Map<String, Dynamic<?>> DOOR_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
      mapDoor(map, "oak_door");
      mapDoor(map, "iron_door");
      mapDoor(map, "spruce_door");
      mapDoor(map, "birch_door");
      mapDoor(map, "jungle_door");
      mapDoor(map, "acacia_door");
      mapDoor(map, "dark_oak_door");
   });
   private static final Map<String, Dynamic<?>> NOTE_BLOCK_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
      for (int i = 0; i < 26; i++) {
         map.put("true" + i, ChunkPalettedStorageFix.legacyBlockState("minecraft:note_block", Map.of("powered", "true", "note", String.valueOf(i))));
         map.put("false" + i, ChunkPalettedStorageFix.legacyBlockState("minecraft:note_block", Map.of("powered", "false", "note", String.valueOf(i))));
      }
   });
   private static final Int2ObjectMap<String> DYE_COLOR_MAP = (Int2ObjectMap<String>)DataFixUtils.make(new Int2ObjectOpenHashMap(), map -> {
      map.put(0, "white");
      map.put(1, "orange");
      map.put(2, "magenta");
      map.put(3, "light_blue");
      map.put(4, "yellow");
      map.put(5, "lime");
      map.put(6, "pink");
      map.put(7, "gray");
      map.put(8, "light_gray");
      map.put(9, "cyan");
      map.put(10, "purple");
      map.put(11, "blue");
      map.put(12, "brown");
      map.put(13, "green");
      map.put(14, "red");
      map.put(15, "black");
   });
   private static final Map<String, Dynamic<?>> BED_BLOCK_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
      Iterator i$ = DYE_COLOR_MAP.int2ObjectEntrySet().iterator();

      while (i$.hasNext()) {
         Entry<String> entry = (Entry<String>)i$.next();
         if (!Objects.equals(entry.getValue(), "red")) {
            addBeds(map, entry.getIntKey(), (String)entry.getValue());
         }
      }
   });
   private static final Map<String, Dynamic<?>> BANNER_BLOCK_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), map -> {
      Iterator i$ = DYE_COLOR_MAP.int2ObjectEntrySet().iterator();

      while (i$.hasNext()) {
         Entry<String> entry = (Entry<String>)i$.next();
         if (!Objects.equals(entry.getValue(), "white")) {
            addBanners(map, 15 - entry.getIntKey(), (String)entry.getValue());
         }
      }
   });
   private static final Dynamic<?> AIR = ChunkPalettedStorageFix.legacyBlockState("minecraft:air");

   private ChunkPalettedStorageFix$MappingConstants() {
   }

   private static void mapSkull(final Map<String, Dynamic<?>> map, final int i, final String name, final String type) {
      map.put(i + "north", ChunkPalettedStorageFix.legacyBlockState("minecraft:" + name + "_wall_" + type, Map.of("facing", "north")));
      map.put(i + "east", ChunkPalettedStorageFix.legacyBlockState("minecraft:" + name + "_wall_" + type, Map.of("facing", "east")));
      map.put(i + "south", ChunkPalettedStorageFix.legacyBlockState("minecraft:" + name + "_wall_" + type, Map.of("facing", "south")));
      map.put(i + "west", ChunkPalettedStorageFix.legacyBlockState("minecraft:" + name + "_wall_" + type, Map.of("facing", "west")));

      for (int rot = 0; rot < 16; rot++) {
         map.put("" + i + rot, ChunkPalettedStorageFix.legacyBlockState("minecraft:" + name + "_" + type, Map.of("rotation", String.valueOf(rot))));
      }
   }

   private static void mapDoor(final Map<String, Dynamic<?>> map, final String type) {
      String id = "minecraft:" + type;
      map.put(
         "minecraft:" + type + "eastlowerleftfalsefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "false", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "eastlowerleftfalsetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "false", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "eastlowerlefttruefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "true", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "eastlowerlefttruetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "true", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "eastlowerrightfalsefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "false", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "eastlowerrightfalsetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "false", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "eastlowerrighttruefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "true", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "eastlowerrighttruetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "true", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "eastupperleftfalsefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "false", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "eastupperleftfalsetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "false", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "eastupperlefttruefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "true", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "eastupperlefttruetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "true", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "eastupperrightfalsefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "false", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "eastupperrightfalsetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "false", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "eastupperrighttruefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "true", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "eastupperrighttruetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "true", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "northlowerleftfalsefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "false", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "northlowerleftfalsetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "false", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "northlowerlefttruefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "true", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "northlowerlefttruetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "true", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "northlowerrightfalsefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "false", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "northlowerrightfalsetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "false", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "northlowerrighttruefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "true", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "northlowerrighttruetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "true", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "northupperleftfalsefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "false", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "northupperleftfalsetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "false", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "northupperlefttruefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "true", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "northupperlefttruetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "true", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "northupperrightfalsefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "false", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "northupperrightfalsetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "false", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "northupperrighttruefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "true", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "northupperrighttruetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "true", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "southlowerleftfalsefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "false", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "southlowerleftfalsetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "false", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "southlowerlefttruefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "true", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "southlowerlefttruetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "true", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "southlowerrightfalsefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "false", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "southlowerrightfalsetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "false", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "southlowerrighttruefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "true", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "southlowerrighttruetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "true", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "southupperleftfalsefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "false", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "southupperleftfalsetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "false", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "southupperlefttruefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "true", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "southupperlefttruetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "true", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "southupperrightfalsefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "false", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "southupperrightfalsetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "false", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "southupperrighttruefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "true", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "southupperrighttruetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "true", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "westlowerleftfalsefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "false", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "westlowerleftfalsetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "false", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "westlowerlefttruefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "true", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "westlowerlefttruetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "true", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "westlowerrightfalsefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "false", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "westlowerrightfalsetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "false", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "westlowerrighttruefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "true", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "westlowerrighttruetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "true", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "westupperleftfalsefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "false", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "westupperleftfalsetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "false", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "westupperlefttruefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "true", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "westupperlefttruetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "true", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "westupperrightfalsefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "false", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "westupperrightfalsetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "false", "powered", "true"))
      );
      map.put(
         "minecraft:" + type + "westupperrighttruefalse",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "true", "powered", "false"))
      );
      map.put(
         "minecraft:" + type + "westupperrighttruetrue",
         ChunkPalettedStorageFix.legacyBlockState(id, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "true", "powered", "true"))
      );
   }

   private static void addBeds(final Map<String, Dynamic<?>> map, final int colorId, final String color) {
      map.put(
         "southfalsefoot" + colorId,
         ChunkPalettedStorageFix.legacyBlockState("minecraft:" + color + "_bed", Map.of("facing", "south", "occupied", "false", "part", "foot"))
      );
      map.put(
         "westfalsefoot" + colorId,
         ChunkPalettedStorageFix.legacyBlockState("minecraft:" + color + "_bed", Map.of("facing", "west", "occupied", "false", "part", "foot"))
      );
      map.put(
         "northfalsefoot" + colorId,
         ChunkPalettedStorageFix.legacyBlockState("minecraft:" + color + "_bed", Map.of("facing", "north", "occupied", "false", "part", "foot"))
      );
      map.put(
         "eastfalsefoot" + colorId,
         ChunkPalettedStorageFix.legacyBlockState("minecraft:" + color + "_bed", Map.of("facing", "east", "occupied", "false", "part", "foot"))
      );
      map.put(
         "southfalsehead" + colorId,
         ChunkPalettedStorageFix.legacyBlockState("minecraft:" + color + "_bed", Map.of("facing", "south", "occupied", "false", "part", "head"))
      );
      map.put(
         "westfalsehead" + colorId,
         ChunkPalettedStorageFix.legacyBlockState("minecraft:" + color + "_bed", Map.of("facing", "west", "occupied", "false", "part", "head"))
      );
      map.put(
         "northfalsehead" + colorId,
         ChunkPalettedStorageFix.legacyBlockState("minecraft:" + color + "_bed", Map.of("facing", "north", "occupied", "false", "part", "head"))
      );
      map.put(
         "eastfalsehead" + colorId,
         ChunkPalettedStorageFix.legacyBlockState("minecraft:" + color + "_bed", Map.of("facing", "east", "occupied", "false", "part", "head"))
      );
      map.put(
         "southtruehead" + colorId,
         ChunkPalettedStorageFix.legacyBlockState("minecraft:" + color + "_bed", Map.of("facing", "south", "occupied", "true", "part", "head"))
      );
      map.put(
         "westtruehead" + colorId,
         ChunkPalettedStorageFix.legacyBlockState("minecraft:" + color + "_bed", Map.of("facing", "west", "occupied", "true", "part", "head"))
      );
      map.put(
         "northtruehead" + colorId,
         ChunkPalettedStorageFix.legacyBlockState("minecraft:" + color + "_bed", Map.of("facing", "north", "occupied", "true", "part", "head"))
      );
      map.put(
         "easttruehead" + colorId,
         ChunkPalettedStorageFix.legacyBlockState("minecraft:" + color + "_bed", Map.of("facing", "east", "occupied", "true", "part", "head"))
      );
   }

   private static void addBanners(final Map<String, Dynamic<?>> map, final int colorId, final String color) {
      for (int i = 0; i < 16; i++) {
         map.put(i + "_" + colorId, ChunkPalettedStorageFix.legacyBlockState("minecraft:" + color + "_banner", Map.of("rotation", String.valueOf(i))));
      }

      map.put("north_" + colorId, ChunkPalettedStorageFix.legacyBlockState("minecraft:" + color + "_wall_banner", Map.of("facing", "north")));
      map.put("south_" + colorId, ChunkPalettedStorageFix.legacyBlockState("minecraft:" + color + "_wall_banner", Map.of("facing", "south")));
      map.put("west_" + colorId, ChunkPalettedStorageFix.legacyBlockState("minecraft:" + color + "_wall_banner", Map.of("facing", "west")));
      map.put("east_" + colorId, ChunkPalettedStorageFix.legacyBlockState("minecraft:" + color + "_wall_banner", Map.of("facing", "east")));
   }

   static {
      FIX.set(2);
      FIX.set(3);
      FIX.set(110);
      FIX.set(140);
      FIX.set(144);
      FIX.set(25);
      FIX.set(86);
      FIX.set(26);
      FIX.set(176);
      FIX.set(177);
      FIX.set(175);
      FIX.set(64);
      FIX.set(71);
      FIX.set(193);
      FIX.set(194);
      FIX.set(195);
      FIX.set(196);
      FIX.set(197);
      VIRTUAL.set(54);
      VIRTUAL.set(146);
      VIRTUAL.set(25);
      VIRTUAL.set(26);
      VIRTUAL.set(51);
      VIRTUAL.set(53);
      VIRTUAL.set(67);
      VIRTUAL.set(108);
      VIRTUAL.set(109);
      VIRTUAL.set(114);
      VIRTUAL.set(128);
      VIRTUAL.set(134);
      VIRTUAL.set(135);
      VIRTUAL.set(136);
      VIRTUAL.set(156);
      VIRTUAL.set(163);
      VIRTUAL.set(164);
      VIRTUAL.set(180);
      VIRTUAL.set(203);
      VIRTUAL.set(55);
      VIRTUAL.set(85);
      VIRTUAL.set(113);
      VIRTUAL.set(188);
      VIRTUAL.set(189);
      VIRTUAL.set(190);
      VIRTUAL.set(191);
      VIRTUAL.set(192);
      VIRTUAL.set(93);
      VIRTUAL.set(94);
      VIRTUAL.set(101);
      VIRTUAL.set(102);
      VIRTUAL.set(160);
      VIRTUAL.set(106);
      VIRTUAL.set(107);
      VIRTUAL.set(183);
      VIRTUAL.set(184);
      VIRTUAL.set(185);
      VIRTUAL.set(186);
      VIRTUAL.set(187);
      VIRTUAL.set(132);
      VIRTUAL.set(139);
      VIRTUAL.set(199);
   }
}
