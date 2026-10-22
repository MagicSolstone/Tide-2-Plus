package net.minecraft.client.gui.screens.worldselection;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.ContainerObjectSelectionList$Entry;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class AbstractGameRulesScreen$RuleEntry extends ContainerObjectSelectionList$Entry<AbstractGameRulesScreen$RuleEntry> {
   @Nullable
   private final List<FormattedCharSequence> tooltip;

   public AbstractGameRulesScreen$RuleEntry(@Nullable final List<FormattedCharSequence> tooltip) {
      this.tooltip = tooltip;
   }
}
