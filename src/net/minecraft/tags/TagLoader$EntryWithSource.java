package net.minecraft.tags;

public record TagLoader$EntryWithSource(TagEntry entry, String source) {
   @Override
   public String toString() {
      return this.entry + " (from " + this.source + ")";
   }
}
