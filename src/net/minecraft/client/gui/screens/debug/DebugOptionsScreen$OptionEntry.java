package net.minecraft.client.gui.screens.debug;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.client.gui.components.debug.DebugScreenEntryStatus;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
class DebugOptionsScreen$OptionEntry extends DebugOptionsScreen$AbstractOptionEntry {
   private static final int BUTTON_WIDTH = 60;
   private final Identifier location;
   protected final List<AbstractWidget> children;
   private final CycleButton<Boolean> always;
   private final CycleButton<Boolean> overlay;
   private final CycleButton<Boolean> never;
   private final String name;
   private final boolean isAllowed;

   public DebugOptionsScreen$OptionEntry(final DebugOptionsScreen this$0, final Identifier location) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.children = Lists.newArrayList();
      this.location = location;
      DebugScreenEntry entry = DebugScreenEntries.getEntry(location);
      this.isAllowed = entry != null && entry.isAllowed(DebugOptionsScreen.access$100(this$0x).showOnlyReducedInfo());
      String name = location.getPath();
      if (this.isAllowed) {
         this.name = name;
      } else {
         this.name = ChatFormatting.ITALIC + name;
      }

      this.always = CycleButton.booleanBuilder(
            DebugOptionsScreen.ENABLED_TEXT.copy().withColor(-2142128), DebugOptionsScreen.ENABLED_TEXT.copy().withColor(-4539718), false
         )
         .displayOnlyValue()
         .withCustomNarration(this::narrateButton)
         .create(10, 5, 60, 16, Component.literal(name), (button, newValue) -> this.setValue(location, DebugScreenEntryStatus.ALWAYS_ON));
      this.overlay = CycleButton.booleanBuilder(
            DebugOptionsScreen.IN_OVERLAY_TEXT.copy().withColor(-171), DebugOptionsScreen.IN_OVERLAY_TEXT.copy().withColor(-4539718), false
         )
         .displayOnlyValue()
         .withCustomNarration(this::narrateButton)
         .create(10, 5, 60, 16, Component.literal(name), (button, newValue) -> this.setValue(location, DebugScreenEntryStatus.IN_OVERLAY));
      this.never = CycleButton.booleanBuilder(
            DebugOptionsScreen.DISABLED_TEXT.copy().withColor(-1), DebugOptionsScreen.DISABLED_TEXT.copy().withColor(-4539718), false
         )
         .displayOnlyValue()
         .withCustomNarration(this::narrateButton)
         .create(10, 5, 60, 16, Component.literal(name), (button, newValue) -> this.setValue(location, DebugScreenEntryStatus.NEVER));
      this.children.add(this.never);
      this.children.add(this.overlay);
      this.children.add(this.always);
      this.refreshEntry();
   }

   private MutableComponent narrateButton(final CycleButton<Boolean> booleanCycleButton) {
      DebugScreenEntryStatus status = DebugOptionsScreen.access$200(this.this$0).debugEntries.getStatus(this.location);
      MutableComponent current = Component.translatable("debug.entry.currently." + status.getSerializedName(), this.name);
      return CommonComponents.optionNameValue(current, booleanCycleButton.getMessage());
   }

   private void setValue(final Identifier location, final DebugScreenEntryStatus never) {
      DebugOptionsScreen.access$300(this.this$0).debugEntries.setStatus(location, never);

      for (Button profileButton : this.this$0.profileButtons) {
         profileButton.active = true;
      }

      this.refreshEntry();
   }

   @Override
   public List<? extends GuiEventListener> children() {
      return this.children;
   }

   @Override
   public List<? extends NarratableEntry> narratables() {
      return this.children;
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      int x = this.getContentX();
      int y = this.getContentY();
      graphics.text(DebugOptionsScreen.access$400(this.this$0).font, this.name, x, y + 5, this.isAllowed ? -1 : -8355712);
      int buttonsStartX = x + this.getContentWidth() - this.never.getWidth() - this.overlay.getWidth() - this.always.getWidth();
      if (!this.isAllowed && hovered && mouseX < buttonsStartX) {
         graphics.setTooltipForNextFrame(DebugOptionsScreen.NOT_ALLOWED_TOOLTIP, mouseX, mouseY);
      }

      this.never.setX(buttonsStartX);
      this.overlay.setX(this.never.getX() + this.never.getWidth());
      this.always.setX(this.overlay.getX() + this.overlay.getWidth());
      this.always.setY(y);
      this.overlay.setY(y);
      this.never.setY(y);
      this.always.extractRenderState(graphics, mouseX, mouseY, a);
      this.overlay.extractRenderState(graphics, mouseX, mouseY, a);
      this.never.extractRenderState(graphics, mouseX, mouseY, a);
   }

   @Override
   public void refreshEntry() {
      DebugScreenEntryStatus status = DebugOptionsScreen.access$500(this.this$0).debugEntries.getStatus(this.location);
      this.always.setValue(status == DebugScreenEntryStatus.ALWAYS_ON);
      this.overlay.setValue(status == DebugScreenEntryStatus.IN_OVERLAY);
      this.never.setValue(status == DebugScreenEntryStatus.NEVER);
      this.always.active = !this.always.getValue();
      this.overlay.active = !this.overlay.getValue();
      this.never.active = !this.never.getValue();
   }
}
