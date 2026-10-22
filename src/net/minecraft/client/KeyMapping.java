package net.minecraft.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants$Key;
import com.mojang.blaze3d.platform.InputConstants$Type;
import com.mojang.blaze3d.platform.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class KeyMapping implements Comparable<KeyMapping> {
   private static final Map<String, KeyMapping> ALL = Maps.newHashMap();
   private static final Map<InputConstants$Key, List<KeyMapping>> MAP = Maps.newHashMap();
   private final String name;
   private final InputConstants$Key defaultKey;
   private final KeyMapping$Category category;
   protected InputConstants$Key key;
   private boolean isDown;
   private int clickCount;
   private final int order;

   public static void click(final InputConstants$Key key) {
      forAllKeyMappings(key, keyMapping -> keyMapping.clickCount++);
   }

   public static void set(final InputConstants$Key key, final boolean state) {
      forAllKeyMappings(key, keyMapping -> keyMapping.setDown(state));
   }

   private static void forAllKeyMappings(final InputConstants$Key key, final Consumer<KeyMapping> operation) {
      List<KeyMapping> keyMappings = MAP.get(key);
      if (keyMappings != null && !keyMappings.isEmpty()) {
         for (KeyMapping keyMapping : keyMappings) {
            operation.accept(keyMapping);
         }
      }
   }

   public static void setAll() {
      Window window = Minecraft.getInstance().getWindow();

      for (KeyMapping keyMapping : ALL.values()) {
         if (keyMapping.shouldSetOnIngameFocus()) {
            keyMapping.setDown(InputConstants.isKeyDown(keyMapping.key.getValue()));
         }
      }
   }

   public static void releaseAll() {
      for (KeyMapping keyMapping : ALL.values()) {
         keyMapping.release();
      }
   }

   public static void restoreToggleStatesOnScreenClosed() {
      for (KeyMapping keyMapping : ALL.values()) {
         if (keyMapping instanceof ToggleKeyMapping toggleKeyMapping && toggleKeyMapping.shouldRestoreStateOnScreenClosed()) {
            toggleKeyMapping.setDown(true);
         }
      }
   }

   public static void resetToggleKeys() {
      for (KeyMapping keyMapping : ALL.values()) {
         if (keyMapping instanceof ToggleKeyMapping toggleKeyMapping) {
            toggleKeyMapping.reset();
         }
      }
   }

   public static void resetMapping() {
      MAP.clear();

      for (KeyMapping keyMapping : ALL.values()) {
         keyMapping.registerMapping(keyMapping.key);
      }
   }

   public KeyMapping(final String name, final int keysym, final KeyMapping$Category category) {
      this(name, InputConstants$Type.KEYBOARD, keysym, category);
   }

   public KeyMapping(final String name, final InputConstants$Type type, final int value, final KeyMapping$Category category) {
      this(name, type, value, category, 0);
   }

   public KeyMapping(final String name, final InputConstants$Type type, final int value, final KeyMapping$Category category, final int order) {
      this.name = name;
      this.key = type.getOrCreate(value);
      this.defaultKey = this.key;
      this.category = category;
      this.order = order;
      ALL.put(name, this);
      this.registerMapping(this.key);
   }

   public boolean isDown() {
      return this.isDown;
   }

   public KeyMapping$Category getCategory() {
      return this.category;
   }

   public boolean consumeClick() {
      if (this.clickCount == 0) {
         return false;
      } else {
         this.clickCount--;
         return true;
      }
   }

   protected void release() {
      this.clickCount = 0;
      this.setDown(false);
   }

   protected boolean shouldSetOnIngameFocus() {
      return this.key.getType() == InputConstants$Type.KEYBOARD && this.key.getValue() != InputConstants.UNKNOWN.getValue();
   }

   public String getName() {
      return this.name;
   }

   public InputConstants$Key getDefaultKey() {
      return this.defaultKey;
   }

   public void setKey(final InputConstants$Key key) {
      this.key = key;
   }

   public int compareTo(final KeyMapping o) {
      if (this.category == o.category) {
         return this.order == o.order ? I18n.get(this.name).compareTo(I18n.get(o.name)) : Integer.compare(this.order, o.order);
      } else {
         return Integer.compare(KeyMapping$Category.SORT_ORDER.indexOf(this.category), KeyMapping$Category.SORT_ORDER.indexOf(o.category));
      }
   }

   public static Supplier<Component> createNameSupplier(final String key) {
      KeyMapping map = ALL.get(key);
      return map == null ? () -> Component.translatable(key) : map::getTranslatedKeyMessage;
   }

   public boolean same(final KeyMapping that) {
      return this.key.equals(that.key);
   }

   public boolean isUnbound() {
      return this.key.equals(InputConstants.UNKNOWN);
   }

   public boolean matches(final KeyEvent event) {
      return this.key.getType() == InputConstants$Type.KEYBOARD && this.key.getValue() == event.key() && !this.isUnbound();
   }

   public boolean matchesMouse(final MouseButtonEvent event) {
      return this.key.getType() == InputConstants$Type.MOUSE && this.key.getValue() == event.button() && !this.isUnbound();
   }

   public boolean matches(final InputConstants$Key key) {
      return this.key.equals(key) && !this.isUnbound();
   }

   public Component getTranslatedKeyMessage() {
      return this.key.getDisplayName();
   }

   public boolean isDefault() {
      return this.key.equals(this.defaultKey);
   }

   public String saveString() {
      return this.key.getName();
   }

   public void setDown(final boolean down) {
      this.isDown = down;
   }

   private void registerMapping(final InputConstants$Key key) {
      MAP.computeIfAbsent(key, k -> new ArrayList<>()).add(this);
   }

   @Nullable
   public static KeyMapping get(final String name) {
      return ALL.get(name);
   }
}
