package net.minecraft.util.parsing.packrat;

import java.util.Objects;
import org.jspecify.annotations.Nullable;

class CachedParseState$Silent implements ParseState<S> {
   private final ErrorCollector<S> silentCollector;

   private CachedParseState$Silent(final CachedParseState this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.silentCollector = new ErrorCollector$Nop();
   }

   @Override
   public ErrorCollector<S> errorCollector() {
      return this.silentCollector;
   }

   @Override
   public Scope scope() {
      return this.this$0.scope();
   }

   @Nullable
   @Override
   public <T> T parse(final NamedRule<S, T> rule) {
      return this.this$0.parse(rule);
   }

   @Override
   public S input() {
      return (S)this.this$0.input();
   }

   @Override
   public int mark() {
      return this.this$0.mark();
   }

   @Override
   public void restore(final int mark) {
      this.this$0.restore(mark);
   }

   @Override
   public Control acquireControl() {
      return this.this$0.acquireControl();
   }

   @Override
   public void releaseControl() {
      this.this$0.releaseControl();
   }

   @Override
   public ParseState<S> silent() {
      return this;
   }
}
