package com.li64.tide.client;

import net.minecraft.client.Minecraft;
import org.lwjgl.sdl.SDLMouse;

/**
 * Switching tabs (Craft / Assemble) or screens (tackle box / rigging) closes one menu and opens another, and the game
 * re-centres the cursor each time. Call {@link #save()} when the button is pressed and {@link #restore()} as the new
 * screen opens to put the cursor back where it was.
 */
public final class MouseKeep {
    private static double x, y;
    private static long savedAt = 0;

    private MouseKeep() {}

    public static void save() {
        Minecraft mc = Minecraft.getInstance();
        x = mc.mouseHandler.xpos();
        y = mc.mouseHandler.ypos();
        savedAt = System.currentTimeMillis();
    }

    public static void restore() {
        if (savedAt == 0) return;
        boolean fresh = System.currentTimeMillis() - savedAt < 3000;
        savedAt = 0;
        if (!fresh) return;
        SDLMouse.SDL_WarpMouseInWindow(Minecraft.getInstance().getWindow().handle(), (float) x, (float) y);
    }
}
