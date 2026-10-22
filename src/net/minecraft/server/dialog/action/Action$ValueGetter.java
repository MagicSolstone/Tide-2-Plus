package net.minecraft.server.dialog.action;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.nbt.Tag;

public interface Action$ValueGetter {
   String asTemplateSubstitution();

   Tag asTag();

   static Map<String, String> getAsTemplateSubstitutions(final Map<String, Action$ValueGetter> parameters) {
      return Maps.transformValues(parameters, Action$ValueGetter::asTemplateSubstitution);
   }

   static Action$ValueGetter of(final String value) {
      return new Action$ValueGetter$1(value);
   }

   static Action$ValueGetter of(final Supplier<String> value) {
      return new Action$ValueGetter$2(value);
   }
}
