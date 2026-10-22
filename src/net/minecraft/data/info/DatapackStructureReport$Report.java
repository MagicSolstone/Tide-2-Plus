package net.minecraft.data.info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

record DatapackStructureReport$Report(
   Map<ResourceKey<? extends Registry<?>>, DatapackStructureReport$Entry> registries, Map<String, DatapackStructureReport$CustomPackEntry> others
) {
   public static final Codec<DatapackStructureReport$Report> CODEC = RecordCodecBuilder.create(
      i -> i.group(
            Codec.unboundedMap(DatapackStructureReport.REGISTRY_KEY_CODEC, DatapackStructureReport$Entry.CODEC)
               .fieldOf("registries")
               .forGetter(DatapackStructureReport$Report::registries),
            Codec.unboundedMap(Codec.STRING, DatapackStructureReport$CustomPackEntry.CODEC).fieldOf("others").forGetter(DatapackStructureReport$Report::others)
         )
         .apply(i, DatapackStructureReport$Report::new)
   );
}
