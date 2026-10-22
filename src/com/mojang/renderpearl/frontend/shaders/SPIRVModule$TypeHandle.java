package com.mojang.renderpearl.frontend.shaders;

import com.mojang.renderpearl.backend.api.SpvModule$Reflection$Type;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.util.spvc.Spvc;

@Environment(EnvType.CLIENT)
record SPIRVModule$TypeHandle(long typeHandle) implements SpvModule$Reflection$Type {
   @Override
   public int baseType() {
      return Spvc.spvc_type_get_basetype(this.typeHandle);
   }

   @Override
   public int dimensions() {
      int baseType = this.baseType();
      return baseType != 16 && baseType != 17 ? Integer.MAX_VALUE : Spvc.spvc_type_get_image_dimension(this.typeHandle);
   }

   @Override
   public int vectorSize() {
      return Spvc.spvc_type_get_vector_size(this.typeHandle);
   }

   @Override
   public int arrayDimensions() {
      return Spvc.spvc_type_get_num_array_dimensions(this.typeHandle);
   }

   @Override
   public int arrayLength(final int dimensionIndex) {
      return Spvc.spvc_type_get_array_dimension(this.typeHandle, dimensionIndex);
   }
}
