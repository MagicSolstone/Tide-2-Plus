package net.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface RotatingSectionStorage$ValueCreator<T extends RotatingSectionStorage$Value> {
   T createValue(int index, long sectionNode);
}
