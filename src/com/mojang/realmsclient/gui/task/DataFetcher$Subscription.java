package com.mojang.realmsclient.gui.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class DataFetcher$Subscription {
   private final List<DataFetcher$SubscribedTask<?>> subscriptions;

   public DataFetcher$Subscription(final DataFetcher this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.subscriptions = new ArrayList<>();
   }

   public <T> void subscribe(final DataFetcher$Task<T> task, final Consumer<T> output) {
      DataFetcher$SubscribedTask<T> subscription = new DataFetcher$SubscribedTask<>(this.this$0, task, output);
      this.subscriptions.add(subscription);
      subscription.runCallbackIfNeeded();
   }

   public void forceUpdate() {
      for (DataFetcher$SubscribedTask<?> subscription : this.subscriptions) {
         subscription.runCallback();
      }
   }

   public void tick() {
      for (DataFetcher$SubscribedTask<?> subscription : this.subscriptions) {
         subscription.update(this.this$0.timeSource.get(this.this$0.resolution));
      }
   }

   public void reset() {
      for (DataFetcher$SubscribedTask<?> subscription : this.subscriptions) {
         subscription.reset();
      }
   }
}
