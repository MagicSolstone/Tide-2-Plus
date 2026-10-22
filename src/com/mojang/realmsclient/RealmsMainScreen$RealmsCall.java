package com.mojang.realmsclient;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.exception.RealmsServiceException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
interface RealmsMainScreen$RealmsCall<T> {
   T request(RealmsClient realmsClient) throws RealmsServiceException;
}
