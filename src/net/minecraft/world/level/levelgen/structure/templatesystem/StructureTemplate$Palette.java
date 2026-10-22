package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.Nullable;

public final class StructureTemplate$Palette {
   private final List<StructureTemplate$StructureBlockInfo> blocks;
   private final Map<Block, List<StructureTemplate$StructureBlockInfo>> cache = Maps.newHashMap();
   @Nullable
   private List<StructureTemplate$JigsawBlockInfo> cachedJigsaws;

   private StructureTemplate$Palette(final List<StructureTemplate$StructureBlockInfo> blocks) {
      this.blocks = blocks;
   }

   public List<StructureTemplate$JigsawBlockInfo> jigsaws() {
      if (this.cachedJigsaws == null) {
         this.cachedJigsaws = this.blocks(Blocks.JIGSAW).stream().map(StructureTemplate$JigsawBlockInfo::parse).toList();
      }

      return this.cachedJigsaws;
   }

   public List<StructureTemplate$StructureBlockInfo> blocks() {
      return this.blocks;
   }

   public List<StructureTemplate$StructureBlockInfo> blocks(final Block filter) {
      return this.cache.computeIfAbsent(filter, block -> this.blocks.stream().filter(b -> b.state.is(block)).collect(Collectors.toList()));
   }
}
