package net.minecraft.client.renderer.block.dispatch.multipart;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;

@Environment(EnvType.CLIENT)
public record CombinedCondition(CombinedCondition$Operation operation, List<Condition> terms) implements Condition {
   @Override
   public <O, S extends StateHolder<O, S>> Predicate<S> instantiate(final StateDefinition<O, S> definition) {
      return this.operation.apply(Lists.transform(this.terms, c -> c.instantiate(definition)));
   }
}
