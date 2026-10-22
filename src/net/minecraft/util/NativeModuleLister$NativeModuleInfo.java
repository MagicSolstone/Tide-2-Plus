package net.minecraft.util;

import java.util.Optional;

public record NativeModuleLister$NativeModuleInfo(String name, Optional<NativeModuleLister$NativeModuleVersion> version) {
   @Override
   public String toString() {
      return this.version.<String>map(v -> this.name + ":" + v).orElse(this.name);
   }
}
