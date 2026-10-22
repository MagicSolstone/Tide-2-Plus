package net.minecraft.client.gui.components.debug;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.TypedInstance;
import net.minecraft.world.level.Level;

@Environment(EnvType.CLIENT)
public abstract class DebugEntryLookingAt$DebugEntryLookingAtTags<T extends TypedInstance<?>> extends DebugEntryLookingAt {
   protected abstract T getInstance(Level level, BlockPos pos);

   @Override
   public void extractInfo(final List<String> result, final Level level, final BlockPos pos) {
      T instance = this.getInstance(level, pos);
      addTagEntries(result, instance);
   }
}
