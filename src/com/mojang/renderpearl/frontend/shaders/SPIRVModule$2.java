package com.mojang.renderpearl.frontend.shaders;

import com.mojang.renderpearl.backend.api.SpvModule$Reflection$InterfaceVariable;
import com.mojang.renderpearl.backend.api.SpvModule$Reflection$Type;
import java.nio.IntBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.util.spvc.Spvc;
import org.lwjgl.util.spvc.SpvcReflectedResource;

@Environment(EnvType.CLIENT)
class SPIRVModule$2 implements SpvModule$Reflection$InterfaceVariable {
   SPIRVModule$2(
      final String val$name,
      final SPIRVModule$TypeHandle val$typeHandle,
      final IntBuffer val$spirv,
      final int val$locationOffset,
      final long val$compiler,
      final SpvcReflectedResource val$resource
   ) {
      this.val$name = val$namex;
      this.val$typeHandle = val$typeHandlex;
      this.val$spirv = val$spirvx;
      this.val$locationOffset = val$locationOffsetx;
      this.val$compiler = val$compilerx;
      this.val$resource = val$resourcex;
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
   public int location() {
      return this.val$spirv.get(this.val$locationOffset);
   }

   @Override
   public void location(final int location) {
      this.val$spirv.put(this.val$locationOffset, location);
   }

   @Override
   public int decoration(final int decoration) {
      return Spvc.spvc_compiler_get_decoration(this.val$compiler, this.val$resource.id(), decoration);
   }
}
