package net.minecraft.world.level.storage;

import com.mojang.serialization.DataResult.Error;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ProblemReporter$Problem;

public record TagValueInput$DecodeFromFieldFailedProblem(String name, Tag tag, Error<?> error) implements ProblemReporter$Problem {
   @Override
   public String description() {
      return "Failed to decode value '" + this.tag + "' from field '" + this.name + "': " + this.error.message();
   }
}
