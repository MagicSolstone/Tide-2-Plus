package net.minecraft.network.chat;

import java.util.function.Predicate;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.contents.objects.ObjectInfo;
import net.minecraft.world.entity.Entity;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jspecify.annotations.Nullable;

public record ResolutionContext(
   @Nullable CommandSourceStack source,
   @Nullable Entity defaultScoreboardEntity,
   Predicate<ObjectInfo> objectInfoValidator,
   int depthLimit,
   ResolutionContext$LimitBehavior depthLimitBehavior,
   int resolvedComponentLimit,
   MutableInt resolvedComponentCount
) {
   private static final int DEFAULT_RESOLVED_COMPONENT_LIMIT = 65536;

   @Nullable
   public ObjectInfo validate(final ObjectInfo description) {
      return this.objectInfoValidator.test(description) ? description : null;
   }

   public static ResolutionContext create(final CommandSourceStack source) {
      return builder().withSource(source).build();
   }

   public static ResolutionContext$Builder builder() {
      return new ResolutionContext$Builder();
   }
}
