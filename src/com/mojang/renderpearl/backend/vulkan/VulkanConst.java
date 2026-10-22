package com.mojang.renderpearl.backend.vulkan;

import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer$Usage;
import com.mojang.renderpearl.api.device.GpuSurface$PresentMode;
import com.mojang.renderpearl.api.pipeline.BlendFactor;
import com.mojang.renderpearl.api.pipeline.BlendOp;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.PolygonMode;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.ShaderType;
import com.mojang.renderpearl.api.textures.AddressMode;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuTexture$Usage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class VulkanConst {
   public static int textureUsageToVk(@GpuTexture$Usage final int usage, final GpuFormat format) {
      int vkUsage = 0;
      if ((usage & 8) != 0) {
         if (format.hasColorAspect()) {
            vkUsage |= 16;
         }

         if (format.hasDepthAspect()) {
            vkUsage |= 32;
         }
      }

      if ((usage & 4) != 0) {
         vkUsage |= 4;
      }

      if ((usage & 1) != 0) {
         vkUsage |= 2;
      }

      if ((usage & 2) != 0) {
         vkUsage |= 1;
      }

      return vkUsage;
   }

   public static int bufferUsageToVk(@GpuBuffer$Usage final int usage) {
      int result = 0;
      if ((usage & 8) != 0) {
         result |= 2;
      }

      if ((usage & 16) != 0) {
         result |= 1;
      }

      if ((usage & 32) != 0) {
         result |= 128;
      }

      if ((usage & 64) != 0) {
         result |= 64;
      }

      if ((usage & 128) != 0) {
         result |= 16;
      }

      if ((usage & 256) != 0) {
         result |= 4;
      }

      if ((usage & 512) != 0) {
         result |= 256;
      }

      return result;
   }

   public static int formatAspectMask(final GpuFormat format) {
      int aspectMask = 0;
      if (format.hasColorAspect()) {
         aspectMask |= 1;
      }

      if (format.hasDepthAspect()) {
         aspectMask |= 2;
      }

      if (format.hasStencilAspect()) {
         aspectMask |= 4;
      }

      return aspectMask;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static int toVk(final AddressMode addressMode) {
      return switch (addressMode) {
         case REPEAT -> 0;
         case CLAMP_TO_EDGE -> 2;
      };
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static int toVk(final FilterMode filter) {
      return switch (filter) {
         case NEAREST -> 0;
         case LINEAR -> 1;
      };
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static int toVk(final GpuFormat format) {
      return switch (format) {
         case R8_UNORM -> 9;
         case R8_SNORM -> 10;
         case RG8_UNORM -> 16;
         case RG8_SNORM -> 17;
         case RGB8_UNORM -> 23;
         case RGB8_SNORM -> 24;
         case RGBA8_UNORM -> 37;
         case RGBA8_SNORM -> 38;
         case R16_UNORM -> 70;
         case R16_SNORM -> 71;
         case RG16_UNORM -> 77;
         case RG16_SNORM -> 78;
         case RGB16_UNORM -> 84;
         case RGB16_SNORM -> 85;
         case RGBA16_UNORM -> 91;
         case RGBA16_SNORM -> 92;
         case R8_UINT -> 13;
         case R8_SINT -> 14;
         case RG8_UINT -> 20;
         case RG8_SINT -> 21;
         case RGB8_UINT -> 27;
         case RGB8_SINT -> 28;
         case RGBA8_UINT -> 41;
         case RGBA8_SINT -> 42;
         case R16_UINT -> 74;
         case R16_SINT -> 75;
         case RG16_UINT -> 81;
         case RG16_SINT -> 82;
         case RGB16_UINT -> 88;
         case RGB16_SINT -> 89;
         case RGBA16_UINT -> 95;
         case RGBA16_SINT -> 96;
         case R32_UINT -> 98;
         case R32_SINT -> 99;
         case RG32_UINT -> 101;
         case RG32_SINT -> 102;
         case RGB32_UINT -> 104;
         case RGB32_SINT -> 105;
         case RGBA32_UINT -> 107;
         case RGBA32_SINT -> 108;
         case R16_FLOAT -> 76;
         case RG16_FLOAT -> 83;
         case RGB16_FLOAT -> 90;
         case RGBA16_FLOAT -> 97;
         case R32_FLOAT -> 100;
         case RG32_FLOAT -> 103;
         case RGB32_FLOAT -> 106;
         case RGBA32_FLOAT -> 109;
         case RGB10A2_UNORM -> 64;
         case RGB10A2_UINT -> 68;
         case RG11B10_FLOAT -> 122;
         case D32_FLOAT -> 126;
         case D32_FLOAT_S8_UINT -> 130;
         case D24_UNORM_S8_UINT -> 129;
         case D16_UNORM -> 124;
         case S8_UINT -> 127;
      };
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static int toVk(final BlendFactor factor) {
      return switch (factor) {
         case CONSTANT_ALPHA -> 12;
         case CONSTANT_COLOR -> 10;
         case ONE_MINUS_CONSTANT_ALPHA -> 13;
         case ONE_MINUS_CONSTANT_COLOR -> 11;
         case DST_ALPHA -> 8;
         case DST_COLOR -> 4;
         case ONE -> 1;
         case ONE_MINUS_DST_ALPHA -> 9;
         case ONE_MINUS_DST_COLOR -> 5;
         case ONE_MINUS_SRC_ALPHA -> 7;
         case ONE_MINUS_SRC_COLOR -> 3;
         case SRC_ALPHA -> 6;
         case SRC_ALPHA_SATURATE -> 14;
         case SRC_COLOR -> 2;
         case ZERO -> 0;
      };
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static int toVk(final BlendOp blendOp) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.RuntimeException: invalid constant type: Lcom/mojang/renderpearl/api/pipeline/BlendOp; with value 4
      //   at org.jetbrains.java.decompiler.modules.decompiler.exps.ConstExprent.toJava(ConstExprent.java:356)
      //   at org.jetbrains.java.decompiler.modules.decompiler.exps.SwitchExprent.toJava(SwitchExprent.java:105)
      //   at org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.getCastedExprent(ExprProcessor.java:1014)
      //   at org.jetbrains.java.decompiler.modules.decompiler.exps.ExitExprent.toJava(ExitExprent.java:86)
      //   at org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.listToJava(ExprProcessor.java:891)
      //   at org.jetbrains.java.decompiler.modules.decompiler.stats.BasicBlockStatement.toJava(BasicBlockStatement.java:91)
      //   at org.jetbrains.java.decompiler.modules.decompiler.ExprProcessor.jmpWrapper(ExprProcessor.java:829)
      //   at org.jetbrains.java.decompiler.modules.decompiler.stats.SequenceStatement.toJava(SequenceStatement.java:107)
      //   at org.jetbrains.java.decompiler.modules.decompiler.stats.RootStatement.toJava(RootStatement.java:36)
      //   at org.jetbrains.java.decompiler.main.ClassWriter.writeMethod(ClassWriter.java:1306)
      //
      // Bytecode:
      // 00: getstatic com/mojang/renderpearl/backend/vulkan/VulkanConst$1.$SwitchMap$com$mojang$renderpearl$api$pipeline$BlendOp [I
      // 03: aload 0
      // 04: invokevirtual com/mojang/renderpearl/api/pipeline/BlendOp.ordinal ()I
      // 07: iaload
      // 08: tableswitch 36 1 5 46 50 54 58 62
      // 2c: new java/lang/MatchException
      // 2f: dup
      // 30: aconst_null
      // 31: aconst_null
      // 32: invokespecial java/lang/MatchException.<init> (Ljava/lang/String;Ljava/lang/Throwable;)V
      // 35: athrow
      // 36: bipush 0
      // 37: goto 47
      // 3a: bipush 1
      // 3b: goto 47
      // 3e: bipush 2
      // 3f: goto 47
      // 42: bipush 3
      // 43: goto 47
      // 46: bipush 4
      // 47: ireturn
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static int toVk(final CompareOp op) {
      return switch (VulkanConst$1.$SwitchMap$com$mojang$renderpearl$api$pipeline$CompareOp[op.ordinal()]) {
         case 1 -> 7;
         case 2 -> 1;
         case 3 -> 3;
         case 4 -> 2;
         case 5 -> 5;
         case 6 -> 6;
         case 7 -> 4;
         case 8 -> 0;
         default -> throw new MatchException(null, null);
      };
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static int toVk(final PolygonMode polygonMode) {
      return switch (VulkanConst$1.$SwitchMap$com$mojang$renderpearl$api$pipeline$PolygonMode[polygonMode.ordinal()]) {
         case 1 -> 0;
         case 2 -> 1;
         default -> throw new MatchException(null, null);
      };
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static int toVk(final PrimitiveTopology primitiveTopology) {
      return switch (VulkanConst$1.$SwitchMap$com$mojang$renderpearl$api$pipeline$PrimitiveTopology[primitiveTopology.ordinal()]) {
         case 1 -> 3;
         case 2 -> 1;
         case 3 -> 2;
         case 4 -> 0;
         case 5 -> 3;
         case 6 -> 4;
         case 7 -> 5;
         case 8 -> 3;
         default -> throw new MatchException(null, null);
      };
   }

   public static int toVk(final ColorTargetState colorTargetState) {
      int result = 0;
      if (colorTargetState.writeAlpha()) {
         result |= 8;
      }

      if (colorTargetState.writeRed()) {
         result |= 1;
      }

      if (colorTargetState.writeGreen()) {
         result |= 2;
      }

      if (colorTargetState.writeBlue()) {
         result |= 4;
      }

      return result;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static int toVk(final GpuSurface$PresentMode mode) {
      return switch (VulkanConst$1.$SwitchMap$com$mojang$renderpearl$api$device$GpuSurface$PresentMode[mode.ordinal()]) {
         case 1 -> 0;
         case 2 -> 1;
         case 3 -> 2;
         case 4 -> 3;
         default -> throw new MatchException(null, null);
      };
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static int toVk(final ShaderType shaderType) {
      return switch (VulkanConst$1.$SwitchMap$com$mojang$renderpearl$api$pipeline$ShaderType[shaderType.ordinal()]) {
         case 1 -> 1;
         case 2 -> 16;
         default -> throw new MatchException(null, null);
      };
   }
}
