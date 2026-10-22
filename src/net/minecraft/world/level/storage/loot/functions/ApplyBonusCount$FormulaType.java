package net.minecraft.world.level.storage.loot.functions;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

record ApplyBonusCount$FormulaType(Identifier id, Codec<? extends ApplyBonusCount$Formula> codec) {
}
