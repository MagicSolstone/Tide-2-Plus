package net.minecraft.client.gui.components.debug;

import java.util.Collection;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public interface DebugScreenDisplayer {
   void addPriorityLine(String line);

   void addLine(String line);

   void addToGroup(final Identifier group, Collection<String> lines);

   void addToGroup(final Identifier group, String lines);
}
