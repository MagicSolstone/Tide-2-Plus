package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

public final class TrappedChestBlockEntityFix$TrappedChestSection extends LeavesFix$Section {
   @Nullable
   private IntSet chestIds;

   public TrappedChestBlockEntityFix$TrappedChestSection(final Typed<?> section, final Schema inputSchema) {
      super(section, inputSchema);
   }

   @Override
   protected boolean skippable() {
      this.chestIds = new IntOpenHashSet();

      for (int i = 0; i < this.palette.size(); i++) {
         Dynamic<?> paletteTag = this.palette.get(i);
         String blockName = paletteTag.get("Name").asString("");
         if (Objects.equals(blockName, "minecraft:trapped_chest")) {
            this.chestIds.add(i);
         }
      }

      return this.chestIds.isEmpty();
   }

   public boolean isTrappedChest(final int block) {
      return this.chestIds.contains(block);
   }
}
