package net.minecraft.client.renderer.block.dispatch;

import com.mojang.math.OctahedralGroup;
import com.mojang.math.Transformation;
import java.util.EnumMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockMath;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
public class BlockModelRotation implements ModelState {
   private static final Map<OctahedralGroup, BlockModelRotation> BY_GROUP_ORDINAL = Util.makeEnumMap(OctahedralGroup.class, BlockModelRotation::new);
   public static final BlockModelRotation IDENTITY = get(OctahedralGroup.IDENTITY);
   private final OctahedralGroup orientation;
   private final Transformation transformation;
   private final Map<Direction, Matrix4fc> faceMapping = new EnumMap<>(Direction.class);
   private final Map<Direction, Matrix4fc> inverseFaceMapping = new EnumMap<>(Direction.class);
   private final BlockModelRotation$WithUvLock withUvLock = new BlockModelRotation$WithUvLock(this);

   private BlockModelRotation(final OctahedralGroup orientation) {
      this.orientation = orientation;
      if (orientation != OctahedralGroup.IDENTITY) {
         this.transformation = new Transformation(new Matrix4f(orientation.transformation()));
      } else {
         this.transformation = Transformation.IDENTITY;
      }

      for (Direction face : Direction.values()) {
         Matrix4fc faceTransform = BlockMath.getFaceTransformation(this.transformation, face).getMatrix();
         this.faceMapping.put(face, faceTransform);
         this.inverseFaceMapping.put(face, faceTransform.invertAffine(new Matrix4f()));
      }
   }

   @Override
   public Transformation transformation() {
      return this.transformation;
   }

   public static BlockModelRotation get(final OctahedralGroup group) {
      return BY_GROUP_ORDINAL.get(group);
   }

   public ModelState withUvLock() {
      return this.withUvLock;
   }

   @Override
   public String toString() {
      return "simple[" + this.orientation.getSerializedName() + "]";
   }
}
