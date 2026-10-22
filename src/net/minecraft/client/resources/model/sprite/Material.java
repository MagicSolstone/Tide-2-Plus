package net.minecraft.client.resources.model.sprite;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record Material(Identifier sprite, boolean forceTranslucent) {
   private static final Codec<Material> SIMPLE_CODEC = Identifier.CODEC.xmap(Material::new, Material::sprite);
   private static final Codec<Material> FULL_CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Identifier.CODEC.fieldOf("sprite").forGetter(Material::sprite),
            Codec.BOOL.optionalFieldOf("force_translucent", false).forGetter(Material::forceTranslucent)
         )
         .apply(i, Material::new)
   );
   public static final Codec<Material> CODEC = Codec.either(SIMPLE_CODEC, FULL_CODEC)
      .xmap(Either::unwrap, material -> material.forceTranslucent ? Either.right(material) : Either.left(material));

   public Material(final Identifier sprite) {
      this(sprite, false);
   }

   public Material withSuffix(final String suffix) {
      return new Material(this.sprite.withSuffix(suffix), this.forceTranslucent);
   }

   public Material withForceTranslucent(final boolean forceTranslucent) {
      return new Material(this.sprite, forceTranslucent);
   }
}
