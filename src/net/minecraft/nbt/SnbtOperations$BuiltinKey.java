package net.minecraft.nbt;

public record SnbtOperations$BuiltinKey(String id, int argCount) {
   @Override
   public String toString() {
      return this.id + "/" + this.argCount;
   }
}
