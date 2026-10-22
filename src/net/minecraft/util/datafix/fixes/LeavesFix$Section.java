package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.util.datafix.PackedBitStorage;
import org.jspecify.annotations.Nullable;

public abstract class LeavesFix$Section {
   protected static final String BLOCK_STATES_TAG = "BlockStates";
   protected static final String NAME_TAG = "Name";
   protected static final String PROPERTIES_TAG = "Properties";
   private final Type<Pair<String, Dynamic<?>>> blockStateType = DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType());
   protected final OpticFinder<List<Pair<String, Dynamic<?>>>> paletteFinder = DSL.fieldFinder("Palette", DSL.list(this.blockStateType));
   protected final List<Dynamic<?>> palette;
   protected final int index;
   @Nullable
   protected PackedBitStorage storage;

   public LeavesFix$Section(final Typed<?> section, final Schema inputSchema) {
      if (!Objects.equals(inputSchema.getType(References.BLOCK_STATE), this.blockStateType)) {
         throw new IllegalStateException("Block state type is not what was expected.");
      } else {
         Optional<List<Pair<String, Dynamic<?>>>> typedPalette = section.getOptional(this.paletteFinder);
         this.palette = typedPalette.<List<Dynamic<?>>>map(p -> p.stream().<Dynamic<?>>map(Pair::getSecond).collect(Collectors.toList()))
            .orElse(ImmutableList.of());
         Dynamic<?> tag = (Dynamic<?>)section.get(DSL.remainderFinder());
         this.index = tag.get("Y").asInt(0);
         this.readStorage(tag);
      }
   }

   protected void readStorage(final Dynamic<?> tag) {
      if (this.skippable()) {
         this.storage = null;
      } else {
         long[] states = tag.get("BlockStates").asLongStream().toArray();
         int size = Math.max(4, DataFixUtils.ceillog2(this.palette.size()));
         this.storage = new PackedBitStorage(size, 4096, states);
      }
   }

   public Typed<?> write(final Typed<?> section) {
      return this.isSkippable()
         ? section
         : section.update(DSL.remainderFinder(), tag -> tag.set("BlockStates", tag.createLongList(Arrays.stream(this.storage.getRaw()))))
            .set(this.paletteFinder, this.palette.stream().map(b -> Pair.of(References.BLOCK_STATE.typeName(), b)).collect(Collectors.toList()));
   }

   public boolean isSkippable() {
      return this.storage == null;
   }

   public int getBlock(final int pos) {
      return this.storage.get(pos);
   }

   protected static int getStateId(final String blockName, final boolean persistent, final int distance) {
      return LeavesFix.LEAVES.get(blockName) << 5 | (persistent ? 16 : 0) | distance;
   }

   public int getIndex() {
      return this.index;
   }

   protected abstract boolean skippable();
}
