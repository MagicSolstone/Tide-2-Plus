package com.li64.tide.data;

import com.li64.tide.data.triggers.TideSimpleTrigger;
import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;

import com.li64.tide.Tide;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.advancements.triggers.Criterion;
import java.util.Optional;

import java.util.HashMap;
import java.util.function.Function;

public class TideCriteriaTriggers {
    public static final HashMap<String, CriterionTrigger<?>> CRITERIA_TRIGGERS = new HashMap<>();

    public static TideSimpleTrigger PULLED_CRATE = (TideSimpleTrigger) register("pull_crate", TideSimpleTrigger::new);
    public static TideSimpleTrigger FISHED_IN_LAVA = (TideSimpleTrigger) register("fish_in_lava", TideSimpleTrigger::new);
    public static TideSimpleTrigger FISHED_IN_VOID = (TideSimpleTrigger) register("fish_in_void", TideSimpleTrigger::new);
    public static TideSimpleTrigger RELEASED_PUPFISH = (TideSimpleTrigger) register("released_pupfish", TideSimpleTrigger::new);
    public static TideSimpleTrigger FROZE_COELACANTH = (TideSimpleTrigger) register("froze_coelacanth", TideSimpleTrigger::new);
    public static TideSimpleTrigger JOURNAL_HALFWAY_COMPLETE = (TideSimpleTrigger) register("journal_halfway_complete", TideSimpleTrigger::new);
    public static TideSimpleTrigger JOURNAL_COMPLETE = (TideSimpleTrigger) register("journal_complete", TideSimpleTrigger::new);

    public static <T extends SimpleCriterionTrigger.SimpleInstance>
            SimpleCriterionTrigger<T> register(String key, Function<String, SimpleCriterionTrigger<T>> triggerFunc) {
        SimpleCriterionTrigger<T> trigger = triggerFunc.apply(key);
        CRITERIA_TRIGGERS.put(key, trigger);
        return Tide.PLATFORM.register(BuiltInRegistries.TRIGGER_TYPES, Tide.resource(key), trigger);
    }

    public static void init() {}

    public static Criterion<TideSimpleTrigger.TriggerInstance> simpleInstance(TideSimpleTrigger trigger) {
        return trigger.createCriterion(new TideSimpleTrigger.TriggerInstance(Optional.empty()));
    }
}
