package net.minecraft.world.entity;

import com.mojang.serialization.Codec;
import java.util.function.IntFunction;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ByIdMap$OutOfBoundsStrategy;
import net.minecraft.util.StringRepresentable;

public enum Display$BillboardConstraints implements StringRepresentable {
   FIXED((byte)0, "fixed"),
   VERTICAL((byte)1, "vertical"),
   HORIZONTAL((byte)2, "horizontal"),
   CENTER((byte)3, "center");

   public static final Codec<Display$BillboardConstraints> CODEC = StringRepresentable.fromEnum(Display$BillboardConstraints::values);
   public static final IntFunction<Display$BillboardConstraints> BY_ID = ByIdMap.continuous(
      Display$BillboardConstraints::getId, values(), ByIdMap$OutOfBoundsStrategy.ZERO
   );
   private final byte id;
   private final String name;

   private Display$BillboardConstraints(final byte id, final String name) {
      this.name = name;
      this.id = id;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   private byte getId() {
      return this.id;
   }
}
