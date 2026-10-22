package net.minecraft.client.renderer;

import com.mojang.blaze3d.framegraph.FrameGraphBuilder$Inspector;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.profiling.ProfilerFiller;

@Environment(EnvType.CLIENT)
class LevelRenderer$1 implements FrameGraphBuilder$Inspector {
   LevelRenderer$1(final LevelRenderer this$0, final ProfilerFiller val$profiler) {
      this.val$profiler = val$profilerx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public void beforeExecutePass(final String name) {
      this.val$profiler.push(name);
   }

   @Override
   public void afterExecutePass(final String name) {
      this.val$profiler.pop();
   }
}
