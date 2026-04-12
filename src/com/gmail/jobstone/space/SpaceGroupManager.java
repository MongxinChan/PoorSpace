package com.gmail.jobstone.space;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class SpaceGroupManager {

    private static final Cache<String, SpaceGroup> groupCache = CacheBuilder.newBuilder()
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .maximumSize(5000)
            .build();

    public static SpaceGroup getGroup(String name) {
        SpaceGroup group = groupCache.getIfPresent(name);
        if (group == null) {
            group = new SpaceGroup(name);
            groupCache.put(name, group);
        }
        return group;
    }

    public static void invalidate(String name) {
        SpaceGroup group = groupCache.getIfPresent(name);
        if (group != null) {
            group.invalidateCache();
        }
        groupCache.invalidate(name);
    }
}
