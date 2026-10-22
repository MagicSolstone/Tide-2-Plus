package com.mojang.realmsclient.gui.task;

import com.mojang.datafixers.util.Either;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
record DataFetcher$ComputationResult<T>(Either<T, Exception> value, long time) {
}
