package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.server.packs.repository.Pack;

interface DataPackCommand$Inserter {
   void apply(final List<Pack> selected, final Pack pack) throws CommandSyntaxException;
}
