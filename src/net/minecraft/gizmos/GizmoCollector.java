package net.minecraft.gizmos;

public interface GizmoCollector {
   GizmoProperties IGNORED = new GizmoCollector$1();
   GizmoCollector NOOP = gizmo -> IGNORED;

   GizmoProperties add(final Gizmo gizmo);
}
