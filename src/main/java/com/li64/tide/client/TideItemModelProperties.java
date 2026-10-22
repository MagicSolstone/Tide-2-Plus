package com.li64.tide.client;

import com.li64.tide.Tide;
import com.li64.tide.mixin.RangeSelectItemModelPropertiesAccessor;

/** In 26.x item models choose their variant in assets/tide/items/*.json. Rods use vanilla's
 * "minecraft:fishing_rod/cast", the Starlight Bow vanilla's bow properties; only the satchel needs its own. */
public class TideItemModelProperties {
    public static void registerAll() {
        RangeSelectItemModelPropertiesAccessor.tide$idMapper().put(Tide.resource("satchel_state"), SatchelStateProperty.MAP_CODEC);
    }
}
