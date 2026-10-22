package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class Checkbox$Builder {
   private final Component message;
   private final Font font;
   private int maxWidth;
   private int x = 0;
   private int y = 0;
   private Checkbox$OnValueChange onValueChange = Checkbox$OnValueChange.NOP;
   private boolean selected = false;
   @Nullable
   private OptionInstance<Boolean> option = null;
   @Nullable
   private Tooltip tooltip = null;

   private Checkbox$Builder(final Component message, final Font font) {
      this.message = message;
      this.font = font;
      this.maxWidth = Checkbox.getDefaultWidth(message, font);
   }

   public Checkbox$Builder pos(final int x, final int y) {
      this.x = x;
      this.y = y;
      return this;
   }

   public Checkbox$Builder onValueChange(final Checkbox$OnValueChange onValueChange) {
      this.onValueChange = onValueChange;
      return this;
   }

   public Checkbox$Builder selected(final boolean selected) {
      this.selected = selected;
      this.option = null;
      return this;
   }

   public Checkbox$Builder selected(final OptionInstance<Boolean> option) {
      this.option = option;
      this.selected = option.get();
      return this;
   }

   public Checkbox$Builder tooltip(final Tooltip tooltip) {
      this.tooltip = tooltip;
      return this;
   }

   public Checkbox$Builder maxWidth(final int maxWidth) {
      this.maxWidth = maxWidth;
      return this;
   }

   public Checkbox build() {
      Checkbox$OnValueChange onChange = this.option == null ? this.onValueChange : (checkbox, value) -> {
         this.option.set(value);
         this.onValueChange.onValueChange(checkbox, value);
      };
      Checkbox box = new Checkbox(this.x, this.y, this.maxWidth, this.message, this.font, this.selected, onChange);
      if (box.overflowsRowLimit(this.font)) {
         box.setTooltip(this.tooltip);
      }

      return box;
   }
}
