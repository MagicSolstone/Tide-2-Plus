package com.mojang.realmsclient.gui.task;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record DataFetcher$SuccessfulComputationResult<T>(T value, long time) {
}
