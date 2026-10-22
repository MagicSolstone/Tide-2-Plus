package net.minecraft.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record KeyMapping$Category(Identifier id) {
   private static final List<KeyMapping$Category> SORT_ORDER = new ArrayList<>();
   public static final KeyMapping$Category MOVEMENT = register("movement");
   public static final KeyMapping$Category MISC = register("misc");
   public static final KeyMapping$Category MULTIPLAYER = register("multiplayer");
   public static final KeyMapping$Category GAMEPLAY = register("gameplay");
   public static final KeyMapping$Category INVENTORY = register("inventory");
   public static final KeyMapping$Category CREATIVE = register("creative");
   public static final KeyMapping$Category SPECTATOR = register("spectator");
   public static final KeyMapping$Category DEBUG = register("debug");

   private static KeyMapping$Category register(final String name) {
      return register(Identifier.withDefaultNamespace(name));
   }

   public static KeyMapping$Category register(final Identifier id) {
      KeyMapping$Category category = new KeyMapping$Category(id);
      if (SORT_ORDER.contains(category)) {
         throw new IllegalArgumentException(String.format(Locale.ROOT, "Category '%s' is already registered.", id));
      } else {
         SORT_ORDER.add(category);
         return category;
      }
   }

   public Component label() {
      return Component.translatable(this.id.toLanguageKey("key.category"));
   }
}
