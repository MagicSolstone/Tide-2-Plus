package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Lists;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Arrays;
import java.util.List;
import org.jspecify.annotations.Nullable;

final class ChunkPalettedStorageFix$UpgradeChunk {
   private int sides;
   private final ChunkPalettedStorageFix$Section[] sections = new ChunkPalettedStorageFix$Section[16];
   private final Dynamic<?> level;
   private final int x;
   private final int z;
   private final Int2ObjectMap<Dynamic<?>> blockEntities = new Int2ObjectLinkedOpenHashMap(16);

   public ChunkPalettedStorageFix$UpgradeChunk(final Dynamic<?> level) {
      this.level = level;
      this.x = level.get("xPos").asInt(0) << 4;
      this.z = level.get("zPos").asInt(0) << 4;
      level.get("TileEntities")
         .asStreamOpt()
         .ifSuccess(
            s -> s.forEach(
               entity -> {
                  int x = entity.get("x").asInt(0) - this.x & 15;
                  int y = entity.get("y").asInt(0);
                  int z = entity.get("z").asInt(0) - this.z & 15;
                  int key = y << 8 | z << 4 | x;
                  if (this.blockEntities.put(key, entity) != null) {
                     ChunkPalettedStorageFix.LOGGER
                        .warn("In chunk: {}x{} found a duplicate block entity at position: [{}, {}, {}]", new Object[]{this.x, this.z, x, y, z});
                  }
               }
            )
         );
      boolean convertedFromAlphaFormat = level.get("convertedFromAlphaFormat").asBoolean(false);
      level.get("Sections").asStreamOpt().ifSuccess(s -> s.forEach(sec -> {
         ChunkPalettedStorageFix$Section sectionx = new ChunkPalettedStorageFix$Section((Dynamic<?>)sec);
         this.sides = sectionx.upgrade(this.sides);
         this.sections[sectionx.y] = sectionx;
      }));

      for (ChunkPalettedStorageFix$Section section : this.sections) {
         if (section != null) {
            ObjectIterator var7 = section.toFix.int2ObjectEntrySet().iterator();

            while (var7.hasNext()) {
               Entry<IntList> entry = (Entry<IntList>)var7.next();
               int dy = section.y << 12;
               switch (entry.getIntKey()) {
                  case 2:
                     IntListIterator var30 = ((IntList)entry.getValue()).iterator();

                     while (var30.hasNext()) {
                        int pos = (Integer)var30.next();
                        pos |= dy;
                        Dynamic<?> state = this.getBlock(pos);
                        if ("minecraft:grass_block".equals(ChunkPalettedStorageFix.getName(state))) {
                           String name = ChunkPalettedStorageFix.getName(this.getBlock(relative(pos, ChunkPalettedStorageFix$Direction.UP)));
                           if ("minecraft:snow".equals(name) || "minecraft:snow_layer".equals(name)) {
                              this.setBlock(pos, ChunkPalettedStorageFix$MappingConstants.SNOWY_GRASS);
                           }
                        }
                     }
                     break;
                  case 3:
                     IntListIterator var29 = ((IntList)entry.getValue()).iterator();

                     while (var29.hasNext()) {
                        int pos = (Integer)var29.next();
                        pos |= dy;
                        Dynamic<?> state = this.getBlock(pos);
                        if ("minecraft:podzol".equals(ChunkPalettedStorageFix.getName(state))) {
                           String name = ChunkPalettedStorageFix.getName(this.getBlock(relative(pos, ChunkPalettedStorageFix$Direction.UP)));
                           if ("minecraft:snow".equals(name) || "minecraft:snow_layer".equals(name)) {
                              this.setBlock(pos, ChunkPalettedStorageFix$MappingConstants.SNOWY_PODZOL);
                           }
                        }
                     }
                     break;
                  case 25:
                     IntListIterator var28 = ((IntList)entry.getValue()).iterator();

                     while (var28.hasNext()) {
                        int pos = (Integer)var28.next();
                        pos |= dy;
                        Dynamic<?> entity = this.removeBlockEntity(pos);
                        if (entity != null) {
                           String key = Boolean.toString(entity.get("powered").asBoolean(false)) + (byte)Math.min(Math.max(entity.get("note").asInt(0), 0), 24);
                           this.setBlock(
                              pos,
                              ChunkPalettedStorageFix$MappingConstants.NOTE_BLOCK_MAP
                                 .getOrDefault(key, ChunkPalettedStorageFix$MappingConstants.NOTE_BLOCK_MAP.get("false0"))
                           );
                        }
                     }
                     break;
                  case 26:
                     IntListIterator var27 = ((IntList)entry.getValue()).iterator();

                     while (var27.hasNext()) {
                        int pos = (Integer)var27.next();
                        pos |= dy;
                        Dynamic<?> entity = this.getBlockEntity(pos);
                        Dynamic<?> state = this.getBlock(pos);
                        if (entity != null) {
                           int color = entity.get("color").asInt(0);
                           if (color != 14 && color >= 0 && color < 16) {
                              String key = ChunkPalettedStorageFix.getProperty(state, "facing")
                                 + ChunkPalettedStorageFix.getProperty(state, "occupied")
                                 + ChunkPalettedStorageFix.getProperty(state, "part")
                                 + color;
                              if (ChunkPalettedStorageFix$MappingConstants.BED_BLOCK_MAP.containsKey(key)) {
                                 this.setBlock(pos, ChunkPalettedStorageFix$MappingConstants.BED_BLOCK_MAP.get(key));
                              }
                           }
                        }
                     }
                     break;
                  case 64:
                  case 71:
                  case 193:
                  case 194:
                  case 195:
                  case 196:
                  case 197:
                     IntListIterator var26 = ((IntList)entry.getValue()).iterator();

                     while (var26.hasNext()) {
                        int pos = (Integer)var26.next();
                        pos |= dy;
                        Dynamic<?> state = this.getBlock(pos);
                        if (ChunkPalettedStorageFix.getName(state).endsWith("_door")) {
                           Dynamic<?> lower = this.getBlock(pos);
                           if ("lower".equals(ChunkPalettedStorageFix.getProperty(lower, "half"))) {
                              int abovePos = relative(pos, ChunkPalettedStorageFix$Direction.UP);
                              Dynamic<?> upper = this.getBlock(abovePos);
                              String name = ChunkPalettedStorageFix.getName(lower);
                              if (name.equals(ChunkPalettedStorageFix.getName(upper))) {
                                 String facing = ChunkPalettedStorageFix.getProperty(lower, "facing");
                                 String open = ChunkPalettedStorageFix.getProperty(lower, "open");
                                 String hinge = convertedFromAlphaFormat ? "left" : ChunkPalettedStorageFix.getProperty(upper, "hinge");
                                 String powered = convertedFromAlphaFormat ? "false" : ChunkPalettedStorageFix.getProperty(upper, "powered");
                                 this.setBlock(pos, ChunkPalettedStorageFix$MappingConstants.DOOR_MAP.get(name + facing + "lower" + hinge + open + powered));
                                 this.setBlock(
                                    abovePos, ChunkPalettedStorageFix$MappingConstants.DOOR_MAP.get(name + facing + "upper" + hinge + open + powered)
                                 );
                              }
                           }
                        }
                     }
                     break;
                  case 86:
                     IntListIterator var25 = ((IntList)entry.getValue()).iterator();

                     while (var25.hasNext()) {
                        int pos = (Integer)var25.next();
                        pos |= dy;
                        Dynamic<?> state = this.getBlock(pos);
                        if ("minecraft:carved_pumpkin".equals(ChunkPalettedStorageFix.getName(state))) {
                           String name = ChunkPalettedStorageFix.getName(this.getBlock(relative(pos, ChunkPalettedStorageFix$Direction.DOWN)));
                           if ("minecraft:grass_block".equals(name) || "minecraft:dirt".equals(name)) {
                              this.setBlock(pos, ChunkPalettedStorageFix$MappingConstants.PUMPKIN);
                           }
                        }
                     }
                     break;
                  case 110:
                     IntListIterator var24 = ((IntList)entry.getValue()).iterator();

                     while (var24.hasNext()) {
                        int pos = (Integer)var24.next();
                        pos |= dy;
                        Dynamic<?> state = this.getBlock(pos);
                        if ("minecraft:mycelium".equals(ChunkPalettedStorageFix.getName(state))) {
                           String name = ChunkPalettedStorageFix.getName(this.getBlock(relative(pos, ChunkPalettedStorageFix$Direction.UP)));
                           if ("minecraft:snow".equals(name) || "minecraft:snow_layer".equals(name)) {
                              this.setBlock(pos, ChunkPalettedStorageFix$MappingConstants.SNOWY_MYCELIUM);
                           }
                        }
                     }
                     break;
                  case 140:
                     IntListIterator var23 = ((IntList)entry.getValue()).iterator();

                     while (var23.hasNext()) {
                        int pos = (Integer)var23.next();
                        pos |= dy;
                        Dynamic<?> entity = this.removeBlockEntity(pos);
                        if (entity != null) {
                           String key = entity.get("Item").asString("") + entity.get("Data").asInt(0);
                           this.setBlock(
                              pos,
                              ChunkPalettedStorageFix$MappingConstants.FLOWER_POT_MAP
                                 .getOrDefault(key, ChunkPalettedStorageFix$MappingConstants.FLOWER_POT_MAP.get("minecraft:air0"))
                           );
                        }
                     }
                     break;
                  case 144:
                     IntListIterator var22 = ((IntList)entry.getValue()).iterator();

                     while (var22.hasNext()) {
                        int pos = (Integer)var22.next();
                        pos |= dy;
                        Dynamic<?> entity = this.getBlockEntity(pos);
                        if (entity != null) {
                           String type = String.valueOf(entity.get("SkullType").asInt(0));
                           String facing = ChunkPalettedStorageFix.getProperty(this.getBlock(pos), "facing");
                           String key;
                           if (!"up".equals(facing) && !"down".equals(facing)) {
                              key = type + facing;
                           } else {
                              key = type + entity.get("Rot").asInt(0);
                           }

                           entity.remove("SkullType");
                           entity.remove("facing");
                           entity.remove("Rot");
                           this.setBlock(
                              pos,
                              ChunkPalettedStorageFix$MappingConstants.SKULL_MAP
                                 .getOrDefault(key, ChunkPalettedStorageFix$MappingConstants.SKULL_MAP.get("0north"))
                           );
                        }
                     }
                     break;
                  case 175:
                     IntListIterator var21 = ((IntList)entry.getValue()).iterator();

                     while (var21.hasNext()) {
                        int pos = (Integer)var21.next();
                        pos |= dy;
                        Dynamic<?> block = this.getBlock(pos);
                        if ("upper".equals(ChunkPalettedStorageFix.getProperty(block, "half"))) {
                           Dynamic<?> below = this.getBlock(relative(pos, ChunkPalettedStorageFix$Direction.DOWN));
                           String variant = ChunkPalettedStorageFix.getName(below);
                           switch (variant) {
                              case "minecraft:sunflower":
                                 this.setBlock(pos, ChunkPalettedStorageFix$MappingConstants.UPPER_SUNFLOWER);
                                 break;
                              case "minecraft:lilac":
                                 this.setBlock(pos, ChunkPalettedStorageFix$MappingConstants.UPPER_LILAC);
                                 break;
                              case "minecraft:tall_grass":
                                 this.setBlock(pos, ChunkPalettedStorageFix$MappingConstants.UPPER_TALL_GRASS);
                                 break;
                              case "minecraft:large_fern":
                                 this.setBlock(pos, ChunkPalettedStorageFix$MappingConstants.UPPER_LARGE_FERN);
                                 break;
                              case "minecraft:rose_bush":
                                 this.setBlock(pos, ChunkPalettedStorageFix$MappingConstants.UPPER_ROSE_BUSH);
                                 break;
                              case "minecraft:peony":
                                 this.setBlock(pos, ChunkPalettedStorageFix$MappingConstants.UPPER_PEONY);
                           }
                        }
                     }
                     break;
                  case 176:
                  case 177:
                     IntListIterator var10 = ((IntList)entry.getValue()).iterator();

                     while (var10.hasNext()) {
                        int pos = (Integer)var10.next();
                        pos |= dy;
                        Dynamic<?> entity = this.getBlockEntity(pos);
                        Dynamic<?> state = this.getBlock(pos);
                        if (entity != null) {
                           int color = entity.get("Base").asInt(0);
                           if (color != 15 && color >= 0 && color < 16) {
                              String key = ChunkPalettedStorageFix.getProperty(state, entry.getIntKey() == 176 ? "rotation" : "facing") + "_" + color;
                              if (ChunkPalettedStorageFix$MappingConstants.BANNER_BLOCK_MAP.containsKey(key)) {
                                 this.setBlock(pos, ChunkPalettedStorageFix$MappingConstants.BANNER_BLOCK_MAP.get(key));
                              }
                           }
                        }
                     }
               }
            }
         }
      }
   }

   @Nullable
   private Dynamic<?> getBlockEntity(final int pos) {
      return (Dynamic<?>)this.blockEntities.get(pos);
   }

   @Nullable
   private Dynamic<?> removeBlockEntity(final int pos) {
      return (Dynamic<?>)this.blockEntities.remove(pos);
   }

   public static int relative(final int pos, final ChunkPalettedStorageFix$Direction direction) {
      return switch (direction.getAxis()) {
         case X -> {
            int x = (pos & 15) + direction.getAxisDirection().getStep();
            yield x >= 0 && x <= 15 ? pos & -16 | x : -1;
         }
         case Y -> {
            int y = (pos >> 8) + direction.getAxisDirection().getStep();
            yield y >= 0 && y <= 255 ? pos & 0xFF | y << 8 : -1;
         }
         case Z -> {
            int z = (pos >> 4 & 15) + direction.getAxisDirection().getStep();
            yield z >= 0 && z <= 15 ? pos & -241 | z << 4 : -1;
         }
      };
   }

   private void setBlock(final int pos, final Dynamic<?> block) {
      if (pos >= 0 && pos <= 65535) {
         ChunkPalettedStorageFix$Section section = this.getSection(pos);
         if (section != null) {
            section.setBlock(pos & 4095, block);
         }
      }
   }

   @Nullable
   private ChunkPalettedStorageFix$Section getSection(final int pos) {
      int sectionY = pos >> 12;
      return sectionY < this.sections.length ? this.sections[sectionY] : null;
   }

   public Dynamic<?> getBlock(final int pos) {
      if (pos >= 0 && pos <= 65535) {
         ChunkPalettedStorageFix$Section section = this.getSection(pos);
         return section == null ? ChunkPalettedStorageFix$MappingConstants.AIR : section.getBlock(pos & 4095);
      } else {
         return ChunkPalettedStorageFix$MappingConstants.AIR;
      }
   }

   public Dynamic<?> write() {
      Dynamic<?> level = this.level;
      if (this.blockEntities.isEmpty()) {
         level = level.remove("TileEntities");
      } else {
         level = level.set("TileEntities", level.createList(this.blockEntities.values().stream()));
      }

      Dynamic<?> indices = level.emptyMap();
      List<Dynamic<?>> sections = Lists.newArrayList();

      for (ChunkPalettedStorageFix$Section section : this.sections) {
         if (section != null) {
            sections.add(section.write());
            indices = indices.set(String.valueOf(section.y), indices.createIntList(Arrays.stream(section.update.toIntArray())));
         }
      }

      Dynamic<?> tag = level.emptyMap();
      tag = tag.set("Sides", tag.createByte((byte)this.sides));
      tag = tag.set("Indices", indices);
      return level.set("UpgradeData", tag).set("Sections", tag.createList(sections.stream()));
   }
}
