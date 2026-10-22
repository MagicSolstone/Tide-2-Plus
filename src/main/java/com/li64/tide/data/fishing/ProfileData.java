package com.li64.tide.data.fishing;

import com.li64.tide.data.journal.FishRarity;
import com.li64.tide.data.journal.JournalGroup;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public record ProfileData(Optional<String> description,
                          Optional<String> location,
                          Optional<Identifier> altSprite,
                          Optional<Integer> altSpriteSize,
                          FishRarity rarity,
                          JournalGroup group) {
    public static final Codec<ProfileData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("description").forGetter(ProfileData::description),
            Codec.STRING.optionalFieldOf("location").forGetter(ProfileData::location),
            Identifier.CODEC.optionalFieldOf("alt_sprite").forGetter(ProfileData::altSprite),
            Codec.INT.optionalFieldOf("alt_sprite_size").forGetter(ProfileData::altSpriteSize),
            FishRarity.CODEC.optionalFieldOf("rarity", FishRarity.COMMON).forGetter(ProfileData::rarity),
            JournalGroup.CODEC.optionalFieldOf("group", JournalGroup.MISC).forGetter(ProfileData::group)
    ).apply(instance, ProfileData::new));

    public ProfileData() {
        this(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(),
                FishRarity.COMMON, JournalGroup.MISC);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String description;
        private String location;
        private Identifier altSprite;
        private int altSpriteSize = 16;
        private FishRarity rarity = FishRarity.COMMON;
        private JournalGroup group = JournalGroup.MISC;

        private Builder() {}

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder altSprite(Identifier path, int size) {
            this.altSprite = path;
            this.altSpriteSize = size;
            return this;
        }

        public Builder rarity(FishRarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Builder group(JournalGroup group) {
            this.group = group;
            return this;
        }

        public ProfileData build() {
            return new ProfileData(
                    Optional.ofNullable(description),
                    Optional.ofNullable(location),
                    Optional.ofNullable(altSprite),
                    altSprite == null
                            ? Optional.empty()
                            : Optional.of(altSpriteSize),
                    rarity, group
            );
        }

        JournalGroup getGroup() {
            return this.group;
        }
    }
}
