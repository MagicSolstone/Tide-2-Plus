package net.minecraft.util;

import com.google.common.base.Ticker;

class Util$1 extends Ticker {
   public long read() {
      return Util.timeSource.getAsLong();
   }
}
