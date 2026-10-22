package net.minecraft.client;

import com.google.common.base.MoreObjects;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import java.util.Objects;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.LenientJsonParser;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
class Options$2 implements Options$FieldAccess {
   Options$2(final Options this$0, final CompoundTag val$options) {
      this.val$options = val$optionsx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Nullable
   private String getValue(final String name) {
      Tag tag = this.val$options.get(name);
      if (tag == null) {
         return null;
      } else if (tag instanceof StringTag(String var7)) {
         return var7;
      } else {
         throw new IllegalStateException("Cannot read field of wrong type, expected string: " + tag);
      }
   }

   @Override
   public <T> void process(final String name, final OptionInstance<T> option) {
      String result = this.getValue(name);
      if (result != null) {
         JsonElement element = LenientJsonParser.parse(result.isEmpty() ? "\"\"" : result);
         option.codec()
            .parse(JsonOps.INSTANCE, element)
            .ifError(error -> Options.LOGGER.error("Error parsing option value {} for option {}: {}", new Object[]{result, option, error.message()}))
            .ifSuccess(option::set);
      }
   }

   @Override
   public int process(final String name, final int current) {
      String result = this.getValue(name);
      if (result != null) {
         try {
            return Integer.parseInt(result);
         } catch (NumberFormatException var5) {
            Options.LOGGER.warn("Invalid integer value for option {} = {}", new Object[]{name, result, var5});
         }
      }

      return current;
   }

   @Override
   public boolean process(final String name, final boolean current) {
      String result = this.getValue(name);
      return result != null ? Options.isTrue(result) : current;
   }

   @Override
   public String process(final String name, final String current) {
      return (String)MoreObjects.firstNonNull(this.getValue(name), current);
   }

   @Override
   public float process(final String name, final float current) {
      String result = this.getValue(name);
      if (result == null) {
         return current;
      } else if (Options.isTrue(result)) {
         return 1.0F;
      } else if (Options.isFalse(result)) {
         return 0.0F;
      } else {
         try {
            return Float.parseFloat(result);
         } catch (NumberFormatException var5) {
            Options.LOGGER.warn("Invalid floating point value for option {} = {}", new Object[]{name, result, var5});
            return current;
         }
      }
   }

   @Override
   public <T> T process(final String name, final T current, final Function<String, T> reader, final Function<T, String> writer) {
      String rawResult = this.getValue(name);
      return rawResult == null ? current : reader.apply(rawResult);
   }
}
