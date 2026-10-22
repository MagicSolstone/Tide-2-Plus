package net.minecraft.client.gui.components.toasts;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SystemToast$SystemToastId {
   public static final SystemToast$SystemToastId NARRATOR_TOGGLE = new SystemToast$SystemToastId();
   public static final SystemToast$SystemToastId WORLD_BACKUP = new SystemToast$SystemToastId();
   public static final SystemToast$SystemToastId PACK_LOAD_FAILURE = new SystemToast$SystemToastId();
   public static final SystemToast$SystemToastId WORLD_ACCESS_FAILURE = new SystemToast$SystemToastId();
   public static final SystemToast$SystemToastId PACK_COPY_FAILURE = new SystemToast$SystemToastId();
   public static final SystemToast$SystemToastId FILE_DROP_FAILURE = new SystemToast$SystemToastId();
   public static final SystemToast$SystemToastId PERIODIC_NOTIFICATION = new SystemToast$SystemToastId();
   public static final SystemToast$SystemToastId LOW_DISK_SPACE = new SystemToast$SystemToastId(10000L);
   public static final SystemToast$SystemToastId CHUNK_LOAD_FAILURE = new SystemToast$SystemToastId();
   public static final SystemToast$SystemToastId CHUNK_SAVE_FAILURE = new SystemToast$SystemToastId();
   public static final SystemToast$SystemToastId UNSECURE_SERVER_WARNING = new SystemToast$SystemToastId(10000L);
   public static final SystemToast$SystemToastId FRIEND_SYSTEM_NOTIFICATION = new SystemToast$SystemToastId();
   private final long displayTime;

   public SystemToast$SystemToastId(final long displayTime) {
      this.displayTime = displayTime;
   }

   public SystemToast$SystemToastId() {
      this(5000L);
   }
}
