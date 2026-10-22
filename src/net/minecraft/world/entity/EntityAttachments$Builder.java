package net.minecraft.world.entity;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;

public class EntityAttachments$Builder {
   private final Map<EntityAttachment, List<Vec3>> attachments = new EnumMap<>(EntityAttachment.class);

   private EntityAttachments$Builder() {
   }

   public EntityAttachments$Builder attach(final EntityAttachment attachment, final float x, final float y, final float z) {
      return this.attach(attachment, new Vec3(x, y, z));
   }

   public EntityAttachments$Builder attach(final EntityAttachment attachment, final Vec3 point) {
      this.attachments.computeIfAbsent(attachment, a -> new ArrayList<>(1)).add(point);
      return this;
   }

   public EntityAttachments build(final float width, final float height) {
      Map<EntityAttachment, List<Vec3>> attachments = Util.makeEnumMap(EntityAttachment.class, attachment -> {
         List<Vec3> points = this.attachments.get(attachment);
         return points == null ? attachment.createFallbackPoints(width, height) : List.copyOf(points);
      });
      return new EntityAttachments(attachments);
   }
}
