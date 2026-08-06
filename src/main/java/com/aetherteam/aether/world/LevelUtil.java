package com.aetherteam.aether.world;

import com.aetherteam.aether.AetherConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public final class LevelUtil {
    private static volatile CachedDimension destinationCache;
    private static volatile CachedDimension returnCache;

    private record CachedDimension(String id, ResourceKey<Level> key) {}

    public static ResourceKey<Level> destinationDimension() {
        String id = AetherConfig.SERVER.portal_destination_dimension_ID.get();

        CachedDimension cached = destinationCache;
        if (cached == null || !cached.id().equals(id)) {
            cached = new CachedDimension(id, createDimensionKey(id));
            destinationCache = cached;
        }

        return cached.key();
    }

    public static ResourceKey<Level> returnDimension() {
        String id = AetherConfig.SERVER.portal_return_dimension_ID.get();

        CachedDimension cached = returnCache;
        if (cached == null || !cached.id().equals(id)) {
            cached = new CachedDimension(id, createDimensionKey(id));
            returnCache = cached;
        }

        return cached.key();
    }

    private static ResourceKey<Level> createDimensionKey(String id) {
        return ResourceKey.create(
                Registries.DIMENSION,
                new ResourceLocation(id)
        );
    }

    private LevelUtil() {}
}
