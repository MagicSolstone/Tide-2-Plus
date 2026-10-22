package net.minecraft.world.level.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Rotation;

public record TestInstanceBlockEntity$Data(
   Optional<ResourceKey<GameTestInstance>> test,
   Vec3i size,
   Rotation rotation,
   boolean ignoreEntities,
   TestInstanceBlockEntity$Status status,
   Optional<Component> errorMessage
) {
   public static final Codec<TestInstanceBlockEntity$Data> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            ResourceKey.codec(Registries.TEST_INSTANCE).optionalFieldOf("test").forGetter(TestInstanceBlockEntity$Data::test),
            Vec3i.CODEC.fieldOf("size").forGetter(TestInstanceBlockEntity$Data::size),
            Rotation.CODEC.fieldOf("rotation").forGetter(TestInstanceBlockEntity$Data::rotation),
            Codec.BOOL.fieldOf("ignore_entities").forGetter(TestInstanceBlockEntity$Data::ignoreEntities),
            TestInstanceBlockEntity$Status.CODEC.fieldOf("status").forGetter(TestInstanceBlockEntity$Data::status),
            ComponentSerialization.CODEC.optionalFieldOf("error_message").forGetter(TestInstanceBlockEntity$Data::errorMessage)
         )
         .apply(i, TestInstanceBlockEntity$Data::new)
   );
   public static final StreamCodec<RegistryFriendlyByteBuf, TestInstanceBlockEntity$Data> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.TEST_INSTANCE)),
      TestInstanceBlockEntity$Data::test,
      Vec3i.STREAM_CODEC,
      TestInstanceBlockEntity$Data::size,
      Rotation.STREAM_CODEC,
      TestInstanceBlockEntity$Data::rotation,
      ByteBufCodecs.BOOL,
      TestInstanceBlockEntity$Data::ignoreEntities,
      TestInstanceBlockEntity$Status.STREAM_CODEC,
      TestInstanceBlockEntity$Data::status,
      ByteBufCodecs.optional(ComponentSerialization.STREAM_CODEC),
      TestInstanceBlockEntity$Data::errorMessage,
      TestInstanceBlockEntity$Data::new
   );

   public TestInstanceBlockEntity$Data withSize(final Vec3i size) {
      return new TestInstanceBlockEntity$Data(this.test, size, this.rotation, this.ignoreEntities, this.status, this.errorMessage);
   }

   public TestInstanceBlockEntity$Data withStatus(final TestInstanceBlockEntity$Status status) {
      return new TestInstanceBlockEntity$Data(this.test, this.size, this.rotation, this.ignoreEntities, status, Optional.empty());
   }

   public TestInstanceBlockEntity$Data withError(final Component error) {
      return new TestInstanceBlockEntity$Data(
         this.test, this.size, this.rotation, this.ignoreEntities, TestInstanceBlockEntity$Status.FINISHED, Optional.of(error)
      );
   }
}
