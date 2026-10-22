package com.mojang.renderpearl.backend.opengl;

import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.pipeline.BlendFactor;
import com.mojang.renderpearl.api.pipeline.BlendOp;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.IndexType;
import com.mojang.renderpearl.api.pipeline.PolygonMode;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.ShaderType;
import com.mojang.renderpearl.api.textures.AddressMode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// $VF: synthetic class
@Environment(EnvType.CLIENT)
class GlConst$1 {
   static {
      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$ShaderType[ShaderType.VERTEX.ordinal()] = 1;
      } catch (NoSuchFieldError var99) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$ShaderType[ShaderType.FRAGMENT.ordinal()] = 2;
      } catch (NoSuchFieldError var98) {
      }

      $SwitchMap$com$mojang$renderpearl$api$GpuFormat = new int[GpuFormat.values().length];

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.R8_UNORM.ordinal()] = 1;
      } catch (NoSuchFieldError var97) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.R8_SNORM.ordinal()] = 2;
      } catch (NoSuchFieldError var96) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.R16_UNORM.ordinal()] = 3;
      } catch (NoSuchFieldError var95) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.R16_SNORM.ordinal()] = 4;
      } catch (NoSuchFieldError var94) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RG8_UNORM.ordinal()] = 5;
      } catch (NoSuchFieldError var93) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RG8_SNORM.ordinal()] = 6;
      } catch (NoSuchFieldError var92) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RG16_UNORM.ordinal()] = 7;
      } catch (NoSuchFieldError var91) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RG16_SNORM.ordinal()] = 8;
      } catch (NoSuchFieldError var90) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGB8_UNORM.ordinal()] = 9;
      } catch (NoSuchFieldError var89) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGB8_SNORM.ordinal()] = 10;
      } catch (NoSuchFieldError var88) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGB16_UNORM.ordinal()] = 11;
      } catch (NoSuchFieldError var87) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGB16_SNORM.ordinal()] = 12;
      } catch (NoSuchFieldError var86) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGBA8_UNORM.ordinal()] = 13;
      } catch (NoSuchFieldError var85) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGBA8_SNORM.ordinal()] = 14;
      } catch (NoSuchFieldError var84) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGBA16_UNORM.ordinal()] = 15;
      } catch (NoSuchFieldError var83) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGB10A2_UNORM.ordinal()] = 16;
      } catch (NoSuchFieldError var82) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.D16_UNORM.ordinal()] = 17;
      } catch (NoSuchFieldError var81) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGBA16_SNORM.ordinal()] = 18;
      } catch (NoSuchFieldError var80) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.R8_UINT.ordinal()] = 19;
      } catch (NoSuchFieldError var79) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.R8_SINT.ordinal()] = 20;
      } catch (NoSuchFieldError var78) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RG8_UINT.ordinal()] = 21;
      } catch (NoSuchFieldError var77) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RG8_SINT.ordinal()] = 22;
      } catch (NoSuchFieldError var76) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGBA8_UINT.ordinal()] = 23;
      } catch (NoSuchFieldError var75) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGBA8_SINT.ordinal()] = 24;
      } catch (NoSuchFieldError var74) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.R16_UINT.ordinal()] = 25;
      } catch (NoSuchFieldError var73) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.R16_SINT.ordinal()] = 26;
      } catch (NoSuchFieldError var72) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RG16_UINT.ordinal()] = 27;
      } catch (NoSuchFieldError var71) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RG16_SINT.ordinal()] = 28;
      } catch (NoSuchFieldError var70) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGBA16_UINT.ordinal()] = 29;
      } catch (NoSuchFieldError var69) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGBA16_SINT.ordinal()] = 30;
      } catch (NoSuchFieldError var68) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.R32_UINT.ordinal()] = 31;
      } catch (NoSuchFieldError var67) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.R32_SINT.ordinal()] = 32;
      } catch (NoSuchFieldError var66) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RG32_UINT.ordinal()] = 33;
      } catch (NoSuchFieldError var65) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RG32_SINT.ordinal()] = 34;
      } catch (NoSuchFieldError var64) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGB32_UINT.ordinal()] = 35;
      } catch (NoSuchFieldError var63) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGB32_SINT.ordinal()] = 36;
      } catch (NoSuchFieldError var62) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGBA32_UINT.ordinal()] = 37;
      } catch (NoSuchFieldError var61) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGBA32_SINT.ordinal()] = 38;
      } catch (NoSuchFieldError var60) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.R16_FLOAT.ordinal()] = 39;
      } catch (NoSuchFieldError var59) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RG16_FLOAT.ordinal()] = 40;
      } catch (NoSuchFieldError var58) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGBA16_FLOAT.ordinal()] = 41;
      } catch (NoSuchFieldError var57) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.R32_FLOAT.ordinal()] = 42;
      } catch (NoSuchFieldError var56) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RG32_FLOAT.ordinal()] = 43;
      } catch (NoSuchFieldError var55) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGBA32_FLOAT.ordinal()] = 44;
      } catch (NoSuchFieldError var54) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGB10A2_UINT.ordinal()] = 45;
      } catch (NoSuchFieldError var53) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RG11B10_FLOAT.ordinal()] = 46;
      } catch (NoSuchFieldError var52) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.D32_FLOAT.ordinal()] = 47;
      } catch (NoSuchFieldError var51) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.D32_FLOAT_S8_UINT.ordinal()] = 48;
      } catch (NoSuchFieldError var50) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.D24_UNORM_S8_UINT.ordinal()] = 49;
      } catch (NoSuchFieldError var49) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.S8_UINT.ordinal()] = 50;
      } catch (NoSuchFieldError var48) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGB16_FLOAT.ordinal()] = 51;
      } catch (NoSuchFieldError var47) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGB32_FLOAT.ordinal()] = 52;
      } catch (NoSuchFieldError var46) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGB8_UINT.ordinal()] = 53;
      } catch (NoSuchFieldError var45) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGB8_SINT.ordinal()] = 54;
      } catch (NoSuchFieldError var44) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGB16_UINT.ordinal()] = 55;
      } catch (NoSuchFieldError var43) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$GpuFormat[GpuFormat.RGB16_SINT.ordinal()] = 56;
      } catch (NoSuchFieldError var42) {
      }

      $SwitchMap$com$mojang$renderpearl$api$textures$AddressMode = new int[AddressMode.values().length];

      try {
         $SwitchMap$com$mojang$renderpearl$api$textures$AddressMode[AddressMode.REPEAT.ordinal()] = 1;
      } catch (NoSuchFieldError var41) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$textures$AddressMode[AddressMode.CLAMP_TO_EDGE.ordinal()] = 2;
      } catch (NoSuchFieldError var40) {
      }

      $SwitchMap$com$mojang$renderpearl$api$pipeline$IndexType = new int[IndexType.values().length];

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$IndexType[IndexType.SHORT.ordinal()] = 1;
      } catch (NoSuchFieldError var39) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$IndexType[IndexType.INT.ordinal()] = 2;
      } catch (NoSuchFieldError var38) {
      }

      $SwitchMap$com$mojang$renderpearl$api$pipeline$PrimitiveTopology = new int[PrimitiveTopology.values().length];

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$PrimitiveTopology[PrimitiveTopology.LINES.ordinal()] = 1;
      } catch (NoSuchFieldError var37) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$PrimitiveTopology[PrimitiveTopology.DEBUG_LINES.ordinal()] = 2;
      } catch (NoSuchFieldError var36) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$PrimitiveTopology[PrimitiveTopology.DEBUG_LINE_STRIP.ordinal()] = 3;
      } catch (NoSuchFieldError var35) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$PrimitiveTopology[PrimitiveTopology.POINTS.ordinal()] = 4;
      } catch (NoSuchFieldError var34) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$PrimitiveTopology[PrimitiveTopology.TRIANGLES.ordinal()] = 5;
      } catch (NoSuchFieldError var33) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$PrimitiveTopology[PrimitiveTopology.TRIANGLE_STRIP.ordinal()] = 6;
      } catch (NoSuchFieldError var32) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$PrimitiveTopology[PrimitiveTopology.TRIANGLE_FAN.ordinal()] = 7;
      } catch (NoSuchFieldError var31) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$PrimitiveTopology[PrimitiveTopology.QUADS.ordinal()] = 8;
      } catch (NoSuchFieldError var30) {
      }

      $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendOp = new int[BlendOp.values().length];

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendOp[BlendOp.ADD.ordinal()] = 1;
      } catch (NoSuchFieldError var29) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendOp[BlendOp.SUBTRACT.ordinal()] = 2;
      } catch (NoSuchFieldError var28) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendOp[BlendOp.REVERSE_SUBTRACT.ordinal()] = 3;
      } catch (NoSuchFieldError var27) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendOp[BlendOp.MIN.ordinal()] = 4;
      } catch (NoSuchFieldError var26) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendOp[BlendOp.MAX.ordinal()] = 5;
      } catch (NoSuchFieldError var25) {
      }

      $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendFactor = new int[BlendFactor.values().length];

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendFactor[BlendFactor.CONSTANT_ALPHA.ordinal()] = 1;
      } catch (NoSuchFieldError var24) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendFactor[BlendFactor.CONSTANT_COLOR.ordinal()] = 2;
      } catch (NoSuchFieldError var23) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendFactor[BlendFactor.DST_ALPHA.ordinal()] = 3;
      } catch (NoSuchFieldError var22) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendFactor[BlendFactor.DST_COLOR.ordinal()] = 4;
      } catch (NoSuchFieldError var21) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendFactor[BlendFactor.ONE.ordinal()] = 5;
      } catch (NoSuchFieldError var20) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendFactor[BlendFactor.ONE_MINUS_CONSTANT_ALPHA.ordinal()] = 6;
      } catch (NoSuchFieldError var19) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendFactor[BlendFactor.ONE_MINUS_CONSTANT_COLOR.ordinal()] = 7;
      } catch (NoSuchFieldError var18) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendFactor[BlendFactor.ONE_MINUS_DST_ALPHA.ordinal()] = 8;
      } catch (NoSuchFieldError var17) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendFactor[BlendFactor.ONE_MINUS_DST_COLOR.ordinal()] = 9;
      } catch (NoSuchFieldError var16) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendFactor[BlendFactor.ONE_MINUS_SRC_ALPHA.ordinal()] = 10;
      } catch (NoSuchFieldError var15) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendFactor[BlendFactor.ONE_MINUS_SRC_COLOR.ordinal()] = 11;
      } catch (NoSuchFieldError var14) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendFactor[BlendFactor.SRC_ALPHA.ordinal()] = 12;
      } catch (NoSuchFieldError var13) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendFactor[BlendFactor.SRC_ALPHA_SATURATE.ordinal()] = 13;
      } catch (NoSuchFieldError var12) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendFactor[BlendFactor.SRC_COLOR.ordinal()] = 14;
      } catch (NoSuchFieldError var11) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$BlendFactor[BlendFactor.ZERO.ordinal()] = 15;
      } catch (NoSuchFieldError var10) {
      }

      $SwitchMap$com$mojang$renderpearl$api$pipeline$PolygonMode = new int[PolygonMode.values().length];

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$PolygonMode[PolygonMode.WIREFRAME.ordinal()] = 1;
      } catch (NoSuchFieldError var9) {
      }

      $SwitchMap$com$mojang$renderpearl$api$pipeline$CompareOp = new int[CompareOp.values().length];

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$CompareOp[CompareOp.ALWAYS_PASS.ordinal()] = 1;
      } catch (NoSuchFieldError var8) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$CompareOp[CompareOp.LESS_THAN.ordinal()] = 2;
      } catch (NoSuchFieldError var7) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$CompareOp[CompareOp.LESS_THAN_OR_EQUAL.ordinal()] = 3;
      } catch (NoSuchFieldError var6) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$CompareOp[CompareOp.EQUAL.ordinal()] = 4;
      } catch (NoSuchFieldError var5) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$CompareOp[CompareOp.NOT_EQUAL.ordinal()] = 5;
      } catch (NoSuchFieldError var4) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$CompareOp[CompareOp.GREATER_THAN_OR_EQUAL.ordinal()] = 6;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$CompareOp[CompareOp.GREATER_THAN.ordinal()] = 7;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$com$mojang$renderpearl$api$pipeline$CompareOp[CompareOp.NEVER_PASS.ordinal()] = 8;
      } catch (NoSuchFieldError var1) {
      }
   }
}
