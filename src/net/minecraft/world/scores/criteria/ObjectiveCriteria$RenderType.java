package net.minecraft.world.scores.criteria;

import net.minecraft.util.StringRepresentable;
import net.minecraft.util.StringRepresentable$EnumCodec;

public enum ObjectiveCriteria$RenderType implements StringRepresentable {
   INTEGER("integer"),
   HEARTS("hearts");

   private final String id;
   public static final StringRepresentable$EnumCodec<ObjectiveCriteria$RenderType> CODEC = StringRepresentable.fromEnum(ObjectiveCriteria$RenderType::values);

   private ObjectiveCriteria$RenderType(final String id) {
      this.id = id;
   }

   public String getId() {
      return this.id;
   }

   @Override
   public String getSerializedName() {
      return this.id;
   }

   public static ObjectiveCriteria$RenderType byId(final String key) {
      return CODEC.byName(key, INTEGER);
   }
}
