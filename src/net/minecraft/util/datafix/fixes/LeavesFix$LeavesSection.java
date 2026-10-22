package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.Objects;
import net.minecraft.util.datafix.PackedBitStorage;
import org.jspecify.annotations.Nullable;

public final class LeavesFix$LeavesSection extends LeavesFix$Section {
   private static final String PERSISTENT = "persistent";
   private static final String DECAYABLE = "decayable";
   private static final String DISTANCE = "distance";
   @Nullable
   private IntSet leaveIds;
   @Nullable
   private IntSet logIds;
   @Nullable
   private Int2IntMap stateToIdMap;

   public LeavesFix$LeavesSection(final Typed<?> section, final Schema inputSchema) {
      super(section, inputSchema);
   }

   @Override
   protected boolean skippable() {
      this.leaveIds = new IntOpenHashSet();
      this.logIds = new IntOpenHashSet();
      this.stateToIdMap = new Int2IntOpenHashMap();

      for (int i = 0; i < this.palette.size(); i++) {
         Dynamic<?> paletteTag = this.palette.get(i);
         String blockName = paletteTag.get("Name").asString("");
         if (LeavesFix.LEAVES.containsKey(blockName)) {
            boolean persistent = Objects.equals(paletteTag.get("Properties").get("decayable").asString(""), "false");
            this.leaveIds.add(i);
            this.stateToIdMap.put(getStateId(blockName, persistent, 7), i);
            this.palette.set(i, this.makeLeafTag(paletteTag, blockName, persistent, 7));
         }

         if (LeavesFix.LOGS.contains(blockName)) {
            this.logIds.add(i);
         }
      }

      return this.leaveIds.isEmpty() && this.logIds.isEmpty();
   }

   private Dynamic<?> makeLeafTag(final Dynamic<?> input, final String blockName, final boolean persistent, final int distance) {
      Dynamic<?> properties = input.emptyMap();
      properties = properties.set("persistent", properties.createString(persistent ? "true" : "false"));
      properties = properties.set("distance", properties.createString(Integer.toString(distance)));
      Dynamic<?> tag = input.emptyMap();
      tag = tag.set("Properties", properties);
      return tag.set("Name", tag.createString(blockName));
   }

   public boolean isLog(final int block) {
      return this.logIds.contains(block);
   }

   public boolean isLeaf(final int block) {
      return this.leaveIds.contains(block);
   }

   private int getDistance(final int block) {
      return this.isLog(block) ? 0 : Integer.parseInt(this.palette.get(block).get("Properties").get("distance").asString(""));
   }

   private void setDistance(final int pos, final int block, final int distance) {
      Dynamic<?> baseTag = this.palette.get(block);
      String blockName = baseTag.get("Name").asString("");
      boolean persistent = Objects.equals(baseTag.get("Properties").get("persistent").asString(""), "true");
      int stateId = getStateId(blockName, persistent, distance);
      if (!this.stateToIdMap.containsKey(stateId)) {
         int id = this.palette.size();
         this.leaveIds.add(id);
         this.stateToIdMap.put(stateId, id);
         this.palette.add(this.makeLeafTag(baseTag, blockName, persistent, distance));
      }

      int id = this.stateToIdMap.get(stateId);
      if (1 << this.storage.getBits() <= id) {
         PackedBitStorage newStorage = new PackedBitStorage(this.storage.getBits() + 1, 4096);

         for (int i = 0; i < 4096; i++) {
            newStorage.set(i, this.storage.get(i));
         }

         this.storage = newStorage;
      }

      this.storage.set(pos, id);
   }
}
