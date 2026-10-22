package net.minecraft.util;

public enum Util$OS {
   LINUX("linux"),
   SOLARIS("solaris"),
   WINDOWS("windows"),
   OSX("mac"),
   UNKNOWN("unknown");

   private final String telemetryName;

   private Util$OS(final String telemetryName) {
      this.telemetryName = telemetryName;
   }

   public String telemetryName() {
      return this.telemetryName;
   }
}
