package net.minecraft.resources;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import java.io.Reader;
import java.util.Locale;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.nbt.Tag;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.StrictJsonParser;

public record RegistryLoadTask$PendingRegistration<T>(ResourceKey<T> key, Either<T, Exception> value, RegistrationInfo registrationInfo) {
   public static <T> Either<T, Exception> loadFromResource(
      final Decoder<T> elementDecoder, final RegistryOps<JsonElement> ops, final ResourceKey<T> elementKey, final Resource thunk
   ) {
      try {
         Either var6;
         try (Reader reader = thunk.openAsReader()) {
            JsonElement json = StrictJsonParser.parse(reader);
            var6 = Either.left(elementDecoder.parse(ops, json).getOrThrow());
         }

         return var6;
      } catch (Exception var9) {
         return Either.right(
            new IllegalStateException(String.format(Locale.ROOT, "Failed to parse %s from pack %s", elementKey.identifier(), thunk.sourcePackId()), var9)
         );
      }
   }

   public static <T> Either<T, Exception> findAndLoadFromResource(
      final Decoder<T> elementDecoder,
      final RegistryOps<JsonElement> ops,
      final ResourceKey<T> elementKey,
      final FileToIdConverter converter,
      final ResourceProvider resourceProvider
   ) {
      Identifier resourceId = converter.idToFile(elementKey.identifier());
      return resourceProvider.getResource(resourceId)
         .map(resource -> loadFromResource(elementDecoder, ops, elementKey, resource))
         .orElseGet(
            () -> Either.right(
               new IllegalStateException(String.format(Locale.ROOT, "Failed to find resource %s for element %s", resourceId, elementKey.identifier()))
            )
         );
   }

   public static <T> Either<T, Exception> loadFromNetwork(
      final Decoder<T> elementDecoder, final RegistryOps<Tag> ops, final ResourceKey<T> elementKey, final Tag contents
   ) {
      try {
         DataResult<T> parseResult = elementDecoder.parse(ops, contents);
         return Either.left(parseResult.getOrThrow());
      } catch (Exception var5) {
         return Either.right(
            new IllegalStateException(String.format(Locale.ROOT, "Failed to parse value %s for key %s from server", contents, elementKey.identifier()), var5)
         );
      }
   }
}
