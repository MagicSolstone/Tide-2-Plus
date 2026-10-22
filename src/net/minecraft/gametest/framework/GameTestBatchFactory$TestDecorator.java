package net.minecraft.gametest.framework;

import java.util.stream.Stream;
import net.minecraft.core.Holder$Reference;
import net.minecraft.server.level.ServerLevel;

@FunctionalInterface
public interface GameTestBatchFactory$TestDecorator {
   Stream<GameTestInfo> decorate(Holder$Reference<GameTestInstance> test, ServerLevel level);
}
