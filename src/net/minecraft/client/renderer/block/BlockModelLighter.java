package net.minecraft.client.renderer.block;

import com.mojang.blaze3d.vertex.QuadInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public class BlockModelLighter {
   private static final int CACHE_SIZE = 100;
   private static final ThreadLocal<BlockModelLighter$Cache> CACHE = ThreadLocal.withInitial(BlockModelLighter$Cache::new);
   public static final int CHECK_LIGHT = -1;
   private final BlockModelLighter$Cache cache;
   private final BlockPos$MutableBlockPos scratchPos = new BlockPos$MutableBlockPos();
   private boolean faceCubic;
   private boolean facePartial;
   private final float[] faceShape = new float[BlockModelLighter$SizeInfo.COUNT];

   public BlockModelLighter() {
      this.cache = CACHE.get();
   }

   public int getLightCoords(final BlockState state, final BlockAndTintGetter level, final BlockPos relativePos) {
      return this.cache.getLightCoords(state, level, relativePos);
   }

   public void prepareQuadAmbientOcclusion(
      final BlockAndTintGetter level, final BlockState state, final BlockPos centerPosition, final BakedQuad quad, final QuadInstance outputInstance
   ) {
      this.prepareQuadShape(level, state, centerPosition, quad, true);
      Direction direction = quad.direction();
      BlockPos basePosition = this.faceCubic ? centerPosition.relative(direction) : centerPosition;
      BlockModelLighter$AdjacencyInfo info = BlockModelLighter$AdjacencyInfo.fromFacing(direction);
      BlockPos$MutableBlockPos pos = this.scratchPos;
      pos.setWithOffset(basePosition, info.corners[0]);
      BlockState state0 = level.getBlockState(pos);
      int light0 = this.cache.getLightCoords(state0, level, pos);
      float shade0 = this.cache.getShadeBrightness(state0, level, pos);
      pos.setWithOffset(basePosition, info.corners[1]);
      BlockState state1 = level.getBlockState(pos);
      int light1 = this.cache.getLightCoords(state1, level, pos);
      float shade1 = this.cache.getShadeBrightness(state1, level, pos);
      pos.setWithOffset(basePosition, info.corners[2]);
      BlockState state2 = level.getBlockState(pos);
      int light2 = this.cache.getLightCoords(state2, level, pos);
      float shade2 = this.cache.getShadeBrightness(state2, level, pos);
      pos.setWithOffset(basePosition, info.corners[3]);
      BlockState state3 = level.getBlockState(pos);
      int light3 = this.cache.getLightCoords(state3, level, pos);
      float shade3 = this.cache.getShadeBrightness(state3, level, pos);
      BlockState corner0 = level.getBlockState(pos.setWithOffset(basePosition, info.corners[0]).move(direction));
      boolean lightPermeable0 = corner0.isLightPermeable();
      BlockState corner1 = level.getBlockState(pos.setWithOffset(basePosition, info.corners[1]).move(direction));
      boolean lightPermeable1 = corner1.isLightPermeable();
      BlockState corner2 = level.getBlockState(pos.setWithOffset(basePosition, info.corners[2]).move(direction));
      boolean lightPermeable2 = corner2.isLightPermeable();
      BlockState corner3 = level.getBlockState(pos.setWithOffset(basePosition, info.corners[3]).move(direction));
      boolean lightPermeable3 = corner3.isLightPermeable();
      float shadeCorner02;
      int lightCorner02;
      if (!lightPermeable2 && !lightPermeable0) {
         shadeCorner02 = shade0;
         lightCorner02 = light0;
      } else {
         pos.setWithOffset(basePosition, info.corners[0]).move(info.corners[2]);
         BlockState state02 = level.getBlockState(pos);
         shadeCorner02 = this.cache.getShadeBrightness(state02, level, pos);
         lightCorner02 = this.cache.getLightCoords(state02, level, pos);
      }

      float shadeCorner03;
      int lightCorner03;
      if (!lightPermeable3 && !lightPermeable0) {
         shadeCorner03 = shade0;
         lightCorner03 = light0;
      } else {
         pos.setWithOffset(basePosition, info.corners[0]).move(info.corners[3]);
         BlockState state03 = level.getBlockState(pos);
         shadeCorner03 = this.cache.getShadeBrightness(state03, level, pos);
         lightCorner03 = this.cache.getLightCoords(state03, level, pos);
      }

      float shadeCorner12;
      int lightCorner12;
      if (!lightPermeable2 && !lightPermeable1) {
         shadeCorner12 = shade0;
         lightCorner12 = light0;
      } else {
         pos.setWithOffset(basePosition, info.corners[1]).move(info.corners[2]);
         BlockState state12 = level.getBlockState(pos);
         shadeCorner12 = this.cache.getShadeBrightness(state12, level, pos);
         lightCorner12 = this.cache.getLightCoords(state12, level, pos);
      }

      float shadeCorner13;
      int lightCorner13;
      if (!lightPermeable3 && !lightPermeable1) {
         shadeCorner13 = shade0;
         lightCorner13 = light0;
      } else {
         pos.setWithOffset(basePosition, info.corners[1]).move(info.corners[3]);
         BlockState state13 = level.getBlockState(pos);
         shadeCorner13 = this.cache.getShadeBrightness(state13, level, pos);
         lightCorner13 = this.cache.getLightCoords(state13, level, pos);
      }

      int lightCenter = this.cache.getLightCoords(state, level, centerPosition);
      pos.setWithOffset(centerPosition, direction);
      BlockState nextState = level.getBlockState(pos);
      if (this.faceCubic || !nextState.isSolidRender()) {
         lightCenter = this.cache.getLightCoords(nextState, level, pos);
      }

      float shadeCenter = this.faceCubic
         ? this.cache.getShadeBrightness(level.getBlockState(basePosition), level, basePosition)
         : this.cache.getShadeBrightness(level.getBlockState(centerPosition), level, centerPosition);
      BlockModelLighter$AmbientVertexRemap remap = BlockModelLighter$AmbientVertexRemap.fromFacing(direction);
      if (this.facePartial && info.doNonCubicWeight) {
         float tempShade1 = (shade3 + shade0 + shadeCorner03 + shadeCenter) * 0.25F;
         float tempShade2 = (shade2 + shade0 + shadeCorner02 + shadeCenter) * 0.25F;
         float tempShade3 = (shade2 + shade1 + shadeCorner12 + shadeCenter) * 0.25F;
         float tempShade4 = (shade3 + shade1 + shadeCorner13 + shadeCenter) * 0.25F;
         float vert0weight01 = this.faceShape[info.vert0Weights[0].index] * this.faceShape[info.vert0Weights[1].index];
         float vert0weight23 = this.faceShape[info.vert0Weights[2].index] * this.faceShape[info.vert0Weights[3].index];
         float vert0weight45 = this.faceShape[info.vert0Weights[4].index] * this.faceShape[info.vert0Weights[5].index];
         float vert0weight67 = this.faceShape[info.vert0Weights[6].index] * this.faceShape[info.vert0Weights[7].index];
         float vert1weight01 = this.faceShape[info.vert1Weights[0].index] * this.faceShape[info.vert1Weights[1].index];
         float vert1weight23 = this.faceShape[info.vert1Weights[2].index] * this.faceShape[info.vert1Weights[3].index];
         float vert1weight45 = this.faceShape[info.vert1Weights[4].index] * this.faceShape[info.vert1Weights[5].index];
         float vert1weight67 = this.faceShape[info.vert1Weights[6].index] * this.faceShape[info.vert1Weights[7].index];
         float vert2weight01 = this.faceShape[info.vert2Weights[0].index] * this.faceShape[info.vert2Weights[1].index];
         float vert2weight23 = this.faceShape[info.vert2Weights[2].index] * this.faceShape[info.vert2Weights[3].index];
         float vert2weight45 = this.faceShape[info.vert2Weights[4].index] * this.faceShape[info.vert2Weights[5].index];
         float vert2weight67 = this.faceShape[info.vert2Weights[6].index] * this.faceShape[info.vert2Weights[7].index];
         float vert3weight01 = this.faceShape[info.vert3Weights[0].index] * this.faceShape[info.vert3Weights[1].index];
         float vert3weight23 = this.faceShape[info.vert3Weights[2].index] * this.faceShape[info.vert3Weights[3].index];
         float vert3weight45 = this.faceShape[info.vert3Weights[4].index] * this.faceShape[info.vert3Weights[5].index];
         float vert3weight67 = this.faceShape[info.vert3Weights[6].index] * this.faceShape[info.vert3Weights[7].index];
         outputInstance.setColor(
            remap.vert0,
            ARGB.gray(Math.clamp(tempShade1 * vert0weight01 + tempShade2 * vert0weight23 + tempShade3 * vert0weight45 + tempShade4 * vert0weight67, 0.0F, 1.0F))
         );
         outputInstance.setColor(
            remap.vert1,
            ARGB.gray(Math.clamp(tempShade1 * vert1weight01 + tempShade2 * vert1weight23 + tempShade3 * vert1weight45 + tempShade4 * vert1weight67, 0.0F, 1.0F))
         );
         outputInstance.setColor(
            remap.vert2,
            ARGB.gray(Math.clamp(tempShade1 * vert2weight01 + tempShade2 * vert2weight23 + tempShade3 * vert2weight45 + tempShade4 * vert2weight67, 0.0F, 1.0F))
         );
         outputInstance.setColor(
            remap.vert3,
            ARGB.gray(Math.clamp(tempShade1 * vert3weight01 + tempShade2 * vert3weight23 + tempShade3 * vert3weight45 + tempShade4 * vert3weight67, 0.0F, 1.0F))
         );
         int _tc1 = LightCoordsUtil.smoothBlend(light3, light0, lightCorner03, lightCenter);
         int _tc2 = LightCoordsUtil.smoothBlend(light2, light0, lightCorner02, lightCenter);
         int _tc3 = LightCoordsUtil.smoothBlend(light2, light1, lightCorner12, lightCenter);
         int _tc4 = LightCoordsUtil.smoothBlend(light3, light1, lightCorner13, lightCenter);
         outputInstance.setLightCoords(
            remap.vert0, LightCoordsUtil.smoothWeightedBlend(_tc1, _tc2, _tc3, _tc4, vert0weight01, vert0weight23, vert0weight45, vert0weight67)
         );
         outputInstance.setLightCoords(
            remap.vert1, LightCoordsUtil.smoothWeightedBlend(_tc1, _tc2, _tc3, _tc4, vert1weight01, vert1weight23, vert1weight45, vert1weight67)
         );
         outputInstance.setLightCoords(
            remap.vert2, LightCoordsUtil.smoothWeightedBlend(_tc1, _tc2, _tc3, _tc4, vert2weight01, vert2weight23, vert2weight45, vert2weight67)
         );
         outputInstance.setLightCoords(
            remap.vert3, LightCoordsUtil.smoothWeightedBlend(_tc1, _tc2, _tc3, _tc4, vert3weight01, vert3weight23, vert3weight45, vert3weight67)
         );
      } else {
         float lightLevel1 = (shade3 + shade0 + shadeCorner03 + shadeCenter) * 0.25F;
         float lightLevel2 = (shade2 + shade0 + shadeCorner02 + shadeCenter) * 0.25F;
         float lightLevel3 = (shade2 + shade1 + shadeCorner12 + shadeCenter) * 0.25F;
         float lightLevel4 = (shade3 + shade1 + shadeCorner13 + shadeCenter) * 0.25F;
         outputInstance.setLightCoords(remap.vert0, LightCoordsUtil.smoothBlend(light3, light0, lightCorner03, lightCenter));
         outputInstance.setLightCoords(remap.vert1, LightCoordsUtil.smoothBlend(light2, light0, lightCorner02, lightCenter));
         outputInstance.setLightCoords(remap.vert2, LightCoordsUtil.smoothBlend(light2, light1, lightCorner12, lightCenter));
         outputInstance.setLightCoords(remap.vert3, LightCoordsUtil.smoothBlend(light3, light1, lightCorner13, lightCenter));
         outputInstance.setColor(remap.vert0, ARGB.gray(lightLevel1));
         outputInstance.setColor(remap.vert1, ARGB.gray(lightLevel2));
         outputInstance.setColor(remap.vert2, ARGB.gray(lightLevel3));
         outputInstance.setColor(remap.vert3, ARGB.gray(lightLevel4));
      }

      CardinalLighting cardinalLighting = level.cardinalLighting();
      outputInstance.scaleColor(getDirectionalBrightness(cardinalLighting, quad, direction));
   }

   public void prepareQuadFlat(
      final BlockAndTintGetter level,
      final BlockState state,
      final BlockPos pos,
      final int lightCoords,
      final BakedQuad quad,
      final QuadInstance outputInstance
   ) {
      if (lightCoords == -1) {
         this.prepareQuadShape(level, state, pos, quad, false);
         BlockPos lightPos = (BlockPos)(this.faceCubic ? this.scratchPos.setWithOffset(pos, quad.direction()) : pos);
         outputInstance.setLightCoords(this.cache.getLightCoords(state, level, lightPos));
      } else {
         outputInstance.setLightCoords(lightCoords);
      }

      CardinalLighting cardinalLighting = level.cardinalLighting();
      outputInstance.setColor(ARGB.gray(getDirectionalBrightness(cardinalLighting, quad, quad.direction())));
   }

   private void prepareQuadShape(
      final BlockAndTintGetter level, final BlockState state, final BlockPos pos, final BakedQuad quad, final boolean ambientOcclusion
   ) {
      float minX = 32.0F;
      float minY = 32.0F;
      float minZ = 32.0F;
      float maxX = -32.0F;
      float maxY = -32.0F;
      float maxZ = -32.0F;

      for (int i = 0; i < 4; i++) {
         Vector3fc position = quad.position(i);
         float x = position.x();
         float y = position.y();
         float z = position.z();
         minX = Math.min(minX, x);
         minY = Math.min(minY, y);
         minZ = Math.min(minZ, z);
         maxX = Math.max(maxX, x);
         maxY = Math.max(maxY, y);
         maxZ = Math.max(maxZ, z);
      }

      if (ambientOcclusion) {
         this.faceShape[BlockModelLighter$SizeInfo.WEST.index] = minX;
         this.faceShape[BlockModelLighter$SizeInfo.EAST.index] = maxX;
         this.faceShape[BlockModelLighter$SizeInfo.DOWN.index] = minY;
         this.faceShape[BlockModelLighter$SizeInfo.UP.index] = maxY;
         this.faceShape[BlockModelLighter$SizeInfo.NORTH.index] = minZ;
         this.faceShape[BlockModelLighter$SizeInfo.SOUTH.index] = maxZ;
         this.faceShape[BlockModelLighter$SizeInfo.FLIP_WEST.index] = 1.0F - minX;
         this.faceShape[BlockModelLighter$SizeInfo.FLIP_EAST.index] = 1.0F - maxX;
         this.faceShape[BlockModelLighter$SizeInfo.FLIP_DOWN.index] = 1.0F - minY;
         this.faceShape[BlockModelLighter$SizeInfo.FLIP_UP.index] = 1.0F - maxY;
         this.faceShape[BlockModelLighter$SizeInfo.FLIP_NORTH.index] = 1.0F - minZ;
         this.faceShape[BlockModelLighter$SizeInfo.FLIP_SOUTH.index] = 1.0F - maxZ;
      }

      float minEpsilon = 1.0E-4F;
      float maxEpsilon = 0.9999F;

      this.facePartial = switch (quad.direction()) {
         case DOWN, UP -> minX >= 1.0E-4F || minZ >= 1.0E-4F || maxX <= 0.9999F || maxZ <= 0.9999F;
         case NORTH, SOUTH -> minX >= 1.0E-4F || minY >= 1.0E-4F || maxX <= 0.9999F || maxY <= 0.9999F;
         case WEST, EAST -> minY >= 1.0E-4F || minZ >= 1.0E-4F || maxY <= 0.9999F || maxZ <= 0.9999F;
      };

      this.faceCubic = switch (quad.direction()) {
         case DOWN -> minY == maxY && (minY < 1.0E-4F || state.isCollisionShapeFullBlock(level, pos));
         case UP -> minY == maxY && (maxY > 0.9999F || state.isCollisionShapeFullBlock(level, pos));
         case NORTH -> minZ == maxZ && (minZ < 1.0E-4F || state.isCollisionShapeFullBlock(level, pos));
         case SOUTH -> minZ == maxZ && (maxZ > 0.9999F || state.isCollisionShapeFullBlock(level, pos));
         case WEST -> minX == maxX && (minX < 1.0E-4F || state.isCollisionShapeFullBlock(level, pos));
         case EAST -> minX == maxX && (maxX > 0.9999F || state.isCollisionShapeFullBlock(level, pos));
      };
   }

   private static float getDirectionalBrightness(final CardinalLighting cardinalLighting, final BakedQuad quad, final Direction actualDirection) {
      Direction shadeDirectionOverride = quad.materialInfo().shadeDirectionOverride();
      return shadeDirectionOverride != null ? cardinalLighting.byFace(shadeDirectionOverride) : cardinalLighting.byFace(actualDirection);
   }

   public static void enableCaching() {
      CACHE.get().enable();
   }

   public static void clearCache() {
      CACHE.get().disable();
   }
}
