package com.mojang.realmsclient.util;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.exception.RealmsServiceException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface RealmsUtil$RealmsIoFunction<T> {
   T apply(final RealmsClient client) throws RealmsServiceException;
}
