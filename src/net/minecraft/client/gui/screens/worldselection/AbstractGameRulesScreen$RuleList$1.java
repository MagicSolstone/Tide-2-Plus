package net.minecraft.client.gui.screens.worldselection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

@Environment(EnvType.CLIENT)
class AbstractGameRulesScreen$RuleList$1 implements GameRuleTypeVisitor {
   AbstractGameRulesScreen$RuleList$1(final AbstractGameRulesScreen$RuleList this$1, final String val$lowerCaseFilter, final Map val$entries) {
      this.val$lowerCaseFilter = val$lowerCaseFilterx;
      this.val$entries = val$entriesx;
      Objects.requireNonNull(this$1);
      this.this$1 = this$1;
      super();
   }

   @Override
   public void visitBoolean(final GameRule<Boolean> gameRule) {
      this.addEntry(gameRule, (x$0, x$1, x$2, x$3) -> new AbstractGameRulesScreen$BooleanRuleEntry(this.this$1.this$0, x$0, x$1, x$2, x$3));
   }

   @Override
   public void visitInteger(final GameRule<Integer> gameRule) {
      this.addEntry(gameRule, (x$0, x$1, x$2, x$3) -> new AbstractGameRulesScreen$IntegerRuleEntry(this.this$1.this$0, x$0, x$1, x$2, x$3));
   }

   private <T> void addEntry(final GameRule<T> gameRule, final AbstractGameRulesScreen$EntryFactory<T> factory) {
      Component readableName = Component.translatable(gameRule.getDescriptionId());
      String descriptionKey = gameRule.getDescriptionId() + ".description";
      Optional<MutableComponent> optionalDescription = Optional.of(Component.translatable(descriptionKey)).filter(ComponentUtils::isTranslationResolvable);
      if (AbstractGameRulesScreen$RuleList.matchesFilter(
         gameRule.id(), readableName.getString(), gameRule.category().label().getString(), optionalDescription, this.val$lowerCaseFilter
      )) {
         Component actualName = Component.literal(gameRule.id()).withStyle(ChatFormatting.YELLOW);
         Component defaultValue = Component.translatable("editGamerule.default", Component.literal(gameRule.serialize(gameRule.defaultValue())))
            .withStyle(ChatFormatting.GRAY);
         List<FormattedCharSequence> tooltip;
         String narration;
         if (optionalDescription.isPresent()) {
            Builder<FormattedCharSequence> result = ImmutableList.builder().add(actualName.getVisualOrderText());
            AbstractGameRulesScreen.access$600(this.this$1.this$0).split(optionalDescription.get(), 150).forEach(result::add);
            tooltip = result.add(defaultValue.getVisualOrderText()).build();
            narration = optionalDescription.get().getString() + "\n" + defaultValue.getString();
         } else {
            tooltip = ImmutableList.of(actualName.getVisualOrderText(), defaultValue.getVisualOrderText());
            narration = defaultValue.getString();
         }

         this.val$entries
            .computeIfAbsent(gameRule.category(), k -> Maps.newHashMap())
            .put(gameRule, factory.create(readableName, tooltip, narration, gameRule));
      }
   }
}
