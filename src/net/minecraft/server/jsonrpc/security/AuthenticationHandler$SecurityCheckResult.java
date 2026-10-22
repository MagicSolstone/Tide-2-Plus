package net.minecraft.server.jsonrpc.security;

class AuthenticationHandler$SecurityCheckResult {
   private final boolean allowed;
   private final String reason;
   private final boolean tokenSentInSecWebsocketProtocol;

   private AuthenticationHandler$SecurityCheckResult(final boolean allowed, final String reason, final boolean tokenSentInSecWebsocketProtocol) {
      this.allowed = allowed;
      this.reason = reason;
      this.tokenSentInSecWebsocketProtocol = tokenSentInSecWebsocketProtocol;
   }

   public static AuthenticationHandler$SecurityCheckResult allowed() {
      return new AuthenticationHandler$SecurityCheckResult(true, null, false);
   }

   public static AuthenticationHandler$SecurityCheckResult allowed(final boolean tokenSentInSecWebsocketProtocol) {
      return new AuthenticationHandler$SecurityCheckResult(true, null, tokenSentInSecWebsocketProtocol);
   }

   public static AuthenticationHandler$SecurityCheckResult denied(final String reason) {
      return new AuthenticationHandler$SecurityCheckResult(false, reason, false);
   }

   public boolean isAllowed() {
      return this.allowed;
   }

   public String getReason() {
      return this.reason;
   }

   public boolean isTokenSentInSecWebsocketProtocol() {
      return this.tokenSentInSecWebsocketProtocol;
   }
}
