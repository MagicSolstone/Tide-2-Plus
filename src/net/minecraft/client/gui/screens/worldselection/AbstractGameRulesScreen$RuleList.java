package net.minecraft.client.gui.screens.worldselection;

import com.google.common.collect.Maps;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRules;

@Environment(EnvType.CLIENT)
public class AbstractGameRulesScreen$RuleList extends ContainerObjectSelectionList<AbstractGameRulesScreen$RuleEntry> {
   private static final int ITEM_HEIGHT = 24;
   private final GameRules gameRules;

   public AbstractGameRulesScreen$RuleList(final AbstractGameRulesScreen this$0, final GameRules gameRules) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(Minecraft.getInstance(), this$0.width, this$0.layout.getContentHeight(), this$0.layout.getHeaderHeight(), 24);
      this.gameRules = gameRules;
      this.populateChildren("");
   }

   private void populateChildren(final String filter) {
      this.clearEntries();
      Map<GameRuleCategory, Map<GameRule<?>, AbstractGameRulesScreen$RuleEntry>> entries = Maps.newHashMap();
      String lowerCaseFilter = filter.toLowerCase(Locale.ROOT);
      this.gameRules.visitGameRuleTypes(new AbstractGameRulesScreen$RuleList$1(this, lowerCaseFilter, entries));
      entries.entrySet()
         .stream()
         .sorted(Map.Entry.comparingByKey(Comparator.comparing(GameRuleCategory::getDescriptionId)))
         .forEach(
            e -> {
               this.addEntry(
                  new AbstractGameRulesScreen$CategoryRuleEntry(
                     this.this$0, e.getKey().label().withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW, ChatFormatting.UNDERLINE)
                  )
               );
               e.getValue()
                  .entrySet()
                  .stream()
                  .sorted(Map.Entry.comparingByKey(Comparator.comparing(GameRule::getDescriptionId)))
                  .forEach(v -> this.addEntry(v.getValue()));
            }
         );
   }

   private static boolean matchesFilter(
      final String gameRuleId,
      final String readableName,
      final String categoryName,
      final Optional<MutableComponent> optionalDescription,
      final String lowerCaseFilter
   ) {
      return toLowerCaseMatchesFilter(gameRuleId, lowerCaseFilter)
         || toLowerCaseMatchesFilter(readableName, lowerCaseFilter)
         || toLowerCaseMatchesFilter(categoryName, lowerCaseFilter)
         || optionalDescription.<Boolean>map(description -> toLowerCaseMatchesFilter(description.getString(), lowerCaseFilter)).orElse(false);
   }

   private static boolean toLowerCaseMatchesFilter(final String gameRuleId, final String lowerCaseFilter) {
      return gameRuleId.toLowerCase(Locale.ROOT).contains(lowerCaseFilter);
   }

   @Override
   public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
      super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
      AbstractGameRulesScreen$RuleEntry hovered = this.getHovered();
      if (hovered != null && hovered.tooltip != null) {
         graphics.setTooltipForNextFrame(hovered.tooltip, mouseX, mouseY);
      }
   }
}
