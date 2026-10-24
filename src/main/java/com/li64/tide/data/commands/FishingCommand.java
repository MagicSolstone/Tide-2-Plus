package com.li64.tide.data.commands;

import com.li64.tide.Tide;
import com.li64.tide.data.fishing.FishData;
import com.li64.tide.data.fishing.selector.FishingEntry;
import com.li64.tide.registries.entities.misc.fishing.HookAccessor;
import com.li64.tide.registries.entities.misc.fishing.TideFishingHook;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class FishingCommand {
    public FishingCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register(Commands.literal("fishing")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("test")
                        .then(Commands.literal("loot").executes(ctx -> test(ctx.getSource(), TestType.LOOT)))
                        .then(Commands.literal("crate").executes(ctx -> test(ctx.getSource(), TestType.CRATES)))
                        .then(Commands.literal("fish").executes(ctx -> test(ctx.getSource(), TestType.FISH))))
                .then(Commands.literal("guide").executes(ctx -> {
                    String out = "tackleguide " + guide(ctx.getSource().getServer());
                    ctx.getSource().sendSuccess(() -> Component.literal(out), false);
                    return 1;
                }))
                .then(Commands.literal("describe")
                        .then(Commands.argument("item", net.minecraft.commands.arguments.item.ItemArgument.item(buildContext))
                                .executes(ctx -> describe(ctx.getSource(),
                                        net.minecraft.commands.arguments.item.ItemArgument.getItem(ctx, "item").createItemStack(1))))));
    }

    /**
     * An item's tooltip (everything under its name), for Solstone's item picker. One message so RCON keeps the lines
     * together: "tooltip <id>", then a line each as "RRGGBB<tab>text" in the line's main colour.
     */
    private int describe(CommandSourceStack source, net.minecraft.world.item.ItemStack stack) {
        String out = tooltip(stack, source.getServer().registryAccess());
        source.sendSuccess(() -> Component.literal(out), false);
        return (int) out.chars().filter(ch -> ch == '\n').count();
    }

    /**
     * Every piece of tackle as one line of JSON, for Solstone's tackle guide: name, kind, tooltip lines (with colours),
     * the places and times its perks work, its rig, and how it's made (the recipe the journal shows, or where it's found).
     * Plus the rigs: name, where they work, both bonuses and their pieces.
     */
    public static String guide(net.minecraft.server.MinecraftServer server) {
        var json = new com.google.gson.JsonObject();
        var rigs = new com.google.gson.JsonArray();
        for (var set : com.li64.tide.data.rods.RigSets.SETS) {
            var r = new com.google.gson.JsonObject();
            r.addProperty("name", set.name());
            r.addProperty("when", set.when().name());
            r.addProperty("when_text", set.when().text);
            r.addProperty("two", set.twoText());
            r.addProperty("full", set.bonusText());
            var members = new com.google.gson.JsonArray();
            set.members().stream().sorted().forEach(m -> members.add("tide:" + m));
            r.add("members", members);
            rigs.add(r);
        }
        json.add("rigs", rigs);
        var whens = new com.google.gson.JsonObject();
        for (var w : com.li64.tide.data.rods.RodEffects.When.values()) whens.addProperty(w.name(), w.text);
        json.add("whens", whens);
        var items = new com.google.gson.JsonArray();
        for (net.minecraft.world.item.Item item : net.minecraft.core.registries.BuiltInRegistries.ITEM) {
            var cat = com.li64.tide.data.tackle.TackleLog.category(item);
            if (cat == null) continue;
            var id = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item);
            var stack = new net.minecraft.world.item.ItemStack(item);
            var o = new com.google.gson.JsonObject();
            o.addProperty("id", id.toString());
            o.addProperty("name", stack.getHoverName().getString());
            o.addProperty("category", cat.label);
            var lines = new com.google.gson.JsonArray();
            String tip = tooltip(stack, server.registryAccess());
            for (String row : tip.substring(tip.indexOf('\n') + 1).split("\n")) {
                int tab = row.indexOf('\t');
                if (tab < 0) continue;
                var l = new com.google.gson.JsonArray();
                l.add("#" + row.substring(0, tab));
                l.add(row.substring(tab + 1));
                lines.add(l);
            }
            o.add("lines", lines);
            var when = new com.google.gson.JsonArray();
            com.li64.tide.data.rods.RodEffects.situational(stack).stream().map(p -> p.when().name()).distinct().forEach(when::add);
            o.add("when", when);
            com.li64.tide.data.rods.RigSets.SETS.stream().filter(set -> set.members().contains(id.getPath())).findFirst()
                    .ifPresent(set -> o.addProperty("rig", set.name()));
            try {
                var r = com.li64.tide.data.tackle.TackleLog.recipeFor(server, id.toString());
                var rec = new com.google.gson.JsonObject();
                rec.addProperty("kind", r.kind());
                rec.addProperty("width", r.width());
                rec.addProperty("height", r.height());
                var slots = new com.google.gson.JsonArray();
                for (var slot : r.slots()) {
                    var a = new com.google.gson.JsonArray();
                    slot.forEach(a::add);
                    slots.add(a);
                }
                rec.add("slots", slots);
                rec.addProperty("note", r.note());
                o.add("recipe", rec);
            } catch (Throwable ignored) {
            }
            items.add(o);
        }
        json.add("items", items);
        return json.toString();
    }

    public static String tooltip(net.minecraft.world.item.ItemStack stack, net.minecraft.core.HolderLookup.Provider registries) {
        java.util.List<Component> lines = new java.util.ArrayList<>(stack.getTooltipLines(
                net.minecraft.world.item.Item.TooltipContext.of(registries), null,
                net.minecraft.world.item.TooltipFlag.NORMAL));
        if (!lines.isEmpty()) lines.removeFirst();                      // the name
        com.li64.tide.data.item.TideTooltips.add(stack, lines, false);
        StringBuilder out = new StringBuilder("tooltip " + net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()));
        java.util.Set<String> seen = new java.util.HashSet<>();
        for (Component c : lines) {
            String text = c.getString();
            if (text.isBlank() || !seen.add(text)) continue;             // singleplayer also runs the client's tooltip hook
            int[] colour = {0xAAAAAA};
            c.visit((style, part) -> {
                if (part.isBlank()) return java.util.Optional.empty();
                if (style.getColor() != null) colour[0] = style.getColor().getValue();
                return java.util.Optional.of(true);
            }, net.minecraft.network.chat.Style.EMPTY);
            out.append('\n').append(String.format("%06X", colour[0] & 0xFFFFFF)).append('\t').append(text.replace('\n', ' '));
        }
        return out.toString();
    }

    private int test(CommandSourceStack source, TestType type) {
        ServerPlayer player = source.getPlayer();
        if (player == null) return 0;
        TideFishingHook hook = HookAccessor.getHook(player);
        if (hook == null) {
            source.sendFailure(Component.translatable("commands.fishing.no_hook"));
            return 0;
        }

        Map<FishingEntry, Double> results = Tide.FISHING_MANAGER.test(hook.getContext(), type);
        if (results == null || results.isEmpty()) {
            source.sendFailure(Component.translatable("commands.fishing.nothing_found"));
            return 0;
        }

        double total = results.values().stream().mapToDouble(d -> d).sum();
        List<Map.Entry<FishingEntry, Double>> sortedResults = results.entrySet().stream()
                .sorted(Comparator.comparing(entry -> -entry.getValue())).toList();

        MutableComponent formattedResults = Component.translatable("commands.fishing.results")
                .withStyle(ChatFormatting.GOLD);
        sortedResults.forEach(entry -> {
            formattedResults.append(Component.literal("\n"));
            formattedResults.append(entry.getKey().getTestKey().withStyle(ChatFormatting.WHITE));
            formattedResults.append(Component.literal(" - ").withStyle(ChatFormatting.WHITE));
            String percent = String.format("%.1f", (entry.getValue() / total) * 100.0) + "%";
            formattedResults.append(Component.literal(percent).withStyle(ChatFormatting.AQUA));
        });

        source.sendSuccess(() -> formattedResults, true);
        return 1;
    }
}