package com.mojang.renderpearl.frontend.shaders;

import com.mojang.renderpearl.backend.api.SpvModule$Reflection;
import com.mojang.renderpearl.backend.api.SpvModule$Reflection$Descriptor;
import com.mojang.renderpearl.backend.api.SpvModule$Reflection$InterfaceVariable;
import com.mojang.renderpearl.backend.api.SpvModule$Reflection$PushConstant;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class SPIRVModule$1 implements SpvModule$Reflection {
   SPIRVModule$1(
      final SPIRVModule this$0,
      final List val$inputs,
      final List val$outputs,
      final Int2ReferenceMap val$descriptors,
      final List val$allDescriptors,
      final List val$pushConstants
   ) {
      this.val$inputs = val$inputsx;
      this.val$outputs = val$outputsx;
      this.val$descriptors = val$descriptorsx;
      this.val$allDescriptors = val$allDescriptorsx;
      this.val$pushConstants = val$pushConstantsx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public List<SpvModule$Reflection$InterfaceVariable> inputs() {
      return this.val$inputs;
   }

   @Override
   public List<SpvModule$Reflection$InterfaceVariable> outputs() {
      return this.val$outputs;
   }

   @Override
   public List<SpvModule$Reflection$Descriptor> descriptors(final int descriptorType) {
      return (List<SpvModule$Reflection$Descriptor>)this.val$descriptors.getOrDefault(descriptorType, List.of());
   }

   @Override
   public List<SpvModule$Reflection$Descriptor> descriptors() {
      return this.val$allDescriptors;
   }

   @Override
   public List<SpvModule$Reflection$PushConstant> pushConstants() {
      return this.val$pushConstants;
   }
}
