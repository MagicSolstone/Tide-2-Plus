package net.minecraft.server.players;

import java.util.Date;

class CachedUserNameToIdResolver$GameProfileInfo {
   private final NameAndId nameAndId;
   private final Date expirationDate;
   private volatile long lastAccess;

   private CachedUserNameToIdResolver$GameProfileInfo(final NameAndId nameAndId, final Date expirationDate) {
      this.nameAndId = nameAndId;
      this.expirationDate = expirationDate;
   }

   public NameAndId nameAndId() {
      return this.nameAndId;
   }

   public Date expirationDate() {
      return this.expirationDate;
   }

   public void setLastAccess(final long currentOperation) {
      this.lastAccess = currentOperation;
   }

   public long lastAccess() {
      return this.lastAccess;
   }
}
