package net.minecraft.gizmos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.util.Util;

public class SimpleGizmoCollector implements GizmoCollector {
   private final List<SimpleGizmoCollector$GizmoInstance> gizmos = new ArrayList<>();
   private final List<SimpleGizmoCollector$GizmoInstance> temporaryGizmos = new ArrayList<>();

   @Override
   public GizmoProperties add(final Gizmo gizmo) {
      SimpleGizmoCollector$GizmoInstance instance = new SimpleGizmoCollector$GizmoInstance(gizmo);
      this.gizmos.add(instance);
      return instance;
   }

   public List<SimpleGizmoCollector$GizmoInstance> drainGizmos() {
      ArrayList<SimpleGizmoCollector$GizmoInstance> result = new ArrayList<>(this.gizmos);
      result.addAll(this.temporaryGizmos);
      long currentMillis = Util.getMillis();
      this.gizmos.removeIf(gizmo -> gizmo.getExpireTimeMillis() < currentMillis);
      this.temporaryGizmos.clear();
      return result;
   }

   public List<SimpleGizmoCollector$GizmoInstance> getGizmos() {
      return this.gizmos;
   }

   public void addTemporaryGizmos(final Collection<SimpleGizmoCollector$GizmoInstance> gizmos) {
      this.temporaryGizmos.addAll(gizmos);
   }
}
