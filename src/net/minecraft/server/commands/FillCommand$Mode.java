package net.minecraft.server.commands;

enum FillCommand$Mode {
   REPLACE(FillCommand$Affector.NOOP, FillCommand$Filter.NOOP),
   OUTLINE(
      FillCommand$Affector.NOOP,
      (r, p, b, l) -> p.getX() != r.minX()
            && p.getX() != r.maxX()
            && p.getY() != r.minY()
            && p.getY() != r.maxY()
            && p.getZ() != r.minZ()
            && p.getZ() != r.maxZ()
         ? null
         : b
   ),
   HOLLOW(
      FillCommand$Affector.NOOP,
      (r, p, b, l) -> p.getX() != r.minX()
            && p.getX() != r.maxX()
            && p.getY() != r.minY()
            && p.getY() != r.maxY()
            && p.getZ() != r.minZ()
            && p.getZ() != r.maxZ()
         ? FillCommand.HOLLOW_CORE
         : b
   ),
   DESTROY((l, p) -> l.destroyBlock(p, true), FillCommand$Filter.NOOP);

   public final FillCommand$Filter filter;
   public final FillCommand$Affector affector;

   private FillCommand$Mode(final FillCommand$Affector affector, final FillCommand$Filter filter) {
      this.affector = affector;
      this.filter = filter;
   }
}
