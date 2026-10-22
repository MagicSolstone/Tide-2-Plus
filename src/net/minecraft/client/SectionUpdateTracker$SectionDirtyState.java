package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SectionUpdateTracker$SectionDirtyState implements RotatingSectionStorage$Value {
   private boolean isDirty;
   private boolean isDirtyFromPlayer;
   private long sectionNode;

   private SectionUpdateTracker$SectionDirtyState(final boolean isDirty, final boolean isDirtyFromPlayer, final long sectionNode) {
      this.isDirty = isDirty;
      this.isDirtyFromPlayer = isDirtyFromPlayer;
      this.sectionNode = sectionNode;
   }

   public void setDirty(final boolean fromPlayer) {
      boolean wasDirty = this.isDirty;
      this.isDirty = true;
      this.isDirtyFromPlayer = fromPlayer | (wasDirty && this.isDirtyFromPlayer);
   }

   public void setNotDirty() {
      this.isDirty = false;
      this.isDirtyFromPlayer = false;
   }

   @Override
   public void setSectionNode(final long sectionNode) {
      if (this.sectionNode != sectionNode) {
         this.sectionNode = sectionNode;
         this.isDirty = true;
         this.isDirtyFromPlayer = false;
      }
   }

   @Override
   public long getSectionNode() {
      return this.sectionNode;
   }

   public boolean isDirty() {
      return this.isDirty;
   }

   public boolean isDirtyFromPlayer() {
      return this.isDirtyFromPlayer;
   }
}
