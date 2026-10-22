package net.minecraft.client.renderer.texture.atlas.sources;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSource$Output;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.ExtraCodecs;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public record Unstitcher(Identifier resource, List<Unstitcher$Region> regions, double xDivisor, double yDivisor) implements SpriteSource {
   private static final Logger LOGGER = LogUtils.getLogger();
   public static final MapCodec<Unstitcher> MAP_CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            Identifier.CODEC.fieldOf("resource").forGetter(Unstitcher::resource),
            ExtraCodecs.nonEmptyList(Unstitcher$Region.CODEC.listOf()).fieldOf("regions").forGetter(Unstitcher::regions),
            Codec.DOUBLE.optionalFieldOf("divisor_x", 1.0).forGetter(Unstitcher::xDivisor),
            Codec.DOUBLE.optionalFieldOf("divisor_y", 1.0).forGetter(Unstitcher::yDivisor)
         )
         .apply(i, Unstitcher::new)
   );

   @Override
   public void run(final ResourceManager resourceManager, final SpriteSource$Output output) {
      Identifier resourceId = TEXTURE_ID_CONVERTER.idToFile(this.resource);
      Optional<Resource> resource = resourceManager.getResource(resourceId);
      if (resource.isPresent()) {
         LazyLoadedImage image = new LazyLoadedImage(resourceId, resource.get(), this.regions.size());

         for (Unstitcher$Region region : this.regions) {
            output.add(region.sprite, new Unstitcher$RegionInstance(image, region, this.xDivisor, this.yDivisor));
         }
      } else {
         LOGGER.warn("Missing sprite: {}", resourceId);
      }
   }

   @Override
   public MapCodec<Unstitcher> codec() {
      return MAP_CODEC;
   }
}
