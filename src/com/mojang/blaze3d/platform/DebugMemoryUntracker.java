package com.mojang.blaze3d.platform;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Util;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DebugMemoryUntracker {
   @Nullable
   private static final MethodHandle UNTRACK = Util.make(() -> {
      try {
         Lookup lookup = MethodHandles.lookup();
         Class<?> debugAllocator = Class.forName("org.lwjgl.system.MemoryManage$DebugAllocator");
         Method reflectionUntrack = debugAllocator.getDeclaredMethod("untrack", long.class);
         reflectionUntrack.setAccessible(true);
         Field allocatorField = Class.forName("org.lwjgl.system.MemoryUtil$LazyInit").getDeclaredField("ALLOCATOR");
         allocatorField.setAccessible(true);
         Object allocator = allocatorField.get(null);
         return debugAllocator.isInstance(allocator) ? lookup.unreflect(reflectionUntrack) : null;
      } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException | ClassNotFoundException var5) {
         throw new RuntimeException(var5);
      }
   });

   public static void untrack(final long address) {
      if (UNTRACK != null) {
         try {
            UNTRACK.invoke((long)address);
         } catch (Throwable var3) {
            throw new RuntimeException(var3);
         }
      }
   }
}
