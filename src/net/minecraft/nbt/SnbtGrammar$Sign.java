package net.minecraft.nbt;

enum SnbtGrammar$Sign {
   PLUS,
   MINUS;

   public void append(final StringBuilder output) {
      if (this == MINUS) {
         output.append("-");
      }
   }
}
