package net.minecraft.world.level.block;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.Map;
import net.minecraft.util.StringRepresentable;

public interface SkullBlock$Type extends StringRepresentable {
   Map<String, SkullBlock$Type> TYPES = new Object2ObjectArrayMap();
   Codec<SkullBlock$Type> CODEC = Codec.stringResolver(StringRepresentable::getSerializedName, TYPES::get);
}
