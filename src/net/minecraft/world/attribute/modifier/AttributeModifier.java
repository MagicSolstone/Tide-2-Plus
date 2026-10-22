package net.minecraft.world.attribute.modifier;

import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Map;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.LerpFunction;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

public interface AttributeModifier<Subject, Argument> {
   Map<AttributeModifier$OperationId, AttributeModifier<Boolean, ?>> BOOLEAN_LIBRARY = Map.of(
      AttributeModifier$OperationId.AND,
      BooleanModifier.AND,
      AttributeModifier$OperationId.NAND,
      BooleanModifier.NAND,
      AttributeModifier$OperationId.OR,
      BooleanModifier.OR,
      AttributeModifier$OperationId.NOR,
      BooleanModifier.NOR,
      AttributeModifier$OperationId.XOR,
      BooleanModifier.XOR,
      AttributeModifier$OperationId.XNOR,
      BooleanModifier.XNOR
   );
   Map<AttributeModifier$OperationId, AttributeModifier<Float, ?>> FLOAT_LIBRARY = Map.of(
      AttributeModifier$OperationId.ALPHA_BLEND,
      FloatModifier.ALPHA_BLEND,
      AttributeModifier$OperationId.ADD,
      FloatModifier.ADD,
      AttributeModifier$OperationId.SUBTRACT,
      FloatModifier.SUBTRACT,
      AttributeModifier$OperationId.MULTIPLY,
      FloatModifier.MULTIPLY,
      AttributeModifier$OperationId.MINIMUM,
      FloatModifier.MINIMUM,
      AttributeModifier$OperationId.MAXIMUM,
      FloatModifier.MAXIMUM
   );
   Map<AttributeModifier$OperationId, AttributeModifier<Vector3fc, ?>> RGB_COLOR_LIBRARY = Map.of(
      AttributeModifier$OperationId.ALPHA_BLEND,
      ColorModifier.ALPHA_BLEND_RGB,
      AttributeModifier$OperationId.ADD,
      ColorModifier.ADD_RGB,
      AttributeModifier$OperationId.SUBTRACT,
      ColorModifier.SUBTRACT_RGB,
      AttributeModifier$OperationId.MULTIPLY,
      ColorModifier.MULTIPLY_RGB,
      AttributeModifier$OperationId.BLEND_TO_GRAY,
      ColorModifier.BLEND_TO_GRAY_RGB
   );
   Map<AttributeModifier$OperationId, AttributeModifier<Vector4fc, ?>> ARGB_COLOR_LIBRARY = Map.of(
      AttributeModifier$OperationId.ALPHA_BLEND,
      ColorModifier.ALPHA_BLEND_ARGB,
      AttributeModifier$OperationId.ADD,
      ColorModifier.ADD_ARGB,
      AttributeModifier$OperationId.SUBTRACT,
      ColorModifier.SUBTRACT_ARGB,
      AttributeModifier$OperationId.MULTIPLY,
      ColorModifier.MULTIPLY_ARGB,
      AttributeModifier$OperationId.BLEND_TO_GRAY,
      ColorModifier.BLEND_TO_GRAY_ARGB
   );
   Map<AttributeModifier$OperationId, AttributeModifier<Integer, ?>> INTEGER_LIBRARY = Map.of(
      AttributeModifier$OperationId.ADD,
      IntegerModifier.ADD,
      AttributeModifier$OperationId.SUBTRACT,
      IntegerModifier.SUBTRACT,
      AttributeModifier$OperationId.MULTIPLY,
      IntegerModifier.MULTIPLY,
      AttributeModifier$OperationId.MINIMUM,
      IntegerModifier.MINIMUM,
      AttributeModifier$OperationId.MAXIMUM,
      IntegerModifier.MAXIMUM
   );
   Map<AttributeModifier$OperationId, AttributeModifier<MobSpawnSettings, ?>> MOB_SPAWN_SETTINGS_LIBRARY = Map.of(
      AttributeModifier$OperationId.OVERLAY, MobSpawnSettingsModifier.overlay()
   );

   static <Value> AttributeModifier<Value, Value> override() {
      return AttributeModifier$OverrideModifier.INSTANCE;
   }

   static <Element> Map<AttributeModifier$OperationId, AttributeModifier<List<Element>, ?>> listLibrary() {
      return Map.of(AttributeModifier$OperationId.APPEND, ListModifier.append());
   }

   Subject apply(Subject subject, Argument argument);

   Codec<Argument> argumentCodec(EnvironmentAttribute<Subject> attribute);

   LerpFunction<Argument> argumentKeyframeLerp(EnvironmentAttribute<Subject> attribute);
}
