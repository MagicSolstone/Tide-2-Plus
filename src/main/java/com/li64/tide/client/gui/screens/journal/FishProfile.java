package com.li64.tide.client.gui.screens.journal;

import com.li64.tide.client.Gui26;
import net.minecraft.client.renderer.RenderPipelines;
import com.li64.tide.Tide;
import com.li64.tide.client.gui.screens.journal.components.*;
import com.li64.tide.compat.seasons.SeasonsCompat;
import com.li64.tide.data.fishing.FishData;
import com.li64.tide.data.fishing.conditions.types.*;
import com.li64.tide.data.fishing.modifiers.types.TemperatureModifier;
import com.li64.tide.data.journal.FishRarity;
import com.li64.tide.data.player.FishStats;
import com.li64.tide.data.player.TidePlayerData;
import com.li64.tide.util.TideUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FishProfile implements Renderable {
    private static final Identifier BORDERS = Tide.resource("textures/gui/journal/profile_borders.png");
    private static final Identifier STAR = Tide.resource("textures/gui/journal/star.png");

    private static final int BG_WIDTH = FishingJournal.BG_WIDTH;
    private static final int BG_HEIGHT = FishingJournal.BG_HEIGHT;
    private static final int TEXT_COLOR = FishingJournal.TEXT_COLOR;

    private final ItemStack fish;
    private final FishData data;
    private final List<ProfileComponent> profileComponents;
    private final Font font;
    private final Component description;
    private final FishRarity rarity;
    private final Component rarityPrefix;

    public FishProfile(ItemStack fish, FishingJournal journal) {
        this.font = journal.getFont();
        this.fish = fish;
        this.data = FishData.getOrThrow(fish);

        Component description = Component.translatable(data.profile().description().orElse("journal.description.missing"));
        if (description.getString().contains("journal.description") || description.getString().isBlank()) description = Component.translatable("journal.description.missing");
        this.description = description;
        this.rarity = data.profile().rarity();
        Style defaultStyle = Component.literal("").getStyle();
        this.rarityPrefix = Component.translatable("journal.rarity.title").append(": ")
                .append(Component.translatable("journal.rarity." + rarity.getKey())
                        .withStyle(defaultStyle.withColor(rarity.getColor()))).append(" (");
        this.profileComponents = buildComponentsWithStats(fish, data);
    }

    public static ArrayList<ProfileComponent> buildComponentsWithStats(ItemStack fish, FishData data) {
        ArrayList<ProfileComponent> builder = buildComponents(data);
        if (!builder.isEmpty() && !(builder.get(builder.size() - 1) instanceof HorizontalLineComponent))
            builder.add(new HorizontalLineComponent(false));

        // add fish player stats info area
        FishStats stats = TidePlayerData.CLIENT_DATA.getDataFor(fish.getItem())
                .flatMap(d -> d.stats).orElse(new FishStats());
        builder.add(new StatsComponent(stats, net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(fish.getItem()).toString()));

        return builder;
    }

    public static ArrayList<ProfileComponent> buildComponents(FishData data) {
        ArrayList<ProfileComponent> builder = new ArrayList<>(5);

        // add location info area
        data.profile().location().ifPresent(location -> {
            builder.add(new LocationComponent(Component.translatable(location)));
        });
        // its favorite lure (twice as likely to bite with it)
        String lure = com.li64.tide.data.rods.FavoriteLures.lureFor(data.fish().value());
        if (lure != null) net.minecraft.core.registries.BuiltInRegistries.ITEM.getOptional(net.minecraft.resources.Identifier.parse(lure))
                .ifPresent(item -> builder.add(new LocationComponent(Component.literal("Favorite lure: ")
                        .append(Component.translatable(item.getDescriptionId())), true)));

        // add luck info area
        Optional<LuckCondition> luckCondition = data.conditions().stream()
                .filter(cond -> cond instanceof LuckCondition)
                .findFirst().map(cond -> (LuckCondition) cond);
        luckCondition.ifPresent(condition -> builder.add(
                new LuckComponent(condition.getMinLuck(), condition.getMaxLuck())));

        // first line break (if applicable)
        if (!builder.isEmpty()) builder.add(new HorizontalLineComponent(false));

        // add dimensions info area
        Optional<DimensionsCondition> dimensionsCondition = data.conditions().stream()
                .filter(cond -> cond instanceof DimensionsCondition)
                .findFirst().map(cond -> (DimensionsCondition) cond);
        dimensionsCondition.ifPresent(condition -> {
            if (!DimensionsComponent.shouldCreate(condition, data.conditions())) return;
            builder.add(new DimensionsComponent(condition.getDimensions()));
        });

        // add seasons info area
        if (SeasonsCompat.isActive()) {
            Optional<SeasonsCondition> seasonsCondition = data.conditions().stream()
                    .filter(cond -> cond instanceof SeasonsCondition)
                    .findFirst().map(cond -> (SeasonsCondition) cond);
            seasonsCondition.ifPresent(condition ->
                    builder.add(new SeasonsComponent(condition.getSeasons())));
        }

        // add temperature info area
        Optional<TemperatureModifier> tempMod = data.modifiers().stream()
                .filter(mod -> mod instanceof TemperatureModifier)
                .findFirst().map(mod -> (TemperatureModifier) mod);
        if (tempMod.isPresent()) {
            TemperatureModifier mod = tempMod.get();
            float minTemp = Math.max(mod.getPreferred() - mod.getTolerance() * 0.95f, -1f);
            float maxTemp = Math.max(mod.getPreferred() + mod.getTolerance() * 0.95f, -1f);
            builder.add(new TemperatureComponent(minTemp, maxTemp));
        }

        // add time info area
        Optional<TimeOfDayCondition> timeCondition = data.conditions().stream()
                .filter(cond -> cond instanceof TimeOfDayCondition)
                .findFirst().map(cond -> (TimeOfDayCondition) cond);
        timeCondition.ifPresent(condition ->
                builder.add(new TimeComponent(condition.getRanges())));

        // add depth info area
        Optional<DepthRangeHolder> depthRange = data.conditions().stream()
                .filter(cond -> cond instanceof DepthRangeHolder)
                .findFirst().map(cond -> (DepthRangeHolder) cond);
        if (depthRange.isPresent()) {
            Optional<DimensionsCondition> dimensionCondition = data.conditions().stream()
                    .filter(cond -> cond instanceof DimensionsCondition)
                    .findFirst().map(cond -> (DimensionsCondition) cond);
            // only show depth bar if fish is from overworld
            if (dimensionCondition.map(DimensionsCondition::isOverworldOnly).orElse(false)) {
                DepthRangeHolder range = depthRange.get();
                float min = range.hasLowerBound() ? DepthComponent.depthToFloat(range.getMinY()) : 0f;
                float max = range.hasUpperBound() ? DepthComponent.depthToFloat(range.getMaxY()) : 1f;
                // only show depth bar if fish isn't from surface
                if (min < 0.7f || max != 1) builder.add(new DepthComponent(min, max));
            }
        }

        // add moon phase info area
        Optional<MoonPhaseCondition> moonPhaseCondition = data.conditions().stream()
                .filter(cond -> cond instanceof MoonPhaseCondition)
                .findFirst().map(cond -> (MoonPhaseCondition) cond);
        moonPhaseCondition.ifPresent(condition ->
                builder.add(new MoonPhaseComponent(condition.getPhases())));

        // add weather info area
        Optional<WeatherCondition> weatherCondition = data.conditions().stream()
                .filter(cond -> cond instanceof WeatherCondition)
                .findFirst().map(cond -> (WeatherCondition) cond);
        weatherCondition.ifPresent(condition ->
                builder.add(new WeatherComponent(condition.getWeatherTypes())));

        return builder;
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        // render background
        int tlX = (graphics.guiWidth() - BG_WIDTH) / 2;
        int tlY = (graphics.guiHeight() - BG_HEIGHT) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, BORDERS, tlX, tlY, 0, 0, BG_WIDTH, BG_HEIGHT, BG_WIDTH, BG_HEIGHT);

        // render title
        int titleX = tlX + 110;
        int titleY = tlY + 30;
        Component title = TideUtils.removeRawTextInName(fish.getHoverName());
        int titleWidth = font.width(title);
        int underlineWidth = titleWidth + 6;
        Gui26.text(graphics, font, title, titleX - titleWidth / 2, titleY, TEXT_COLOR, false);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FishingJournal.LINE_BOTTOM, titleX - underlineWidth / 2,
                titleY + font.lineHeight + 2, underlineWidth, 2);

        // get fish sizing and offsets
        boolean isLarge = data.profile().altSprite().isPresent();
        Identifier alternateTexture = data.profile().altSprite().orElse(null);
        int fishSize = isLarge ? data.profile().altSpriteSize().orElse(16) : 16;
        int itemCenterX = tlX + 109;
        int itemCenterY = tlY + 100;
        final int shadowOffset = 2;
        final float scale = 2f;

        // scale around the fish's center
        graphics.pose().pushMatrix();
        graphics.pose().translate(itemCenterX, itemCenterY);
        graphics.pose().scale(scale, scale);
        graphics.pose().translate(-itemCenterX, -itemCenterY);

        // offset for shadow rendering
        graphics.pose().pushMatrix();
        graphics.pose().translate(shadowOffset / scale, shadowOffset / scale);

        // render fish silhouette (its shadow)
        if (!isLarge) FishingJournal.renderItemSilhouette(graphics, fish,
                itemCenterX - fishSize / 2, itemCenterY - fishSize / 2, FishingJournal.SHADOW_COLOR);
        else FishingJournal.renderTextureSilhouette(graphics, alternateTexture,
                itemCenterX - fishSize / 2, itemCenterY - fishSize / 2, fishSize, fishSize, FishingJournal.SHADOW_COLOR);

        // pop shadow offset
        graphics.pose().popMatrix();

        if (!isLarge) graphics.item(fish, itemCenterX - fishSize / 2, itemCenterY - fishSize / 2);
        else graphics.blit(RenderPipelines.GUI_TEXTURED, alternateTexture,
                itemCenterX - fishSize / 2, itemCenterY - fishSize / 2,
                0, 0, fishSize, fishSize, fishSize, fishSize);

        // pop fish scaling
        graphics.pose().popMatrix();

        // render rarity text
        int stars = rarity.getNumStars();
        int rarityX = tlX + 40;
        int rarityY = tlY + 167;
        Gui26.text(graphics, font, rarityPrefix, rarityX, rarityY, TEXT_COLOR, false);
        for (int i = 0; i < stars; i++) graphics.blit(RenderPipelines.GUI_TEXTURED, STAR,
                rarityX + font.width(rarityPrefix) + i * 8, rarityY, 0, 0,
                7, 6, 7, 6);
        Gui26.text(graphics, font, ")", rarityX + font.width(rarityPrefix) + stars * 8, rarityY, TEXT_COLOR, false);

        // render description
        int descX = tlX + 38;
        int descY = tlY + 207;
        List<FormattedCharSequence> descriptionLines = font.split(this.description, 156);
        descY -= (descriptionLines.size() * font.lineHeight) / 2;
        for (int i = 0; i < descriptionLines.size(); i++) {
            Gui26.text(graphics, font, descriptionLines.get(i), descX,
                    descY + i * font.lineHeight, TEXT_COLOR, false);
        }

        // render profile components
        int padding = 4;
        int cursorY = padding + 8;
        for (ProfileComponent area : profileComponents) {
            area.render(graphics, font, tlX + ProfileComponent.AREA_X,
                    tlY + ProfileComponent.AREA_Y + cursorY, mouseX, mouseY, partialTick);
            cursorY += area.getRequiredHeight() + padding;
        }
    }
}