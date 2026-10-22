package net.minecraft.commands.arguments;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.function.BiFunction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public enum EntityAnchorArgument$Anchor {
   FEET("feet", (p, e) -> p),
   EYES("eyes", (p, e) -> new Vec3(p.x, p.y + e.getEyeHeight(), p.z));

   private static final Map<String, EntityAnchorArgument$Anchor> BY_NAME = Util.make(Maps.newHashMap(), map -> {
      for (EntityAnchorArgument$Anchor anchor : values()) {
         map.put(anchor.name, anchor);
      }
   });
   private final String name;
   private final BiFunction<Vec3, Entity, Vec3> transform;

   private EntityAnchorArgument$Anchor(final String name, final BiFunction<Vec3, Entity, Vec3> transform) {
      this.name = name;
      this.transform = transform;
   }

   @Nullable
   public static EntityAnchorArgument$Anchor getByName(final String name) {
      return BY_NAME.get(name);
   }

   public Vec3 apply(final Entity entity) {
      return this.transform.apply(entity.position(), entity);
   }

   public Vec3 apply(final CommandSourceStack source) {
      Entity entity = source.getEntity();
      return entity == null ? source.getPosition() : this.transform.apply(source.getPosition(), entity);
   }
}
