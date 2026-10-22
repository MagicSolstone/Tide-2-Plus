package net.minecraft.data;

public enum PackOutput$Target {
   DATA_PACK("data"),
   RESOURCE_PACK("assets"),
   REPORTS("reports");

   private final String directory;

   private PackOutput$Target(final String directory) {
      this.directory = directory;
   }
}
