package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.List.ListType;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LeavesFix extends DataFix {
   private static final int NORTH_WEST_MASK = 128;
   private static final int WEST_MASK = 64;
   private static final int SOUTH_WEST_MASK = 32;
   private static final int SOUTH_MASK = 16;
   private static final int SOUTH_EAST_MASK = 8;
   private static final int EAST_MASK = 4;
   private static final int NORTH_EAST_MASK = 2;
   private static final int NORTH_MASK = 1;
   private static final int[][] DIRECTIONS = new int[][]{{-1, 0, 0}, {1, 0, 0}, {0, -1, 0}, {0, 1, 0}, {0, 0, -1}, {0, 0, 1}};
   private static final int DECAY_DISTANCE = 7;
   private static final int SIZE_BITS = 12;
   private static final int SIZE = 4096;
   private static final Object2IntMap<String> LEAVES = (Object2IntMap<String>)DataFixUtils.make(new Object2IntOpenHashMap(), map -> {
      map.put("minecraft:acacia_leaves", 0);
      map.put("minecraft:birch_leaves", 1);
      map.put("minecraft:dark_oak_leaves", 2);
      map.put("minecraft:jungle_leaves", 3);
      map.put("minecraft:oak_leaves", 4);
      map.put("minecraft:spruce_leaves", 5);
   });
   private static final Set<String> LOGS = ImmutableSet.of(
      "minecraft:acacia_bark",
      "minecraft:birch_bark",
      "minecraft:dark_oak_bark",
      "minecraft:jungle_bark",
      "minecraft:oak_bark",
      "minecraft:spruce_bark",
      new String[]{
         "minecraft:acacia_log",
         "minecraft:birch_log",
         "minecraft:dark_oak_log",
         "minecraft:jungle_log",
         "minecraft:oak_log",
         "minecraft:spruce_log",
         "minecraft:stripped_acacia_log",
         "minecraft:stripped_birch_log",
         "minecraft:stripped_dark_oak_log",
         "minecraft:stripped_jungle_log",
         "minecraft:stripped_oak_log",
         "minecraft:stripped_spruce_log"
      }
   );

   public LeavesFix(final Schema outputSchema, final boolean changesType) {
      super(outputSchema, changesType);
   }

   protected TypeRewriteRule makeRule() {
      Type<?> chunkType = this.getInputSchema().getType(References.CHUNK);
      OpticFinder<?> levelFinder = chunkType.findField("Level");
      OpticFinder<?> sectionsFinder = levelFinder.type().findField("Sections");
      Type<?> sectionsType = sectionsFinder.type();
      if (!(sectionsType instanceof ListType)) {
         throw new IllegalStateException("Expecting sections to be a list.");
      } else {
         Type<?> sectionType = ((ListType)sectionsType).getElement();
         OpticFinder<?> sectionFinder = DSL.typeFinder(sectionType);
         return this.fixTypeEverywhereTyped(
            "Leaves fix",
            chunkType,
            chunk -> chunk.updateTyped(
               levelFinder,
               level -> {
                  int[] sides = new int[]{0};
                  Typed<?> newLevel = level.updateTyped(
                     sectionsFinder,
                     sections -> {
                        Int2ObjectMap<LeavesFix$LeavesSection> sectionMap = new Int2ObjectOpenHashMap(
                           sections.getAllTyped(sectionFinder)
                              .stream()
                              .map(sectionx -> new LeavesFix$LeavesSection(sectionx, this.getInputSchema()))
                              .collect(Collectors.toMap(LeavesFix$Section::getIndex, s -> (LeavesFix$LeavesSection)s))
                        );
                        if (sectionMap.values().stream().allMatch(LeavesFix$Section::isSkippable)) {
                           return sections;
                        } else {
                           List<IntSet> queue = Lists.newArrayList();

                           for (int i = 0; i < 7; i++) {
                              queue.add(new IntOpenHashSet());
                           }

                           Iterator i$ = sectionMap.values().iterator();

                           while (i$.hasNext()) {
                              LeavesFix$LeavesSection section = (LeavesFix$LeavesSection)i$.next();
                              if (!section.isSkippable()) {
                                 for (int i = 0; i < 4096; i++) {
                                    int block = section.getBlock(i);
                                    if (section.isLog(block)) {
                                       queue.get(0).add(section.getIndex() << 12 | i);
                                    } else if (section.isLeaf(block)) {
                                       int x = this.getX(i);
                                       int z = this.getZ(i);
                                       sides[0] |= getSideMask(x == 0, x == 15, z == 0, z == 15);
                                    }
                                 }
                              }
                           }

                           for (int ix = 1; ix < 7; ix++) {
                              IntSet set = queue.get(ix - 1);
                              IntSet newSet = queue.get(ix);
                              IntIterator iterator = set.iterator();

                              while (iterator.hasNext()) {
                                 int posChunk = iterator.nextInt();
                                 int x = this.getX(posChunk);
                                 int y = this.getY(posChunk);
                                 int z = this.getZ(posChunk);

                                 for (int[] direction : DIRECTIONS) {
                                    int nx = x + direction[0];
                                    int nyChunk = y + direction[1];
                                    int nz = z + direction[2];
                                    if (nx >= 0 && nx <= 15 && nz >= 0 && nz <= 15 && nyChunk >= 0 && nyChunk <= 255) {
                                       LeavesFix$LeavesSection section = (LeavesFix$LeavesSection)sectionMap.get(nyChunk >> 4);
                                       if (section != null && !section.isSkippable()) {
                                          int posSection = getIndex(nx, nyChunk & 15, nz);
                                          int block = section.getBlock(posSection);
                                          if (section.isLeaf(block)) {
                                             int oldDistance = section.getDistance(block);
                                             if (oldDistance > ix) {
                                                section.setDistance(posSection, block, ix);
                                                newSet.add(getIndex(nx, nyChunk, nz));
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }

                           return sections.updateTyped(
                              sectionFinder,
                              sectionx -> ((LeavesFix$LeavesSection)sectionMap.get(((Dynamic)sectionx.get(DSL.remainderFinder())).get("Y").asInt(0)))
                                 .write(sectionx)
                           );
                        }
                     }
                  );
                  if (sides[0] != 0) {
                     newLevel = newLevel.update(DSL.remainderFinder(), tag -> {
                        Dynamic<?> upgradeData = (Dynamic<?>)DataFixUtils.orElse(tag.get("UpgradeData").result(), tag.emptyMap());
                        return tag.set("UpgradeData", upgradeData.set("Sides", tag.createByte((byte)(upgradeData.get("Sides").asByte((byte)0) | sides[0]))));
                     });
                  }

                  return newLevel;
               }
            )
         );
      }
   }

   public static int getIndex(final int x, final int y, final int z) {
      return y << 8 | z << 4 | x;
   }

   private int getX(final int index) {
      return index & 15;
   }

   private int getY(final int index) {
      return index >> 8 & 0xFF;
   }

   private int getZ(final int index) {
      return index >> 4 & 15;
   }

   public static int getSideMask(final boolean west, final boolean east, final boolean north, final boolean south) {
      int s = 0;
      if (north) {
         if (east) {
            s |= 2;
         } else if (west) {
            s |= 128;
         } else {
            s |= 1;
         }
      } else if (south) {
         if (west) {
            s |= 32;
         } else if (east) {
            s |= 8;
         } else {
            s |= 16;
         }
      } else if (east) {
         s |= 4;
      } else if (west) {
         s |= 64;
      }

      return s;
   }
}
