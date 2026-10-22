package net.minecraft.network.chat;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.contents.KeybindContents;
import net.minecraft.network.chat.contents.NbtContents;
import net.minecraft.network.chat.contents.ObjectContents;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.ScoreContents;
import net.minecraft.network.chat.contents.SelectorContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.ExtraCodecs$LateBoundIdMapper;

public class ComponentSerialization {
   public static final Codec<Component> CODEC = Codec.recursive("Component", ComponentSerialization::createCodec);
   public static final StreamCodec<RegistryFriendlyByteBuf, Component> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
   public static final StreamCodec<RegistryFriendlyByteBuf, Optional<Component>> OPTIONAL_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs::optional);
   public static final StreamCodec<RegistryFriendlyByteBuf, Component> TRUSTED_STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistriesTrusted(CODEC);
   public static final StreamCodec<RegistryFriendlyByteBuf, Optional<Component>> TRUSTED_OPTIONAL_STREAM_CODEC = TRUSTED_STREAM_CODEC.apply(
      ByteBufCodecs::optional
   );
   public static final StreamCodec<ByteBuf, Component> TRUSTED_CONTEXT_FREE_STREAM_CODEC = ByteBufCodecs.fromCodecTrusted(CODEC);

   public static Codec<Component> flatRestrictedCodec(final int maxFlatSize) {
      return new ComponentSerialization$1(maxFlatSize);
   }

   private static MutableComponent createFromList(final List<Component> list) {
      MutableComponent result = list.get(0).copy();

      for (int i = 1; i < list.size(); i++) {
         result.append(list.get(i));
      }

      return result;
   }

   public static <T> MapCodec<T> createLegacyComponentMatcher(
      final ExtraCodecs$LateBoundIdMapper<String, MapCodec<? extends T>> types,
      final Function<T, MapCodec<? extends T>> codecGetter,
      final String typeFieldName
   ) {
      MapCodec<T> compactCodec = new ComponentSerialization$FuzzyCodec<>(types.values(), codecGetter);
      MapCodec<T> discriminatorCodec = types.codec(Codec.STRING).dispatchMap(typeFieldName, codecGetter, c -> c);
      MapCodec<T> contentsCodec = new ComponentSerialization$StrictEither<>(typeFieldName, discriminatorCodec, compactCodec);
      return ExtraCodecs.orCompressed(contentsCodec, discriminatorCodec);
   }

   private static Codec<Component> createCodec(final Codec<Component> topSerializer) {
      ExtraCodecs$LateBoundIdMapper<String, MapCodec<? extends ComponentContents>> contentTypes = new ExtraCodecs$LateBoundIdMapper<>();
      bootstrap(contentTypes);
      MapCodec<ComponentContents> compressedContentsCodec = createLegacyComponentMatcher(contentTypes, ComponentContents::codec, "type");
      Codec<Component> fullCodec = RecordCodecBuilder.create(
         i -> i.group(
               compressedContentsCodec.forGetter(Component::getContents),
               ExtraCodecs.nonEmptyList(topSerializer.listOf()).optionalFieldOf("extra", List.of()).forGetter(Component::getSiblings),
               Style$Serializer.MAP_CODEC.forGetter(Component::getStyle)
            )
            .apply(i, MutableComponent::new)
      );
      return Codec.either(Codec.either(Codec.STRING, ExtraCodecs.nonEmptyList(topSerializer.listOf())), fullCodec)
         .xmap(
            specialOrComponent -> (Component)specialOrComponent.map(
               special -> (Component)special.map(Component::literal, ComponentSerialization::createFromList), c -> c
            ),
            component -> {
               String text = component.tryCollapseToString();
               return text != null ? Either.left(Either.left(text)) : Either.right(component);
            }
         );
   }

   private static void bootstrap(final ExtraCodecs$LateBoundIdMapper<String, MapCodec<? extends ComponentContents>> contentTypes) {
      contentTypes.put("text", PlainTextContents.MAP_CODEC);
      contentTypes.put("translatable", TranslatableContents.MAP_CODEC);
      contentTypes.put("keybind", KeybindContents.MAP_CODEC);
      contentTypes.put("score", ScoreContents.MAP_CODEC);
      contentTypes.put("selector", SelectorContents.MAP_CODEC);
      contentTypes.put("nbt", NbtContents.MAP_CODEC);
      contentTypes.put("object", ObjectContents.MAP_CODEC);
   }
}
