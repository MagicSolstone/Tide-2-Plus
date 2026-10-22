package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.Typed;
import org.jspecify.annotations.Nullable;

record MergeTerrainChunkStatusFix$FixedAndStatuses(Typed<?> fixed, String oldStatus, @Nullable String oldTargetStatus) {
}
