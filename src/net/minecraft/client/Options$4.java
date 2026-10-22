package net.minecraft.client;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class Options$4 implements Options$OptionAccess {
   Options$4(final Options this$0, final List val$optionsForReport) {
      this.val$optionsForReport = val$optionsForReportx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public <T> void process(final String name, final OptionInstance<T> option) {
      this.val$optionsForReport.add(Pair.of(name, option.get()));
   }
}
