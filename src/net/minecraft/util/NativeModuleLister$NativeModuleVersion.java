package net.minecraft.util;

public record NativeModuleLister$NativeModuleVersion(String description, String version, String company) {
   @Override
   public String toString() {
      return this.description + ":" + this.version + ":" + this.company;
   }
}
