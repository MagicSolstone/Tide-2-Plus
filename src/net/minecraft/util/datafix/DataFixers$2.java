package net.minecraft.util.datafix;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

class DataFixers$2 extends DataFix {
   DataFixers$2(Schema outputSchema, boolean changesType) {
      super(outputSchema, changesType);
   }

   protected TypeRewriteRule makeRule() {
      Type<?> entityType = this.getInputSchema().getType(References.ENTITY);
      OpticFinder<String> idFinder = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
      OpticFinder<?> customNameFinder = entityType.findField("CustomName");
      OpticFinder<Pair<String, String>> componentFinder = DSL.typeFinder(this.getInputSchema().getType(References.TEXT_COMPONENT));
      return this.fixTypeEverywhereTyped(
         "Command block minecart custom name fix",
         entityType,
         input -> {
            String id = input.getOptional(idFinder).orElse("");
            return !"minecraft:commandblock_minecart".equals(id)
               ? input
               : input.updateTyped(
                  customNameFinder,
                  customName -> customName.update(componentFinder, pair -> pair.mapSecond(LegacyComponentDataFixUtils::createTextComponentJson))
               );
         }
      );
   }
}
