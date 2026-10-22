package net.minecraft.client.gui.screens;

import java.io.IOException;
import java.io.Reader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
interface WinScreen$CreditsReader {
   void read(final Reader reader) throws IOException;
}
