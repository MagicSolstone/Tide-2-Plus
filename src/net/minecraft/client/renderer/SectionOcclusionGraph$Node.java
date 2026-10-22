package net.minecraft.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection;
import net.minecraft.core.Direction;
import net.minecraft.util.VisibleForDebug;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
@VisibleForDebug
public class SectionOcclusionGraph$Node {
   @VisibleForDebug
   protected final SectionRenderDispatcher$RenderSection section;
   private byte sourceDirections;
   private byte directions;
   @VisibleForDebug
   public final int step;

   private SectionOcclusionGraph$Node(final SectionRenderDispatcher$RenderSection section, @Nullable final Direction sourceDirection, final int step) {
      this.section = section;
      if (sourceDirection != null) {
         this.addSourceDirection(sourceDirection);
      }

      this.step = step;
   }

   private void setDirections(final byte oldDirections, final Direction direction) {
      this.directions = (byte)(this.directions | oldDirections | 1 << direction.ordinal());
   }

   private boolean hasDirection(final Direction direction) {
      return (this.directions & 1 << direction.ordinal()) > 0;
   }

   private void addSourceDirection(final Direction direction) {
      this.sourceDirections = (byte)(this.sourceDirections | this.sourceDirections | 1 << direction.ordinal());
   }

   @VisibleForDebug
   public boolean hasSourceDirection(final int directionOrdinal) {
      return (this.sourceDirections & 1 << directionOrdinal) > 0;
   }

   private boolean hasSourceDirections() {
      return this.sourceDirections != 0;
   }

   @Override
   public int hashCode() {
      return Long.hashCode(this.section.getSectionNode());
   }

   @Override
   public boolean equals(final Object obj) {
      return obj instanceof SectionOcclusionGraph$Node other ? this.section.getSectionNode() == other.section.getSectionNode() : false;
   }
}
