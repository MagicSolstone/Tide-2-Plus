package net.minecraft.gametest.framework;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;

public record TestEnvironmentDefinition$AllOf(List<Holder<TestEnvironmentDefinition<?>>> definitions)
   implements TestEnvironmentDefinition<List<? extends TestEnvironmentDefinition$Activation<?>>> {
   public static final MapCodec<TestEnvironmentDefinition$AllOf> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(TestEnvironmentDefinition.CODEC.listOf().fieldOf("definitions").forGetter(TestEnvironmentDefinition$AllOf::definitions))
         .apply(i, TestEnvironmentDefinition$AllOf::new)
   );

   public TestEnvironmentDefinition$AllOf(final TestEnvironmentDefinition<?>... defs) {
      this(Arrays.stream(defs).map(TestEnvironmentDefinition$AllOf::holder).toList());
   }

   private static Holder<TestEnvironmentDefinition<?>> holder(final TestEnvironmentDefinition<?> holder) {
      return Holder.direct(holder);
   }

   public List<? extends TestEnvironmentDefinition$Activation<?>> setup(final ServerLevel level) {
      return this.definitions.stream().map(b -> TestEnvironmentDefinition.activate(b.value(), level)).toList();
   }

   public void teardown(final ServerLevel level, final List<? extends TestEnvironmentDefinition$Activation<?>> activations) {
      activations.reversed().forEach(TestEnvironmentDefinition$Activation::teardown);
   }

   @Override
   public MapCodec<TestEnvironmentDefinition$AllOf> codec() {
      return CODEC;
   }
}
