package com.mojang.renderpearl.frontend.shaders;

import com.mojang.renderpearl.api.pipeline.ShaderType;
import com.mojang.renderpearl.backend.api.SpvModule;
import com.mojang.renderpearl.backend.api.SpvModule$Reflection;
import com.mojang.renderpearl.backend.api.SpvModule$Reflection$Descriptor;
import com.mojang.renderpearl.backend.api.SpvModule$Reflection$InterfaceVariable;
import com.mojang.renderpearl.backend.api.SpvModule$Reflection$PushConstant;
import com.mojang.renderpearl.util.ShaderCompileException;
import it.unimi.dsi.fastutil.ints.Int2ReferenceArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceLists;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.spvc.Spvc;
import org.lwjgl.util.spvc.SpvcReflectedResource;
import org.lwjgl.util.spvc.SpvcReflectedResource.Buffer;

@Environment(EnvType.CLIENT)
public class SPIRVModule implements SpvModule {
   private final ByteBuffer spv;
   private final ShaderType type;
   @Nullable
   private SpvModule$Reflection reflection;
   private long spvcContext;

   public SPIRVModule(final ByteBuffer spv, final ShaderType type) {
      this.spv = spv;
      this.type = type;
   }

   @Override
   public void close() {
      MemoryUtil.memFree(this.spv);
      if (this.spvcContext != 0L) {
         Spvc.spvc_context_destroy(this.spvcContext);
      }
   }

   @Override
   public ByteBuffer spv() {
      return this.spv;
   }

   @Override
   public ShaderType type() {
      return this.type;
   }

   @Override
   public SpvModule$Reflection reflect() throws ShaderCompileException {
      if (this.reflection == null) {
         this.doReflection();
      }

      return this.reflection;
   }

   @Nullable
   @Override
   public SpvModule$Reflection getReflectionInfoIfAvailable() {
      return this.reflection;
   }

   private void doReflection() throws ShaderCompileException {
      IntBuffer spirvBuffer = this.spv.asIntBuffer();
      MemoryStack stack = MemoryStack.stackPush();

      try {
         PointerBuffer pointer = stack.callocPointer(1);
         SpvUtil.throwIfError(Spvc.spvc_context_create(pointer), "Couldn't create spvc context");
         this.spvcContext = pointer.get(0);
      } catch (Throwable var17) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var16) {
               var17.addSuppressed(var16);
            }
         }

         throw var17;
      }

      if (stack != null) {
         stack.close();
      }

      stack = MemoryStack.stackPush();

      try {
         PointerBuffer pointerReturnBuffer = stack.callocPointer(1);
         SpvUtil.throwIfError(
            Spvc.spvc_context_parse_spirv(this.spvcContext, spirvBuffer, spirvBuffer.remaining(), pointerReturnBuffer), "Couldn't parse spirv"
         );
         long ir = pointerReturnBuffer.get(0);
         SpvUtil.throwIfError(Spvc.spvc_context_create_compiler(this.spvcContext, 0, ir, 0, pointerReturnBuffer), "Couldn't create compiler");
         long compiler = pointerReturnBuffer.get(0);
         SpvUtil.throwIfError(Spvc.spvc_compiler_create_shader_resources(compiler, pointerReturnBuffer), "Couldn't create resource list");
         long spvcResources = pointerReturnBuffer.get(0);
         List<SpvModule$Reflection$InterfaceVariable> inputs = generateInterfaceVariableList(compiler, spvcResources, spirvBuffer, 3);
         List<SpvModule$Reflection$InterfaceVariable> outputs = generateInterfaceVariableList(compiler, spvcResources, spirvBuffer, 4);
         Int2ReferenceMap<List<SpvModule$Reflection$Descriptor>> descriptors = new Int2ReferenceArrayMap();

         for (int i = 0; i < SpvUtil.DESCRIPTOR_TYPES.size(); i++) {
            int descriptorType = SpvUtil.DESCRIPTOR_TYPES.getInt(i);
            descriptors.put(descriptorType, generateDescriptorList(compiler, spvcResources, spirvBuffer, descriptorType));
         }

         List<SpvModule$Reflection$Descriptor> allDescriptors = descriptors.values().stream().flatMap(Collection::stream).toList();
         List<SpvModule$Reflection$PushConstant> pushConstants = generatePushConstantList(compiler, spvcResources);
         this.reflection = new SPIRVModule$1(this, inputs, outputs, descriptors, allDescriptors, pushConstants);
      } catch (Throwable var18) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var15) {
               var18.addSuppressed(var15);
            }
         }

         throw var18;
      }

      if (stack != null) {
         stack.close();
      }
   }

   private static List<SpvModule$Reflection$InterfaceVariable> generateInterfaceVariableList(
      final long compiler, final long spvcResources, final IntBuffer spirv, final int resourceType
   ) throws ShaderCompileException {
      ReferenceArrayList<SpvModule$Reflection$InterfaceVariable> list = new ReferenceArrayList();
      MemoryStack stack = MemoryStack.stackPush();

      try {
         PointerBuffer pointerReturnBuffer = stack.callocPointer(1);
         IntBuffer intReturnBuffer = stack.callocInt(1);
         PointerBuffer countPointer = stack.callocPointer(1);
         SpvUtil.throwIfError(
            Spvc.spvc_resources_get_resource_list_for_type(spvcResources, resourceType, pointerReturnBuffer, countPointer), "Couldn't list input variables"
         );
         long spvcList = pointerReturnBuffer.get(0);
         long spvcCount = countPointer.get(0);
         Buffer resources = SpvcReflectedResource.create(spvcList, (int)spvcCount);

         for (int i = 0; i < spvcCount; i++) {
            SpvcReflectedResource resource = (SpvcReflectedResource)resources.get(i);
            String name = resource.nameString();
            if (!Spvc.spvc_compiler_get_binary_offset_for_decoration(compiler, resource.id(), 30, intReturnBuffer)) {
               throw new ShaderCompileException("Couldn't find byte offset for location decoration of " + name);
            }

            int locationOffset = intReturnBuffer.get(0);
            SPIRVModule$TypeHandle typeHandle = new SPIRVModule$TypeHandle(Spvc.spvc_compiler_get_type_handle(compiler, resource.type_id()));
            list.add(new SPIRVModule$2(name, typeHandle, spirv, locationOffset, compiler, resource));
         }
      } catch (Throwable var22) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var21) {
               var22.addSuppressed(var21);
            }
         }

         throw var22;
      }

      if (stack != null) {
         stack.close();
      }

      return ReferenceLists.unmodifiable(list);
   }

   private static List<SpvModule$Reflection$Descriptor> generateDescriptorList(
      final long compiler, final long spvcResources, final IntBuffer spirv, final int resourceType
   ) throws ShaderCompileException {
      ReferenceArrayList<SpvModule$Reflection$Descriptor> list = new ReferenceArrayList();
      MemoryStack stack = MemoryStack.stackPush();

      try {
         PointerBuffer pointerReturnBuffer = stack.callocPointer(1);
         IntBuffer intReturnBuffer = stack.callocInt(1);
         PointerBuffer countPointer = stack.callocPointer(1);
         SpvUtil.throwIfError(
            Spvc.spvc_resources_get_resource_list_for_type(spvcResources, resourceType, pointerReturnBuffer, countPointer), "Couldn't list input variables"
         );
         long spvcList = pointerReturnBuffer.get(0);
         long spvcCount = countPointer.get(0);
         Buffer resources = SpvcReflectedResource.create(spvcList, (int)spvcCount);

         for (int i = 0; i < spvcCount; i++) {
            SpvcReflectedResource resource = (SpvcReflectedResource)resources.get(i);
            String name = resource.nameString();
            if (!Spvc.spvc_compiler_get_binary_offset_for_decoration(compiler, resource.id(), 34, intReturnBuffer)) {
               throw new ShaderCompileException("Couldn't find byte offset for set decoration of " + name);
            }

            int setOffset = intReturnBuffer.get(0);
            if (!Spvc.spvc_compiler_get_binary_offset_for_decoration(compiler, resource.id(), 33, intReturnBuffer)) {
               throw new ShaderCompileException("Couldn't find byte offset for binding decoration of " + name);
            }

            SPIRVModule$TypeHandle typeHandle = new SPIRVModule$TypeHandle(Spvc.spvc_compiler_get_type_handle(compiler, resource.type_id()));
            int bindingOffset = intReturnBuffer.get(0);
            list.add(new SPIRVModule$3(name, typeHandle, resourceType, spirv, setOffset, bindingOffset));
         }
      } catch (Throwable var23) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var22) {
               var23.addSuppressed(var22);
            }
         }

         throw var23;
      }

      if (stack != null) {
         stack.close();
      }

      return ReferenceLists.unmodifiable(list);
   }

   private static List<SpvModule$Reflection$PushConstant> generatePushConstantList(final long compiler, final long spvcResources) throws ShaderCompileException {
      ReferenceArrayList<SpvModule$Reflection$PushConstant> list = new ReferenceArrayList();
      MemoryStack stack = MemoryStack.stackPush();

      try {
         PointerBuffer pointerReturnBuffer = stack.callocPointer(1);
         PointerBuffer countPointer = stack.callocPointer(1);
         SpvUtil.throwIfError(
            Spvc.spvc_resources_get_resource_list_for_type(spvcResources, 9, pointerReturnBuffer, countPointer), "Couldn't list input variables"
         );
         long spvcList = pointerReturnBuffer.get(0);
         long spvcCount = countPointer.get(0);
         Buffer resources = SpvcReflectedResource.create(spvcList, (int)spvcCount);

         for (int i = 0; i < spvcCount; i++) {
            SpvcReflectedResource resource = (SpvcReflectedResource)resources.get(i);
            SPIRVModule$TypeHandle typeHandle = new SPIRVModule$TypeHandle(Spvc.spvc_compiler_get_type_handle(compiler, resource.type_id()));
            Spvc.spvc_compiler_get_declared_struct_size(compiler, typeHandle.typeHandle, pointerReturnBuffer);
            int structSize = Math.toIntExact(pointerReturnBuffer.get(0));
            list.add((SpvModule$Reflection$PushConstant)() -> structSize);
         }
      } catch (Throwable var18) {
         if (stack != null) {
            try {
               stack.close();
            } catch (Throwable var17) {
               var18.addSuppressed(var17);
            }
         }

         throw var18;
      }

      if (stack != null) {
         stack.close();
      }

      return ReferenceLists.unmodifiable(list);
   }
}
