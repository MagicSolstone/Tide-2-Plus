package net.minecraft.world.level.storage;

import com.mojang.serialization.DataResult.Error;
import net.minecraft.util.ProblemReporter$Problem;

public record TagValueOutput$EncodeToListFailedProblem(String name, Object value, Error<?> error) implements ProblemReporter$Problem {
   @Override
   public String description() {
      return "Failed to append value '" + this.value + "' to list '" + this.name + "': " + this.error.message();
   }
}
