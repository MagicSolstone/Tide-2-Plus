package net.minecraft.world.attribute.modifier;

import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.LerpFunction;

public interface ListModifier<Element> extends AttributeModifier<List<Element>, List<Element>> {
   static <Element> ListModifier<Element> append() {
      return (ListModifier<Element>)ListModifier$Append.INSTANCE;
   }

   @Override
   default Codec<List<Element>> argumentCodec(final EnvironmentAttribute<List<Element>> attribute) {
      return attribute.valueCodec();
   }

   @Override
   default LerpFunction<List<Element>> argumentKeyframeLerp(final EnvironmentAttribute<List<Element>> attribute) {
      return attribute.type().keyframeLerp();
   }
}
