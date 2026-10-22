package com.mojang.realmsclient.dto;

import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class RealmsServer$McoServerComparator implements Comparator<RealmsServer> {
   private final String refOwner;

   public RealmsServer$McoServerComparator(final String owner) {
      this.refOwner = owner;
   }

   public int compare(final RealmsServer server1, final RealmsServer server2) {
      return ComparisonChain.start()
         .compareTrueFirst(server1.isSnapshotRealm(), server2.isSnapshotRealm())
         .compareTrueFirst(server1.state == RealmsServer$State.UNINITIALIZED, server2.state == RealmsServer$State.UNINITIALIZED)
         .compareTrueFirst(server1.expiredTrial, server2.expiredTrial)
         .compareTrueFirst(Objects.equals(server1.owner, this.refOwner), Objects.equals(server2.owner, this.refOwner))
         .compareFalseFirst(server1.expired, server2.expired)
         .compareTrueFirst(server1.state == RealmsServer$State.OPEN, server2.state == RealmsServer$State.OPEN)
         .compare(server1.id, server2.id)
         .result();
   }
}
