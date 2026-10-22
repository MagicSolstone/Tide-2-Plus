package net.minecraft.commands;

public enum Commands$CommandSelection {
   ALL(true, true),
   DEDICATED(false, true),
   INTEGRATED(true, false);

   private final boolean includeIntegrated;
   private final boolean includeDedicated;

   private Commands$CommandSelection(final boolean includeIntegrated, final boolean includeDedicated) {
      this.includeIntegrated = includeIntegrated;
      this.includeDedicated = includeDedicated;
   }
}
