package net.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface Checkbox$OnValueChange {
   Checkbox$OnValueChange NOP = (checkbox, value) -> {};

   void onValueChange(Checkbox checkbox, boolean value);
}
