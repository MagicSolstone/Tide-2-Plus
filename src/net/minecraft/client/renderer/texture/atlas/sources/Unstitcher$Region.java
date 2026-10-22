package net.minecraft.client.renderer.texture.atlas.sources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public record Unstitcher$Region(Identifier sprite, double x, double y, double width, double height) {
   public static final Codec<Unstitcher$Region> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Identifier.CODEC.fieldOf("sprite").forGetter(Unstitcher$Region::sprite),
            Codec.DOUBLE.fieldOf("x").forGetter(Unstitcher$Region::x),
            Codec.DOUBLE.fieldOf("y").forGetter(Unstitcher$Region::y),
            Codec.DOUBLE.fieldOf("width").forGetter(Unstitcher$Region::width),
            Codec.DOUBLE.fieldOf("height").forGetter(Unstitcher$Region::height)
         )
         .apply(i, Unstitcher$Region::new)
   );
}
