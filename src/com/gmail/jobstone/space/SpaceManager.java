package com.gmail.jobstone.space;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.gmail.jobstone.PoorSpace;

public class SpaceManager {

    private static Map<String, SpaceManager> spaceWorlds = new HashMap<>();

    private final int world;

    public SpaceManager(String world) {
        this.world = NormalSpace.getWorldId(world);
    }

    private Cache<String, NormalSpace> spaceCache = CacheBuilder.newBuilder()
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .maximumSize(5000)
            .build();
    private ConcurrentMap<String, NormalSpace> loadedSpaces = spaceCache.asMap();

    public boolean load(String id) {
        if (isLoaded(id))
            return false;
        loadedSpaces.put(id, new NormalSpace(id, world));
        return true;
    }

    public void update(String id, NormalSpace space) {
        loadedSpaces.replace(id, space);
    }

    public boolean unload(String id) {
        if (isLoaded(id)) {
            loadedSpaces.remove(id);
            return true;
        }
        return false;
    }

    private boolean isLoaded(String id) {
        return loadedSpaces.containsKey(id);
    }

    public NormalSpace getSpace(String id) {
        if (isLoaded(id)) {
            return loadedSpaces.get(id);
        }
        else {
            NormalSpace space = new NormalSpace(id, this.world);
            loadedSpaces.put(id, space);
            return space;
        }
    }

    public int getLoadedSpacesSize() {
        return loadedSpaces.size();
    }

    public static final Set<String> knownFiles = ConcurrentHashMap.newKeySet();

    private static void scanDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanDirectory(file);
                } else if (file.getName().endsWith(".yml")) {
                    knownFiles.add(file.getAbsolutePath());
                }
            }
        }
    }

    public static boolean scanFinished = false;

    public static void initialize() {
        Bukkit.getScheduler().runTaskAsynchronously(PoorSpace.plugin, () -> {
            File spacesDir = new File(PoorSpace.plugin.getDataFolder(), "spaces");
            if (spacesDir.exists()) {
                scanDirectory(spacesDir);
            }
            scanFinished = true;
        });

        SpaceManager.spaceWorlds.put("world", new SpaceManager("world"));
        SpaceManager.spaceWorlds.put("world_nether", new SpaceManager("world_nether"));
        SpaceManager.spaceWorlds.put("world_the_end", new SpaceManager("world_the_end"));
        SpaceManager.spaceWorlds.put("creative", new SpaceManager("creative"));
    }

    public static SpaceManager getSpaceManager(String world) {
        return SpaceManager.spaceWorlds.get(world);
    }

    public static SpaceManager getSpaceManager(int world) {
        switch(world) {
            case 0:
                return SpaceManager.spaceWorlds.get("world");
            case 1:
                return SpaceManager.spaceWorlds.get("world_nether");
            case 2:
                return SpaceManager.spaceWorlds.get("world_the_end");
            case 3:
                return SpaceManager.spaceWorlds.get("creative");
            default:
                return null;
        }
    }

    public static NormalSpace getSpace(Location loc) {
        return SpaceManager.getSpaceManager(loc.getWorld().getName()).getSpace(NormalSpace.getSpaceId(loc));
    }

}
