package com.mojang.blaze3d.platform.cursor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.sdl.SDLMouse;

@Environment(EnvType.CLIENT)
public class CursorType {
   public static final CursorType DEFAULT = new CursorType("default", 0L);
   private final String name;
   private final long handle;

   private CursorType(final String name, final long handle) {
      this.name = name;
      this.handle = handle;
   }

   public void select() {
      long cursor = this.handle == 0L ? SDLMouse.SDL_GetDefaultCursor() : this.handle;
      SDLMouse.SDL_SetCursor(cursor);
   }

   @Override
   public String toString() {
      return this.name;
   }

   public static CursorType createStandardCursor(final int shape, final String name, final CursorType fallback) {
      long handle = SDLMouse.SDL_CreateSystemCursor(shape);
      return handle == 0L ? fallback : new CursorType(name, handle);
   }
}
