package net.minecraft.commands;

public class SharedSuggestionProvider$TextCoordinates {
   public static final SharedSuggestionProvider$TextCoordinates DEFAULT_LOCAL = new SharedSuggestionProvider$TextCoordinates("^", "^", "^");
   public static final SharedSuggestionProvider$TextCoordinates DEFAULT_GLOBAL = new SharedSuggestionProvider$TextCoordinates("~", "~", "~");
   public final String x;
   public final String y;
   public final String z;

   public SharedSuggestionProvider$TextCoordinates(final String x, final String y, final String z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }
}
