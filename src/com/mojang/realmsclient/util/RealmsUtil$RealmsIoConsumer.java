package com.mojang.realmsclient.util;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.exception.RealmsServiceException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface RealmsUtil$RealmsIoConsumer extends RealmsUtil$RealmsIoFunction<Void> {
   void accept(final RealmsClient client) throws RealmsServiceException;

   default Void apply(final RealmsClient client) throws RealmsServiceException {
      this.accept(client);
      return null;
   }
}
