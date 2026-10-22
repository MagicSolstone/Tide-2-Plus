package com.mojang.renderpearl.backend.opengl;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.util.HexFormat;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.Nullable;
import org.lwjgl.opengl.ARBDebugOutput;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33C;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLDebugMessageARBCallback;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.opengl.KHRDebug;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class GlDebug {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final int CIRCULAR_LOG_SIZE = 10;
   private final Queue<GlDebug$LogEntry> MESSAGE_BUFFER = EvictingQueue.create(10);
   @Nullable
   private volatile GlDebug$LogEntry lastEntry;
   private static final List<Integer> DEBUG_LEVELS = ImmutableList.of(37190, 37191, 37192, 33387);
   private static final List<Integer> DEBUG_LEVELS_ARB = ImmutableList.of(37190, 37191, 37192);

   private static String printUnknownToken(final int token) {
      return "Unknown (0x" + HexFormat.of().withUpperCase().toHexDigits(token) + ")";
   }

   public static String sourceToString(final int source) {
      return switch (source) {
         case 33350 -> "API";
         case 33351 -> "WINDOW SYSTEM";
         case 33352 -> "SHADER COMPILER";
         case 33353 -> "THIRD PARTY";
         case 33354 -> "APPLICATION";
         case 33355 -> "OTHER";
         default -> printUnknownToken(source);
      };
   }

   public static String typeToString(final int type) {
      return switch (type) {
         case 33356 -> "ERROR";
         case 33357 -> "DEPRECATED BEHAVIOR";
         case 33358 -> "UNDEFINED BEHAVIOR";
         case 33359 -> "PORTABILITY";
         case 33360 -> "PERFORMANCE";
         case 33361 -> "OTHER";
         case 33384 -> "MARKER";
         default -> printUnknownToken(type);
      };
   }

   public static String severityToString(final int severity) {
      return switch (severity) {
         case 33387 -> "NOTIFICATION";
         case 37190 -> "HIGH";
         case 37191 -> "MEDIUM";
         case 37192 -> "LOW";
         default -> printUnknownToken(severity);
      };
   }

   private void printDebugLog(final int source, final int type, final int id, final int severity, final int length, final long message, final long userParam) {
      String msg = GLDebugMessageCallback.getMessage(length, message);
      GlDebug$LogEntry entry;
      synchronized (this.MESSAGE_BUFFER) {
         entry = this.lastEntry;
         if (entry != null && entry.isSame(source, type, id, severity, msg)) {
            entry.count++;
         } else {
            entry = new GlDebug$LogEntry(source, type, id, severity, msg);
            this.MESSAGE_BUFFER.add(entry);
            this.lastEntry = entry;
         }
      }

      LOGGER.info("OpenGL debug message: {}", entry);
   }

   public List<String> getLastOpenGlDebugMessages() {
      synchronized (this.MESSAGE_BUFFER) {
         List<String> result = Lists.newArrayListWithCapacity(this.MESSAGE_BUFFER.size());

         for (GlDebug$LogEntry e : this.MESSAGE_BUFFER) {
            result.add(e + " x " + e.count);
         }

         return result;
      }
   }

   @Nullable
   public static GlDebug enableDebugCallback(final int verbosity, final boolean debugSynchronousGlLogs, final Set<String> enabledExtensions) {
      if (verbosity <= 0) {
         return null;
      } else {
         GLCapabilities caps = GL.getCapabilities();
         if (caps.GL_KHR_debug && GlDevice.USE_GL_KHR_debug) {
            GlDebug debug = new GlDebug();
            enabledExtensions.add("GL_KHR_debug");
            GL33C.glEnable(37600);
            if (debugSynchronousGlLogs) {
               GL33C.glEnable(33346);
            }

            for (int i = 0; i < DEBUG_LEVELS.size(); i++) {
               boolean isEnabled = i < verbosity;
               KHRDebug.glDebugMessageControl(4352, 4352, DEBUG_LEVELS.get(i), (int[])null, isEnabled);
            }

            KHRDebug.glDebugMessageCallback(GLDebugMessageCallback.create(debug::printDebugLog), 0L);
            return debug;
         } else if (caps.GL_ARB_debug_output && GlDevice.USE_GL_ARB_debug_output) {
            GlDebug debug = new GlDebug();
            enabledExtensions.add("GL_ARB_debug_output");
            if (debugSynchronousGlLogs) {
               GL33C.glEnable(33346);
            }

            for (int i = 0; i < DEBUG_LEVELS_ARB.size(); i++) {
               boolean isEnabled = i < verbosity;
               ARBDebugOutput.glDebugMessageControlARB(4352, 4352, DEBUG_LEVELS_ARB.get(i), (int[])null, isEnabled);
            }

            ARBDebugOutput.glDebugMessageCallbackARB(GLDebugMessageARBCallback.create(debug::printDebugLog), 0L);
            return debug;
         } else {
            return null;
         }
      }
   }
}
