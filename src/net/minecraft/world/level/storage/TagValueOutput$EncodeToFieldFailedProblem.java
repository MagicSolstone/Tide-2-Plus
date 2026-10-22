package net.minecraft.world.level.storage;

import com.mojang.serialization.DataResult.Error;
import net.minecraft.util.ProblemReporter$Problem;

public record TagValueOutput$EncodeToFieldFailedProblem(String name, Object value, Error<?> error) implements ProblemReporter$Problem {
   @Override
   public String description() {
      return "Failed to encode value '" + this.value + "' to field '" + this.name + "': " + this.error.message();
   }
}
