package net.minecraft.server.permissions;

class LevelBasedPermissionSet$1 implements LevelBasedPermissionSet {
   LevelBasedPermissionSet$1(final PermissionLevel val$level) {
      this.val$level = val$levelx;
   }

   @Override
   public PermissionLevel level() {
      return this.val$level;
   }

   @Override
   public String toString() {
      return "permission level: " + this.val$level.name();
   }
}
