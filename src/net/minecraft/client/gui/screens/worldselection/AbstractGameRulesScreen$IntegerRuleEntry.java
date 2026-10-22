package net.minecraft.client.gui.screens.worldselection;

import com.mojang.serialization.DataResult;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.gamerules.GameRule;

@Environment(EnvType.CLIENT)
public class AbstractGameRulesScreen$IntegerRuleEntry extends AbstractGameRulesScreen$GameRuleEntry {
   private final EditBox input;

   public AbstractGameRulesScreen$IntegerRuleEntry(
      final Component this$0, final List<FormattedCharSequence> label, final String tooltip, final GameRule<Integer> narration, final GameRule gameRule
   ) {
      Objects.requireNonNull(this$0);
      super(this$0, tooltip, label);
      this.input = new EditBox(AbstractGameRulesScreen.access$500(this$0).font, 10, 5, 44, 20, label.copy().append("\n").append(narration).append("\n"));
      this.input.setValue(this$0.gameRules.getAsString(gameRule));
      this.input.setResponder(v -> {
         DataResult<Integer> value = gameRule.deserialize(v);
         if (value.isSuccess()) {
            this.input.setTextColor(-2039584);
            this$0.clearInvalid(this);
            this$0.gameRules.set(gameRule, (Integer)value.getOrThrow(), null);
         } else {
            this.input.setTextColor(-65536);
            this$0.markInvalid(this);
         }
      });
      this.children.add(this.input);
   }

   @Override
   public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovered, final float a) {
      this.extractLabel(graphics, this.getContentY(), this.getContentX());
      this.input.setX(this.getContentRight() - 45);
      this.input.setY(this.getContentY());
      this.input.extractRenderState(graphics, mouseX, mouseY, a);
   }
}
