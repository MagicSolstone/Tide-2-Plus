package net.minecraft.client.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public enum ChatScreen$ExitReason {
   INTENTIONAL,
   INTERRUPTED,
   DONE;
}
