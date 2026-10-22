package net.minecraft.client.renderer.chunk;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.core.BlockPos;

@Environment(EnvType.CLIENT)
public abstract class SectionRenderDispatcher$RenderSection$SectionTask {
   protected final AtomicBoolean isCancelled;
   protected final AtomicBoolean isCompleted;
   private final boolean isRecompile;

   public SectionRenderDispatcher$RenderSection$SectionTask(final SectionRenderDispatcher$RenderSection this$1, final boolean isRecompile) {
      Objects.requireNonNull(this$1);
      this.this$1 = this$1;
      super();
      this.isCancelled = new AtomicBoolean(false);
      this.isCompleted = new AtomicBoolean(false);
      this.isRecompile = isRecompile;
   }

   public abstract SectionRenderDispatcher$RenderSection$SectionTask$SectionTaskResult doTask(final SectionBufferBuilderPack buffers);

   public abstract void cancel();

   public boolean isRecompile() {
      return this.isRecompile;
   }

   public BlockPos getRenderOrigin() {
      return this.this$1.renderOrigin;
   }
}
