package org.redcastlemedia.multitallented.civs.towns;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.redcastlemedia.multitallented.civs.TestUtil;
import org.redcastlemedia.multitallented.civs.items.ItemManager;

import static org.junit.Assert.*;

public class TownTests {
    private TownManager townManager;

    @BeforeClass
    public static void onBeforeEverything() {
        if (Bukkit.getServer() == null) {
            TestUtil.serverSetup();
        }
    }

    @Before
    public void onBefore() {
        townManager = new TownManager();
    }

    @Test
    public void findTownAtShouldReturnTown() {
        loadTownTypeHamlet();
        Town town = new Town("BizRep", "hamlet",
                new Location(Bukkit.getWorld("world"), 0, 0, 20));
        townManager.addTown(town);
        townManager.addTown(new Town("Silverstone", "hamlet",
                new Location(Bukkit.getWorld("world"), 100, 0, 0)));
        townManager.addTown(new Town("Cupcake", "hamlet",
                new Location(Bukkit.getWorld("world"), -100, 0, 0)));

        assertEquals(town, townManager.getTownAt(new Location(Bukkit.getWorld("world"), 0, 0,0)));
    }

    @Test
    public void shouldNotFindTown() {

        loadTownTypeHamlet();
        Town town = new Town("BizRep", "hamlet",
                new Location(Bukkit.getWorld("world"), 0, 0, 20));
        townManager.addTown(town);
        assertNull(townManager.getTownAt(new Location(Bukkit.getWorld("world"), 0, 55,0)));
    }

    private void loadTownTypeHamlet() {
        FileConfiguration config = new YamlConfiguration();
        config.set("name", "Hamlet");
        config.set("type", "town");
        config.set("build-radius", 25);
        ItemManager.getInstance().loadTownType(config, "hamlet");
    }
}