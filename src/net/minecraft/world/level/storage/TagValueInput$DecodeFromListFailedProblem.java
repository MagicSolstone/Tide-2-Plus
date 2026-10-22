package net.minecraft.world.level.storage;

import com.mojang.serialization.DataResult.Error;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ProblemReporter$Problem;

public record TagValueInput$DecodeFromListFailedProblem(String name, int index, Tag tag, Error<?> error) implements ProblemReporter$Problem {
   @Override
   public String description() {
      return "Failed to decode value '" + this.tag + "' from field '" + this.name + "' at index " + this.index + "': " + this.error.message();
   }
}
