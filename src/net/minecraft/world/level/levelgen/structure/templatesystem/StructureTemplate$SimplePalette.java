package net.minecraft.world.level.levelgen.structure.templatesystem;

import java.util.Iterator;
import net.minecraft.core.IdMapper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

class StructureTemplate$SimplePalette implements Iterable<BlockState> {
   public static final BlockState DEFAULT_BLOCK_STATE = Blocks.AIR.defaultBlockState();
   private final IdMapper<BlockState> ids = new IdMapper<>(16);
   private int lastId;

   private StructureTemplate$SimplePalette() {
   }

   public int idFor(final BlockState state) {
      int id = this.ids.getId(state);
      if (id == -1) {
         id = this.lastId++;
         this.ids.addMapping(state, id);
      }

      return id;
   }

   @Nullable
   public BlockState stateFor(final int index) {
      BlockState blockState = this.ids.byId(index);
      return blockState == null ? DEFAULT_BLOCK_STATE : blockState;
   }

   @Override
   public Iterator<BlockState> iterator() {
      return this.ids.iterator();
   }

   public void addMapping(final BlockState state, final int id) {
      this.ids.addMapping(state, id);
   }
}
