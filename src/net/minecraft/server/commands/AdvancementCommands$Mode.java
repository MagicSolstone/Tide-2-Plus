package net.minecraft.server.commands;

enum AdvancementCommands$Mode {
   ONLY(false, false),
   THROUGH(true, true),
   FROM(false, true),
   UNTIL(true, false),
   EVERYTHING(true, true);

   private final boolean parents;
   private final boolean children;

   private AdvancementCommands$Mode(final boolean parents, final boolean children) {
      this.parents = parents;
      this.children = children;
   }
}
