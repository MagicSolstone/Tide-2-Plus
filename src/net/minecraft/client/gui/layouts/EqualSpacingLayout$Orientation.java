package net.minecraft.client.gui.layouts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum EqualSpacingLayout$Orientation {
   HORIZONTAL,
   VERTICAL;

   private int getPrimaryLength(final LayoutElement widget) {
      return switch (this) {
         case HORIZONTAL -> widget.getWidth();
         case VERTICAL -> widget.getHeight();
      };
   }

   private int getPrimaryLength(final EqualSpacingLayout$ChildContainer childContainer) {
      return switch (this) {
         case HORIZONTAL -> childContainer.getWidth();
         case VERTICAL -> childContainer.getHeight();
      };
   }

   private int getSecondaryLength(final LayoutElement widget) {
      return switch (this) {
         case HORIZONTAL -> widget.getHeight();
         case VERTICAL -> widget.getWidth();
      };
   }

   private int getSecondaryLength(final EqualSpacingLayout$ChildContainer childContainer) {
      return switch (this) {
         case HORIZONTAL -> childContainer.getHeight();
         case VERTICAL -> childContainer.getWidth();
      };
   }

   private void setPrimaryPosition(final EqualSpacingLayout$ChildContainer childContainer, final int position) {
      switch (this) {
         case HORIZONTAL:
            childContainer.setX(position, childContainer.getWidth());
            break;
         case VERTICAL:
            childContainer.setY(position, childContainer.getHeight());
      }
   }

   private void setSecondaryPosition(final EqualSpacingLayout$ChildContainer childContainer, final int position, final int availableSpace) {
      switch (this) {
         case HORIZONTAL:
            childContainer.setY(position, availableSpace);
            break;
         case VERTICAL:
            childContainer.setX(position, availableSpace);
      }
   }

   private int getPrimaryPosition(final LayoutElement widget) {
      return switch (this) {
         case HORIZONTAL -> widget.getX();
         case VERTICAL -> widget.getY();
      };
   }

   private int getSecondaryPosition(final LayoutElement widget) {
      return switch (this) {
         case HORIZONTAL -> widget.getY();
         case VERTICAL -> widget.getX();
      };
   }
}
