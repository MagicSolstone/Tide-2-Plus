package net.minecraft.client.model.geom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack$Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public final class ModelPart {
   public static final float DEFAULT_SCALE = 1.0F;
   public float x;
   public float y;
   public float z;
   public float xRot;
   public float yRot;
   public float zRot;
   public float xScale = 1.0F;
   public float yScale = 1.0F;
   public float zScale = 1.0F;
   public boolean visible = true;
   public boolean skipDraw;
   private final List<ModelPart$Cube> cubes;
   private final Map<String, ModelPart> children;
   private PartPose initialPose = PartPose.ZERO;

   public ModelPart(final List<ModelPart$Cube> cubes, final Map<String, ModelPart> children) {
      this.cubes = cubes;
      this.children = children;
   }

   public PartPose storePose() {
      return PartPose.offsetAndRotation(this.x, this.y, this.z, this.xRot, this.yRot, this.zRot);
   }

   public PartPose getInitialPose() {
      return this.initialPose;
   }

   public void setInitialPose(final PartPose initialPose) {
      this.initialPose = initialPose;
   }

   public void resetPose() {
      this.loadPose(this.initialPose);
   }

   public void loadPose(final PartPose pose) {
      this.x = pose.x();
      this.y = pose.y();
      this.z = pose.z();
      this.xRot = pose.xRot();
      this.yRot = pose.yRot();
      this.zRot = pose.zRot();
      this.xScale = pose.xScale();
      this.yScale = pose.yScale();
      this.zScale = pose.zScale();
   }

   public boolean hasChild(final String name) {
      return this.children.containsKey(name);
   }

   public ModelPart getChild(final String name) {
      ModelPart result = this.children.get(name);
      if (result == null) {
         throw new NoSuchElementException("Can't find part " + name);
      } else {
         return result;
      }
   }

   public void setPos(final float x, final float y, final float z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public void setRotation(final float xRot, final float yRot, final float zRot) {
      this.xRot = xRot;
      this.yRot = yRot;
      this.zRot = zRot;
   }

   public void render(final PoseStack poseStack, final VertexConsumer buffer, final int lightCoords, final int overlayCoords) {
      this.render(poseStack, buffer, lightCoords, overlayCoords, -1);
   }

   public void render(final PoseStack poseStack, final VertexConsumer buffer, final int lightCoords, final int overlayCoords, final int color) {
      if (this.visible) {
         if (!this.cubes.isEmpty() || !this.children.isEmpty()) {
            poseStack.pushPose();
            this.translateAndRotate(poseStack);
            if (!this.skipDraw) {
               this.compile(poseStack.last(), buffer, lightCoords, overlayCoords, color);
            }

            for (ModelPart child : this.children.values()) {
               child.render(poseStack, buffer, lightCoords, overlayCoords, color);
            }

            poseStack.popPose();
         }
      }
   }

   public void rotateBy(final Quaternionf rotation) {
      Matrix3f oldRotation = new Matrix3f().rotationZYX(this.zRot, this.yRot, this.xRot);
      Matrix3f newRotation = oldRotation.rotate(rotation);
      Vector3f newAngles = newRotation.getEulerAnglesZYX(new Vector3f());
      this.setRotation(newAngles.x, newAngles.y, newAngles.z);
   }

   public void getExtentsForGui(final PoseStack poseStack, final Consumer<Vector3fc> output) {
      this.visit(poseStack, (pose, partPath, cubeIndex, cube) -> {
         for (ModelPart$Polygon polygon : cube.polygons) {
            for (ModelPart$Vertex vertex : polygon.vertices()) {
               float x = vertex.worldX();
               float y = vertex.worldY();
               float z = vertex.worldZ();
               Vector3f pos = pose.pose().transformPosition(x, y, z, new Vector3f());
               output.accept(pos);
            }
         }
      });
   }

   public void visit(final PoseStack poseStack, final ModelPart$Visitor visitor) {
      this.visit(poseStack, visitor, "");
   }

   private void visit(final PoseStack poseStack, final ModelPart$Visitor visitor, final String path) {
      if (!this.cubes.isEmpty() || !this.children.isEmpty()) {
         poseStack.pushPose();
         this.translateAndRotate(poseStack);
         PoseStack$Pose pose = poseStack.last();

         for (int i = 0; i < this.cubes.size(); i++) {
            visitor.visit(pose, path, i, this.cubes.get(i));
         }

         String childPath = path + "/";
         this.children.forEach((name, child) -> child.visit(poseStack, visitor, childPath + name));
         poseStack.popPose();
      }
   }

   public void translateAndRotate(final PoseStack poseStack) {
      poseStack.translate(this.x / 16.0F, this.y / 16.0F, this.z / 16.0F);
      if (this.xRot != 0.0F || this.yRot != 0.0F || this.zRot != 0.0F) {
         poseStack.rotate(new Quaternionf().rotationZYX(this.zRot, this.yRot, this.xRot));
      }

      if (this.xScale != 1.0F || this.yScale != 1.0F || this.zScale != 1.0F) {
         poseStack.scale(this.xScale, this.yScale, this.zScale);
      }
   }

   private void compile(final PoseStack$Pose pose, final VertexConsumer builder, final int lightCoords, final int overlayCoords, final int color) {
      for (ModelPart$Cube cube : this.cubes) {
         cube.compile(pose, builder, lightCoords, overlayCoords, color);
      }
   }

   public ModelPart$Cube getRandomCube(final RandomSource random) {
      return Util.getRandom(this.cubes, random);
   }

   public boolean isEmpty() {
      return this.cubes.isEmpty();
   }

   public void offsetPos(final Vector3f offset) {
      this.x = this.x + offset.x();
      this.y = this.y + offset.y();
      this.z = this.z + offset.z();
   }

   public void offsetRotation(final Vector3f offset) {
      this.xRot = this.xRot + offset.x();
      this.yRot = this.yRot + offset.y();
      this.zRot = this.zRot + offset.z();
   }

   public void offsetScale(final Vector3f offset) {
      this.xScale = this.xScale + offset.x();
      this.yScale = this.yScale + offset.y();
      this.zScale = this.zScale + offset.z();
   }

   public List<ModelPart> getAllParts() {
      List<ModelPart> allParts = new ArrayList<>();
      allParts.add(this);
      this.addAllChildren((name, part) -> allParts.add(part));
      return List.copyOf(allParts);
   }

   public Function<String, ModelPart> createPartLookup() {
      Map<String, ModelPart> parts = new HashMap<>();
      parts.put("root", this);
      this.addAllChildren(parts::putIfAbsent);
      return parts::get;
   }

   private void addAllChildren(final BiConsumer<String, ModelPart> output) {
      for (Entry<String, ModelPart> entry : this.children.entrySet()) {
         output.accept(entry.getKey(), entry.getValue());
      }

      for (ModelPart part : this.children.values()) {
         part.addAllChildren(output);
      }
   }
}
