package net.minecraft.world.entity;

import java.util.Optional;
import net.minecraft.world.phys.Vec3;

record Entity$Movement(Vec3 from, Vec3 to, Optional<Vec3> axisDependentOriginalMovement) {
   public Entity$Movement(final Vec3 from, final Vec3 to, final Vec3 axisDependentOriginalMovement) {
      this(from, to, Optional.of(axisDependentOriginalMovement));
   }

   public Entity$Movement(final Vec3 from, final Vec3 to) {
      this(from, to, Optional.empty());
   }
}
