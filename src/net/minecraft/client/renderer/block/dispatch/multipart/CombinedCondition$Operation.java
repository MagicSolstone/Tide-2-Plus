package net.minecraft.client.renderer.block.dispatch.multipart;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.StringRepresentable;

@Environment(EnvType.CLIENT)
public enum CombinedCondition$Operation implements StringRepresentable {
   AND("AND"),
   OR("OR");

   public static final Codec<CombinedCondition$Operation> CODEC = StringRepresentable.fromEnum(CombinedCondition$Operation::values);
   private final String name;

   private CombinedCondition$Operation(final String name) {
      this.name = name;
   }

   @Override
   public String getSerializedName() {
      return this.name;
   }

   public abstract <V> Predicate<V> apply(List<Predicate<V>> terms);
}
