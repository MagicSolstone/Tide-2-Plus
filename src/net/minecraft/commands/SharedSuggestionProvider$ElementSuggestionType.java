package net.minecraft.commands;

public enum SharedSuggestionProvider$ElementSuggestionType {
   TAGS,
   ELEMENTS,
   ALL;

   public boolean shouldSuggestTags() {
      return this == TAGS || this == ALL;
   }

   public boolean shouldSuggestElements() {
      return this == ELEMENTS || this == ALL;
   }
}
