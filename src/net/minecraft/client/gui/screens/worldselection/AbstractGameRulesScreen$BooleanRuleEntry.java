package net.minecraft.client.gui.screens.worldselection;

import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.gamerules.GameRule;

@Environment(EnvType.CLIENT)
public class AbstractGameRulesScreen$BooleanRuleEntry extends AbstractGameRulesScreen$GameRuleEntry {
   private final CycleButton<Boolean> checkbox;

   public AbstractGameRulesScreen$BooleanRuleEntry(
      final Component this$0, final List<FormattedCharSequence> name, final String tooltip, final GameRule<Boolean> narration, final GameRule gameRule
   ) {
      Objects.requireNonNull(this$0);
      super(this$0, tooltip, name);
      this.checkbox = CycleButton.onOffBuilder(this$0.gameRules.get(gameRule))
         .displayOnlyValue()
         .withCustomNarration(button -> button.createDefaultNarrationMessage().append("\n").append(narration))
         .create(10, 5, 44, 20, name, (button, newValue) -> this$0.gameRules.set(gameRule, newValue, null));
      this.children.add(this.checkbox);
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      this.extractLabel(graphics, this.getContentY(), this.getContentX());
      this.checkbox.setX(this.getContentRight() - 45);
      this.checkbox.setY(this.getContentY());
      this.checkbox.extractRenderState(graphics, mouseX, mouseY, a);
   }
}
