package net.minecraft.client.renderer.block.dispatch;

import com.mojang.math.Quadrant;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record Variant$SimpleModelState(Quadrant x, Quadrant y, Quadrant z, boolean uvLock) {
   public static final MapCodec<Variant$SimpleModelState> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Quadrant.CODEC.optionalFieldOf("x", Quadrant.R0).forGetter(Variant$SimpleModelState::x),
            Quadrant.CODEC.optionalFieldOf("y", Quadrant.R0).forGetter(Variant$SimpleModelState::y),
            Quadrant.CODEC.optionalFieldOf("z", Quadrant.R0).forGetter(Variant$SimpleModelState::z),
            Codec.BOOL.optionalFieldOf("uvlock", false).forGetter(Variant$SimpleModelState::uvLock)
         )
         .apply(i, Variant$SimpleModelState::new)
   );
   public static final Variant$SimpleModelState DEFAULT = new Variant$SimpleModelState(Quadrant.R0, Quadrant.R0, Quadrant.R0, false);

   public ModelState asModelState() {
      BlockModelRotation rotation = BlockModelRotation.get(Quadrant.fromXYZAngles(this.x, this.y, this.z));
      return (ModelState)(this.uvLock ? rotation.withUvLock() : rotation);
   }

   public Variant$SimpleModelState withX(final Quadrant x) {
      return new Variant$SimpleModelState(x, this.y, this.z, this.uvLock);
   }

   public Variant$SimpleModelState withY(final Quadrant y) {
      return new Variant$SimpleModelState(this.x, y, this.z, this.uvLock);
   }

   public Variant$SimpleModelState withZ(final Quadrant z) {
      return new Variant$SimpleModelState(this.x, this.y, z, this.uvLock);
   }

   public Variant$SimpleModelState withUvLock(final boolean uvLock) {
      return new Variant$SimpleModelState(this.x, this.y, this.z, uvLock);
   }
}
