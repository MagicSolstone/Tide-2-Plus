package net.minecraft.util.parsing.packrat;

class CachedParseState$SimpleControl implements Control {
   private boolean hasCut;

   private CachedParseState$SimpleControl() {
   }

   @Override
   public void cut() {
      this.hasCut = true;
   }

   @Override
   public boolean hasCut() {
      return this.hasCut;
   }

   public void reset() {
      this.hasCut = false;
   }
}
