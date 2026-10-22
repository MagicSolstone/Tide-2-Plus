package net.minecraft.world.level.storage;

import com.mojang.serialization.DataResult.Error;
import net.minecraft.util.ProblemReporter$Problem;

public record TagValueInput$DecodeFromMapFailedProblem(Error<?> error) implements ProblemReporter$Problem {
   @Override
   public String description() {
      return "Failed to decode from map: " + this.error.message();
   }
}
