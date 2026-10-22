package net.minecraft.world.attribute.modifier;

import net.minecraft.util.ARGB;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

public interface ColorModifier<Subject, Argument> extends AttributeModifier<Subject, Argument> {
   ColorModifier$ArgbModifier<Vector3fc> ALPHA_BLEND_RGB = ARGB::alphaBlend;
   ColorModifier$ArgbModifier<Vector4fc> ALPHA_BLEND_ARGB = ARGB::alphaBlend;
   ColorModifier$RgbModifier<Vector3fc> ADD_RGB = ARGB::addRgb;
   ColorModifier$RgbModifier<Vector3fc> SUBTRACT_RGB = ARGB::subtractRgb;
   ColorModifier$RgbModifier<Vector3fc> MULTIPLY_RGB = ARGB::multiply;
   ColorModifier$RgbModifier<Vector4fc> ADD_ARGB = ARGB::addRgb;
   ColorModifier$RgbModifier<Vector4fc> SUBTRACT_ARGB = ARGB::subtractRgb;
   ColorModifier$ArgbModifier<Vector4fc> MULTIPLY_ARGB = ARGB::multiply;
   ColorModifier<Vector3fc, ColorModifier$BlendToGray> BLEND_TO_GRAY_RGB = new ColorModifier$1();
   ColorModifier<Vector4fc, ColorModifier$BlendToGray> BLEND_TO_GRAY_ARGB = new ColorModifier$2();
}
