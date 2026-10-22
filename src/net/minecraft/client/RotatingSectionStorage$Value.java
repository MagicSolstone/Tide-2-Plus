package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface RotatingSectionStorage$Value {
   void setSectionNode(long sectionNode);

   long getSectionNode();
}
