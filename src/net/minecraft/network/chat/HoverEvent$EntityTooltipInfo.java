package net.minecraft.network.chat;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import org.jspecify.annotations.Nullable;

public class HoverEvent$EntityTooltipInfo {
   public static final MapCodec<HoverEvent$EntityTooltipInfo> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("id").forGetter(o -> o.type),
            UUIDUtil.LENIENT_CODEC.fieldOf("uuid").forGetter(o -> o.uuid),
            ComponentSerialization.CODEC.optionalFieldOf("name").forGetter(o -> o.name)
         )
         .apply(i, HoverEvent$EntityTooltipInfo::new)
   );
   public final EntityType<?> type;
   public final UUID uuid;
   public final Optional<Component> name;
   @Nullable
   private List<Component> linesCache;

   public HoverEvent$EntityTooltipInfo(final EntityType<?> type, final UUID uuid, @Nullable final Component name) {
      this(type, uuid, Optional.ofNullable(name));
   }

   public HoverEvent$EntityTooltipInfo(final EntityType<?> type, final UUID uuid, final Optional<Component> name) {
      this.type = type;
      this.uuid = uuid;
      this.name = name;
   }

   public List<Component> getTooltipLines() {
      if (this.linesCache == null) {
         this.linesCache = new ArrayList<>();
         this.name.ifPresent(this.linesCache::add);
         this.linesCache.add(Component.translatable("gui.entity_tooltip.type", this.type.getDescription()));
         this.linesCache.add(Component.literal(this.uuid.toString()));
      }

      return this.linesCache;
   }

   @Override
   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         HoverEvent$EntityTooltipInfo that = (HoverEvent$EntityTooltipInfo)o;
         return this.type.equals(that.type) && this.uuid.equals(that.uuid) && this.name.equals(that.name);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int result = this.type.hashCode();
      result = 31 * result + this.uuid.hashCode();
      return 31 * result + this.name.hashCode();
   }
}
