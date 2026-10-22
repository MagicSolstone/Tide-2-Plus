package net.minecraft.client.gui.screens.options.controls;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

@Environment(EnvType.CLIENT)
public class KeyBindsList$KeyEntry extends KeyBindsList$Entry {
   private static final Component RESET_BUTTON_TITLE = Component.translatable("controls.reset");
   private static final int PADDING = 10;
   private final KeyMapping key;
   private final Component name;
   private final Button changeButton;
   private final Button resetButton;
   private boolean hasCollision;

   private KeyBindsList$KeyEntry(final KeyBindsList this$0, final KeyMapping key, final Component name) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.hasCollision = false;
      this.key = key;
      this.name = name;
      this.changeButton = Button.builder(name, button -> {
            this$0.keyBindsScreen.selectedKey = key;
            this$0.resetMappingAndUpdateButtons();
         })
         .bounds(0, 0, 75, 20)
         .createNarration(
            defaultNarrationSupplier -> key.isUnbound()
               ? Component.translatable("narrator.controls.unbound", name)
               : Component.translatable("narrator.controls.bound", name, defaultNarrationSupplier.get())
         )
         .build();
      this.resetButton = Button.builder(RESET_BUTTON_TITLE, button -> {
         key.setKey(key.getDefaultKey());
         this$0.resetMappingAndUpdateButtons();
      }).bounds(0, 0, 50, 20).createNarration(defaultNarrationSupplier -> Component.translatable("narrator.controls.reset", name)).build();
      this.refreshEntry();
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      this.updateChangeButtonMessage();
      int resetButtonX = KeyBindsList.access$200(this.this$0) - this.resetButton.getWidth() - 10;
      int buttonY = this.getContentY() - 2;
      this.resetButton.setPosition(resetButtonX, buttonY);
      this.resetButton.extractRenderState(graphics, mouseX, mouseY, a);
      int changeButtonX = resetButtonX - 5 - this.changeButton.getWidth();
      this.changeButton.setPosition(changeButtonX, buttonY);
      this.changeButton.extractRenderState(graphics, mouseX, mouseY, a);
      graphics.text(KeyBindsList.access$300(this.this$0).font, this.name, this.getContentX(), this.getContentYMiddle() - 9 / 2, -1);
      if (this.hasCollision) {
         int stripeWidth = 3;
         int stripeLeft = this.changeButton.getX() - 6;
         graphics.fill(stripeLeft, this.getContentY() - 1, stripeLeft + 3, this.getContentBottom(), -256);
      }
   }

   @Override
   public List<? extends GuiEventListener> children() {
      return ImmutableList.of(this.changeButton, this.resetButton);
   }

   @Override
   public List<? extends NarratableEntry> narratables() {
      return ImmutableList.of(this.changeButton, this.resetButton);
   }

   @Override
   public void refreshEntry() {
      this.resetButton.active = !this.key.isDefault();
      this.hasCollision = false;
      MutableComponent tooltip = Component.empty();
      if (!this.key.isUnbound()) {
         for (KeyMapping otherKey : KeyBindsList.access$500(this.this$0).options.keyMappings) {
            if (otherKey != this.key && this.key.same(otherKey) && (!otherKey.isDefault() || !this.key.isDefault())) {
               if (this.hasCollision) {
                  tooltip.append(", ");
               }

               this.hasCollision = true;
               tooltip.append(Component.translatable(otherKey.getName()));
            }
         }
      }

      if (this.hasCollision) {
         this.changeButton.setTooltip(Tooltip.create(Component.translatable("controls.keybinds.duplicateKeybinds", tooltip)));
      } else {
         this.changeButton.setTooltip(null);
      }

      this.updateChangeButtonMessage();
   }

   private void updateChangeButtonMessage() {
      Component message = this.key.getTranslatedKeyMessage();
      if (this.hasCollision) {
         message = Component.literal("[ ").append(message.copy().withStyle(ChatFormatting.WHITE)).append(" ]").withStyle(ChatFormatting.YELLOW);
      }

      if (this.this$0.keyBindsScreen.selectedKey == this.key) {
         message = Component.literal("> ")
            .append(message.copy().withStyle(ChatFormatting.WHITE, ChatFormatting.UNDERLINE))
            .append(" <")
            .withStyle(ChatFormatting.YELLOW);
      }

      this.changeButton.setMessage(message);
   }
}
