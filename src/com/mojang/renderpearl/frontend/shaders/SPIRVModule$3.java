package com.mojang.renderpearl.frontend.shaders;

import com.mojang.renderpearl.backend.api.SpvModule$Reflection$Descriptor;
import com.mojang.renderpearl.backend.api.SpvModule$Reflection$Type;
import java.nio.IntBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class SPIRVModule$3 implements SpvModule$Reflection$Descriptor {
   SPIRVModule$3(
      final String val$name,
      final SPIRVModule$TypeHandle val$typeHandle,
      final int val$resourceType,
      final IntBuffer val$spirv,
      final int val$setOffset,
      final int val$bindingOffset
   ) {
      this.val$name = val$namex;
      this.val$typeHandle = val$typeHandlex;
      this.val$resourceType = val$resourceTypex;
      this.val$spirv = val$spirvx;
      this.val$setOffset = val$setOffsetx;
      this.val$bindingOffset = val$bindingOffsetx;
   }

   @Override
   public String name() {
      return this.val$name;
   }

   @Override
   public SpvModule$Reflection$Type type() {
      return this.val$typeHandle;
   }

   @Override
   public int resourceType() {
      return this.val$resourceType;
   }

   @Override
   public int descriptorSetIndex() {
      return this.val$spirv.get(this.val$setOffset);
   }

   @Override
   public void descriptorSetIndex(final int index) {
      this.val$spirv.put(this.val$setOffset, index);
   }

   @Override
   public int binding() {
      return this.val$spirv.get(this.val$bindingOffset);
   }

   @Override
   public void binding(final int binding) {
      this.val$spirv.put(this.val$bindingOffset, binding);
   }
}
