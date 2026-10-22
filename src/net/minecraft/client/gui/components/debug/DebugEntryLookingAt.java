package net.minecraft.client.gui.components.debug;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.TypedInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult$Type;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class DebugEntryLookingAt implements DebugScreenEntry {
   private static final int RANGE = 20;
   private static final Identifier BLOCK_GROUP = Identifier.withDefaultNamespace("looking_at_block");
   private static final Identifier FLUID_GROUP = Identifier.withDefaultNamespace("looking_at_fluid");

   @Override
   public void display(
      final DebugScreenDisplayer displayer,
      @Nullable final Level serverOrClientLevel,
      @Nullable final LevelChunk clientChunk,
      @Nullable final LevelChunk serverChunk
   ) {
      Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
      Level clientOrServerLevel = (Level)(SharedConstants.DEBUG_SHOW_SERVER_DEBUG_VALUES ? serverOrClientLevel : Minecraft.getInstance().level);
      if (cameraEntity != null && clientOrServerLevel != null) {
         HitResult block = this.getHitResult(cameraEntity);
         List<String> result = new ArrayList<>();
         if (block.getType() == HitResult$Type.BLOCK) {
            BlockPos pos = ((BlockHitResult)block).getBlockPos();
            this.extractInfo(result, clientOrServerLevel, pos);
         }

         displayer.addToGroup(this.group(), result);
      }
   }

   public abstract HitResult getHitResult(final Entity cameraEntity);

   public abstract void extractInfo(List<String> result, Level level, BlockPos pos);

   public abstract Identifier group();

   public static void addTagEntries(final List<String> result, final TypedInstance<?> instance) {
      instance.tags().map(e -> "#" + e.location()).forEach(result::add);
   }
}
