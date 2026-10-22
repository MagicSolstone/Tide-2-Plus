package net.minecraft.client.gui.screens.worldselection;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.gamerules.GameRule;

@FunctionalInterface
@Environment(EnvType.CLIENT)
interface AbstractGameRulesScreen$EntryFactory<T> {
   AbstractGameRulesScreen$RuleEntry create(Component name, List<FormattedCharSequence> tooltip, String narration, GameRule<T> gameRule);
}
