package com.mojang.blaze3d.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record NativeLibrariesBootstrap$LibraryLoadEntry(String name, Runnable loader) {
}
