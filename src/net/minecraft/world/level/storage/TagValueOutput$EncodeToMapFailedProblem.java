package net.minecraft.world.level.storage;

import com.mojang.serialization.DataResult.Error;
import net.minecraft.util.ProblemReporter$Problem;

public record TagValueOutput$EncodeToMapFailedProblem(Object value, Error<?> error) implements ProblemReporter$Problem {
   @Override
   public String description() {
      return "Failed to merge value '" + this.value + "' to an object: " + this.error.message();
   }
}
