package net.minecraft.world.level.gameevent;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public record GameEvent$Context(@Nullable Entity sourceEntity, @Nullable BlockState affectedState) {
   public static GameEvent$Context of(@Nullable final Entity sourceEntity) {
      return new GameEvent$Context(sourceEntity, null);
   }

   public static GameEvent$Context of(@Nullable final BlockState state) {
      return new GameEvent$Context(null, state);
   }

   public static GameEvent$Context of(@Nullable final Entity sourceEntity, @Nullable final BlockState state) {
      return new GameEvent$Context(sourceEntity, state);
   }
}
