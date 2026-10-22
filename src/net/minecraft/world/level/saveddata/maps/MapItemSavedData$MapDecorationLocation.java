package net.minecraft.world.level.saveddata.maps;

import net.minecraft.core.Holder;

record MapItemSavedData$MapDecorationLocation(Holder<MapDecorationType> type, byte x, byte y, byte rot) {
}
