package org.redcastlemedia.multitallented.civs.towns;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.redcastlemedia.multitallented.civs.Civs;
import org.redcastlemedia.multitallented.civs.items.ItemManager;
import org.redcastlemedia.multitallented.civs.regions.Region;
import org.redcastlemedia.multitallented.civs.util.CVItem;
import org.redcastlemedia.multitallented.civs.util.Util;

import java.io.File;
import java.util.*;

public class TownManager {

    private static TownManager townManager = null;
    private HashMap<String, Town> towns = new HashMap<>();
    private List<Town> sortedTowns = new ArrayList<Town>();

    public TownManager() {
        townManager = this;
    }

    public void loadAllTowns() {
        File townFolder = new File(Civs.getInstance().getDataFolder(), "towns");
        if (!townFolder.exists()) {
            townFolder.mkdir();
        }
        try {
            for (File file : townFolder.listFiles()) {
                FileConfiguration config = new YamlConfiguration();
                try {
                    config.load(file);

                    loadTown(config, file.getName().replace(".yml", ""));
                } catch (Exception e) {
                    Civs.logger.warning("Unable to read from towns/" + file.getName());
                }
            }
        } catch (NullPointerException npe) {
            Civs.logger.severe("Unable to read from town folder!");
        }
    }

    public Town getTown(String name) {
        return towns.get(name);
    }

    public Town getTownAt(Location location) {
        ItemManager itemManager = ItemManager.getInstance();
        for (Town town : sortedTowns) {
            TownType townType = (TownType) itemManager.getItemType(town.getType());
            int radius = townType.getBuildRadius();
            int radiusY = townType.getBuildRadiusY();
            Location townLocation = town.getLocation();

            if (townLocation.getX() - radius > location.getX()) {
                break;
            }

            if (townLocation.getX() + radius > location.getX() &&
                    townLocation.getZ() + radius > location.getZ() &&
                    townLocation.getZ() - radius < location.getZ() &&
                    townLocation.getY() - radiusY < location.getY() &&
                    townLocation.getY() + radiusY > location.getY()) {
                return town;
            }

        }
        return null;
    }

    private void loadTown(FileConfiguration config, String name) {

        Town town = new Town(name,
                config.getString("type"),
                Region.idToLocation(config.getString("location")));
        addTown(town);
    }
    void addTown(Town town) {
        towns.put(town.getName().toLowerCase(), town);
        sortedTowns.add(town);
        Collections.sort(sortedTowns, new Comparator<Town>() {

            @Override
            public int compare(Town o1, Town o2) {
                ItemManager itemManager = ItemManager.getInstance();
                TownType townType1 = (TownType) itemManager.getItemType(o1.getType());
                TownType townType2 = (TownType) itemManager.getItemType(o2.getType());
                if (o1.getLocation().getX() - townType1.getBuildRadius() >
                         o2.getLocation().getX() - townType2.getBuildRadius()) {
                    return 1;
                } else if (o1.getLocation().getX() - townType1.getBuildRadius() <
                        o2.getLocation().getX() - townType2.getBuildRadius()) {
                    return -1;
                }
                return 0;
            }
        });
    }

    public static TownManager getInstance() {
        if (townManager == null) {
            new TownManager();
        }
        return townManager;
    }
}