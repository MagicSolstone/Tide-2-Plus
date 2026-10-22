package net.minecraft.client.renderer.block.dispatch.multipart;

import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public record MultiPartModel$Selector<T>(Predicate<BlockState> condition, T model) {
   public <S> MultiPartModel$Selector<S> with(final S newModel) {
      return new MultiPartModel$Selector<>(this.condition, newModel);
   }
}
