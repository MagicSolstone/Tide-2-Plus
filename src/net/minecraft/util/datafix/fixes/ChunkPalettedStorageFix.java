package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
import org.slf4j.Logger;

public class ChunkPalettedStorageFix extends DataFix {
   private static final int NORTH_WEST_MASK = 128;
   private static final int WEST_MASK = 64;
   private static final int SOUTH_WEST_MASK = 32;
   private static final int SOUTH_MASK = 16;
   private static final int SOUTH_EAST_MASK = 8;
   private static final int EAST_MASK = 4;
   private static final int NORTH_EAST_MASK = 2;
   private static final int NORTH_MASK = 1;
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final int SIZE = 4096;

   public ChunkPalettedStorageFix(final Schema outputSchema, final boolean changesType) {
      super(outputSchema, changesType);
   }

   private static Dynamic<?> legacyBlockState(final String id, final Map<String, String> properties) {
      Dynamic<Tag> dynamic = new Dynamic(NbtOps.INSTANCE, new CompoundTag());
      Dynamic<Tag> blockState = dynamic.set("Name", dynamic.createString(id));
      if (!properties.isEmpty()) {
         blockState = blockState.set(
            "Properties",
            dynamic.createMap(
               properties.entrySet()
                  .stream()
                  .collect(Collectors.toMap(entry -> dynamic.createString(entry.getKey()), entry -> dynamic.createString(entry.getValue())))
            )
         );
      }

      return blockState;
   }

   private static Dynamic<?> legacyBlockState(final String id) {
      return legacyBlockState(id, Map.of());
   }

   public static String getName(final Dynamic<?> state) {
      return state.get("Name").asString("");
   }

   public static String getProperty(final Dynamic<?> state, final String property) {
      return state.get("Properties").get(property).asString("");
   }

   public static int idFor(final CrudeIncrementalIntIdentityHashBiMap<Dynamic<?>> states, final Dynamic<?> state) {
      int id = states.getId(state);
      if (id == -1) {
         id = states.add(state);
      }

      return id;
   }

   private Dynamic<?> fix(final Dynamic<?> input) {
      Optional<? extends Dynamic<?>> level = input.get("Level").result();
      return level.isPresent() && level.get().get("Sections").asStreamOpt().result().isPresent()
         ? input.set("Level", new ChunkPalettedStorageFix$UpgradeChunk((Dynamic<?>)level.get()).write())
         : input;
   }

   public TypeRewriteRule makeRule() {
      Type<?> oldType = this.getInputSchema().getType(References.CHUNK);
      Type<?> newType = this.getOutputSchema().getType(References.CHUNK);
      return this.writeFixAndRead("ChunkPalettedStorageFix", oldType, newType, this::fix);
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
