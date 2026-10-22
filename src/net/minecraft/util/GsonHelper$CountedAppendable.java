package net.minecraft.util;

class GsonHelper$CountedAppendable implements Appendable {
   private int totalCount;
   private final int limit;

   public GsonHelper$CountedAppendable(final int limit) {
      this.limit = limit;
   }

   private Appendable accountChars(final int count) {
      this.totalCount += count;
      if (this.totalCount > this.limit) {
         throw new IllegalStateException("Character count over limit: " + this.totalCount + " > " + this.limit);
      } else {
         return this;
      }
   }

   @Override
   public Appendable append(final CharSequence csq) {
      return this.accountChars(csq.length());
   }

   @Override
   public Appendable append(final CharSequence csq, final int start, final int end) {
      return this.accountChars(end - start);
   }

   @Override
   public Appendable append(final char c) {
      return this.accountChars(1);
   }
}
