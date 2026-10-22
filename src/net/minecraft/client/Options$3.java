package net.minecraft.client;

import com.mojang.serialization.JsonOps;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class Options$3 implements Options$FieldAccess {
   Options$3(final Options this$0, final PrintWriter val$writer) {
      this.val$writer = val$writerx;
      Objects.requireNonNull(this$0);
      super();
   }

   public void writePrefix(final String name) {
      this.val$writer.print(name);
      this.val$writer.print(':');
   }

   @Override
   public <T> void process(final String name, final OptionInstance<T> option) {
      option.codec()
         .encodeStart(JsonOps.INSTANCE, option.get())
         .ifError(error -> Options.LOGGER.error("Error saving option {}: {}", option, error.message()))
         .ifSuccess(element -> {
            this.writePrefix(name);
            writer.println(Options.GSON.toJson(element));
         });
   }

   @Override
   public int process(final String name, final int value) {
      this.writePrefix(name);
      this.val$writer.println(value);
      return value;
   }

   @Override
   public boolean process(final String name, final boolean value) {
      this.writePrefix(name);
      this.val$writer.println(value);
      return value;
   }

   @Override
   public String process(final String name, final String value) {
      this.writePrefix(name);
      this.val$writer.println(value);
      return value;
   }

   @Override
   public float process(final String name, final float value) {
      this.writePrefix(name);
      this.val$writer.println(value);
      return value;
   }

   @Override
   public <T> T process(final String name, final T value, final Function<String, T> reader, final Function<T, String> converter) {
      this.writePrefix(name);
      this.val$writer.println(converter.apply(value));
      return value;
   }
}
