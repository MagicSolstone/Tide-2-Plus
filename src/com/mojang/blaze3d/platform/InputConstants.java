package com.mojang.blaze3d.platform;

import com.mojang.logging.LogUtils;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.sdl.SDLError;
import org.lwjgl.sdl.SDLKeyboard;
import org.lwjgl.sdl.SDLMouse;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class InputConstants {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final int KEY_0 = 39;
   public static final int KEY_1 = 30;
   public static final int KEY_2 = 31;
   public static final int KEY_3 = 32;
   public static final int KEY_4 = 33;
   public static final int KEY_5 = 34;
   public static final int KEY_6 = 35;
   public static final int KEY_7 = 36;
   public static final int KEY_8 = 37;
   public static final int KEY_9 = 38;
   public static final int KEY_A = 4;
   public static final int KEY_B = 5;
   public static final int KEY_C = 6;
   public static final int KEY_D = 7;
   public static final int KEY_E = 8;
   public static final int KEY_F = 9;
   public static final int KEY_G = 10;
   public static final int KEY_H = 11;
   public static final int KEY_I = 12;
   public static final int KEY_J = 13;
   public static final int KEY_K = 14;
   public static final int KEY_L = 15;
   public static final int KEY_M = 16;
   public static final int KEY_N = 17;
   public static final int KEY_O = 18;
   public static final int KEY_P = 19;
   public static final int KEY_Q = 20;
   public static final int KEY_R = 21;
   public static final int KEY_S = 22;
   public static final int KEY_T = 23;
   public static final int KEY_U = 24;
   public static final int KEY_V = 25;
   public static final int KEY_W = 26;
   public static final int KEY_X = 27;
   public static final int KEY_Y = 28;
   public static final int KEY_Z = 29;
   public static final int KEY_F1 = 58;
   public static final int KEY_F2 = 59;
   public static final int KEY_F3 = 60;
   public static final int KEY_F4 = 61;
   public static final int KEY_F5 = 62;
   public static final int KEY_F6 = 63;
   public static final int KEY_F7 = 64;
   public static final int KEY_F8 = 65;
   public static final int KEY_F9 = 66;
   public static final int KEY_F10 = 67;
   public static final int KEY_F11 = 68;
   public static final int KEY_F12 = 69;
   public static final int KEY_F13 = 104;
   public static final int KEY_F14 = 105;
   public static final int KEY_F15 = 106;
   public static final int KEY_F16 = 107;
   public static final int KEY_F17 = 108;
   public static final int KEY_F18 = 109;
   public static final int KEY_F19 = 110;
   public static final int KEY_F20 = 111;
   public static final int KEY_F21 = 112;
   public static final int KEY_F22 = 113;
   public static final int KEY_F23 = 114;
   public static final int KEY_F24 = 115;
   public static final int KEY_NUMLOCK = 83;
   public static final int KEY_NUMPAD0 = 98;
   public static final int KEY_NUMPAD1 = 89;
   public static final int KEY_NUMPAD2 = 90;
   public static final int KEY_NUMPAD3 = 91;
   public static final int KEY_NUMPAD4 = 92;
   public static final int KEY_NUMPAD5 = 93;
   public static final int KEY_NUMPAD6 = 94;
   public static final int KEY_NUMPAD7 = 95;
   public static final int KEY_NUMPAD8 = 96;
   public static final int KEY_NUMPAD9 = 97;
   public static final int KEY_NUMPADCOMMA = 220;
   public static final int KEY_NUMPADENTER = 88;
   public static final int KEY_NUMPADEQUALS = 103;
   public static final int KEY_DOWN = 81;
   public static final int KEY_LEFT = 80;
   public static final int KEY_RIGHT = 79;
   public static final int KEY_UP = 82;
   public static final int KEY_ADD = 87;
   public static final int KEY_APOSTROPHE = 52;
   public static final int KEY_BACKSLASH = 49;
   public static final int KEY_COMMA = 54;
   public static final int KEY_EQUALS = 46;
   public static final int KEY_GRAVE = 53;
   public static final int KEY_LBRACKET = 47;
   public static final int KEY_MINUS = 45;
   public static final int KEY_MULTIPLY = 85;
   public static final int KEY_PERIOD = 55;
   public static final int KEY_RBRACKET = 48;
   public static final int KEY_SEMICOLON = 51;
   public static final int KEY_SLASH = 56;
   public static final int KEY_SPACE = 44;
   public static final int KEY_TAB = 43;
   public static final int KEY_LALT = 226;
   public static final int KEY_LCONTROL = 224;
   public static final int KEY_LSHIFT = 225;
   public static final int KEY_LGUI = 227;
   public static final int KEY_RALT = 230;
   public static final int KEY_RCONTROL = 228;
   public static final int KEY_RSHIFT = 229;
   public static final int KEY_RGUI = 231;
   public static final int KEY_RETURN = 40;
   public static final int KEY_ESCAPE = 41;
   public static final int KEY_BACKSPACE = 42;
   public static final int KEY_DELETE = 76;
   public static final int KEY_END = 77;
   public static final int KEY_HOME = 74;
   public static final int KEY_INSERT = 73;
   public static final int KEY_PAGEDOWN = 78;
   public static final int KEY_PAGEUP = 75;
   public static final int KEY_CAPSLOCK = 57;
   public static final int KEY_PAUSE = 72;
   public static final int KEY_SCROLLLOCK = 71;
   public static final int KEY_PRINTSCREEN = 70;
   public static final int PRESS = 1;
   public static final int RELEASE = 0;
   public static final int REPEAT = -1;
   public static final int MOUSE_BUTTON_LEFT = 1;
   public static final int MOUSE_BUTTON_MIDDLE = 2;
   public static final int MOUSE_BUTTON_RIGHT = 3;
   public static final int MOUSE_BUTTON_4 = 4;
   public static final int MOUSE_BUTTON_5 = 5;
   public static final int MOUSE_BUTTON_6 = 6;
   public static final int MOUSE_BUTTON_7 = 7;
   public static final int MOUSE_BUTTON_8 = 8;
   public static final int MOD_SHIFT = 3;
   public static final int MOD_CONTROL = 192;
   public static final int MOD_ALT = 768;
   public static final int MOD_SUPER = 3072;
   public static final int MOD_CAPS_LOCK = 8192;
   public static final int MOD_NUM_LOCK = 4096;
   public static final int KEYCODE_A = 97;
   public static final int KEYCODE_B = 98;
   public static final int KEYCODE_C = 99;
   public static final int KEYCODE_E = 101;
   public static final int KEYCODE_F = 102;
   public static final int KEYCODE_L = 108;
   public static final int KEYCODE_M = 109;
   public static final int KEYCODE_O = 111;
   public static final int KEYCODE_R = 114;
   public static final int KEYCODE_U = 117;
   public static final int KEYCODE_V = 118;
   public static final int KEYCODE_W = 119;
   public static final int KEYCODE_X = 120;
   public static final int KEYCODE_Y = 121;
   public static final int KEYCODE_Z = 122;
   public static final int KEYCODE_RETURN = 13;
   public static final int KEYCODE_NUMPADENTER = 1073741912;
   public static final int KEYCODE_PAGEUP = 1073741899;
   public static final int KEYCODE_PAGEDOWN = 1073741902;
   public static final int KEYCODE_BACKSPACE = 8;
   public static final int KEYCODE_UP = 1073741906;
   public static final int KEYCODE_DOWN = 1073741905;
   public static final int KEYCODE_FORWARD = 1073741906;
   public static final int KEYCODE_BACKWARD = 1073741905;
   public static final int KEYCODE_LEFT = 1073741904;
   public static final int KEYCODE_RIGHT = 1073741903;
   public static final int KEYCODE_NUMPAD9 = 1073741921;
   public static final int KEYCODE_NUMPAD3 = 1073741915;
   public static final int KEYCODE_DELETE = 127;
   public static final int KEYCODE_HOME = 1073741898;
   public static final int KEYCODE_END = 1073741901;
   public static final int KEYCODE_F5 = 1073741886;
   public static final int KEYCODE_TAB = 9;
   public static final int KEYCODE_LCONTROL = 1073742048;
   public static final int KEYCODE_RCONTROL = 1073742052;
   public static final int KEYCODE_SPACE = 32;
   public static final InputConstants$Key UNKNOWN = InputConstants$Type.KEYBOARD.getOrCreate(0);

   public static InputConstants$Key getKey(final KeyEvent event) {
      return InputConstants$Type.KEYBOARD.getOrCreate(event.key());
   }

   public static InputConstants$Key getKey(final String name) {
      if (InputConstants$Key.NAME_MAP.containsKey(name)) {
         return InputConstants$Key.NAME_MAP.get(name);
      } else {
         for (InputConstants$Type type : InputConstants$Type.values()) {
            if (name.startsWith(type.defaultPrefix)) {
               String humanReadableValue = name.substring(type.defaultPrefix.length() + 1);
               int intValue = Integer.parseInt(humanReadableValue);
               return type.getOrCreate(intValue);
            }
         }

         throw new IllegalArgumentException("Unknown key name: " + name);
      }
   }

   public static boolean isKeyDown(final int key) {
      ByteBuffer keyboardState = SDLKeyboard.SDL_GetKeyboardState();
      return keyboardState != null && keyboardState.get(key) != 0;
   }

   public static void grabMouse(final Window window, final double xpos, final double ypos) {
      SDLMouse.SDL_WarpMouseInWindow(window.handle(), (float)xpos, (float)ypos);
      if (!SDLMouse.SDL_SetWindowRelativeMouseMode(window.handle(), true)) {
         LOGGER.warn("Failed to enable relative mouse mode: {}", SDLError.SDL_GetError());
      }
   }

   public static void releaseMouse(final Window window, final double xpos, final double ypos) {
      SDLMouse.SDL_WarpMouseInWindow(window.handle(), (float)xpos, (float)ypos);
      if (!SDLMouse.SDL_SetWindowRelativeMouseMode(window.handle(), false)) {
         LOGGER.warn("Failed to disable relative mouse mode: {}", SDLError.SDL_GetError());
      }
   }
}
