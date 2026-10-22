package net.minecraft.server.rcon.thread;

import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Locale;
import net.minecraft.util.RandomSource;

class QueryThreadGs4$RequestChallenge {
   private final long time = new Date().getTime();
   private final int challenge;
   private final byte[] identBytes;
   private final byte[] challengeBytes;
   private final String ident;

   public QueryThreadGs4$RequestChallenge(final DatagramPacket src) {
      byte[] buf = src.getData();
      this.identBytes = new byte[4];
      this.identBytes[0] = buf[3];
      this.identBytes[1] = buf[4];
      this.identBytes[2] = buf[5];
      this.identBytes[3] = buf[6];
      this.ident = new String(this.identBytes, StandardCharsets.UTF_8);
      this.challenge = RandomSource.createThreadLocalInstance().nextInt(16777216);
      this.challengeBytes = String.format(Locale.ROOT, "\t%s%d\u0000", this.ident, this.challenge).getBytes(StandardCharsets.UTF_8);
   }

   public Boolean before(final long time) {
      return this.time < time;
   }

   public int getChallenge() {
      return this.challenge;
   }

   public byte[] getChallengeBytes() {
      return this.challengeBytes;
   }

   public byte[] getIdentBytes() {
      return this.identBytes;
   }

   public String getIdent() {
      return this.ident;
   }
}
