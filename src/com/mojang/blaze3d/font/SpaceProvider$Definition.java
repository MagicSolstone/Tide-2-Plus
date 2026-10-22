package com.mojang.blaze3d.font;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.providers.GlyphProviderDefinition;
import net.minecraft.client.gui.font.providers.GlyphProviderDefinition$Loader;
import net.minecraft.client.gui.font.providers.GlyphProviderDefinition$Reference;
import net.minecraft.client.gui.font.providers.GlyphProviderType;
import net.minecraft.util.ExtraCodecs;

@Environment(EnvType.CLIENT)
public record SpaceProvider$Definition(Map<Integer, Float> advances) implements GlyphProviderDefinition {
   public static final MapCodec<SpaceProvider$Definition> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(Codec.unboundedMap(ExtraCodecs.CODEPOINT, Codec.FLOAT).fieldOf("advances").forGetter(SpaceProvider$Definition::advances))
         .apply(i, SpaceProvider$Definition::new)
   );

   @Override
   public GlyphProviderType type() {
      return GlyphProviderType.SPACE;
   }

   @Override
   public Either<GlyphProviderDefinition$Loader, GlyphProviderDefinition$Reference> unpack() {
      GlyphProviderDefinition$Loader loader = resourceManager -> new SpaceProvider(this.advances);
      return Either.left(loader);
   }
}
