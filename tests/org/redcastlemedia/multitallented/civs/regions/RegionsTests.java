package org.redcastlemedia.multitallented.civs.regions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.block.BlockPlaceEvent;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.redcastlemedia.multitallented.civs.TestUtil;
import org.redcastlemedia.multitallented.civs.items.ItemManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegionsTests {
    private RegionManager regionManager;

    @BeforeClass
    public static void onBeforeEverything() {
        if (Bukkit.getServer() == null) {
            TestUtil.serverSetup();
        }
    }

    @Before
    public void onBefore() {
        regionManager = new RegionManager();
    }

    @Test
    public void withNoRegionsShouldNotDetectRegion() {
        assertNull(RegionManager.getInstance().getRegionAt(TestUtil.block2.getLocation()));
    }

    @Test
    public void regionManagerShouldLoadRegionTypesFromConfig() {
        loadRegionTypeCobble();
        assertNotNull(ItemManager.getInstance().getItemType("cobble"));
    }

    @Test
    public void regionManagerShouldGetRegionBasedOnLocation() {
        Location location = new Location(Bukkit.getWorld("world"), 100, 0, 0);
        HashSet<UUID> owners = new HashSet<>();
        HashSet<UUID> members = new HashSet<>();
        regionManager.addRegion(new Region("cobble", owners, members, location, getRadii(), new HashSet<String>()));
        assertNull(regionManager.getRegionAt(new Location(Bukkit.getWorld("world"), 0,0,0)));
    }

    @Test
    public void regionShouldNotBeCreatedWithoutAllReqs() {
        loadRegionTypeCobble();

        BlockPlaceEvent event1 = mock(BlockPlaceEvent.class);
        when(event1.getPlayer()).thenReturn(TestUtil.player);
        when(event1.getBlockPlaced()).thenReturn(TestUtil.blockUnique);
        World world = Bukkit.getWorld("world");
        Block dirtBlock = mock(Block.class);
        when(dirtBlock.getType()).thenReturn(Material.DIRT);
        when(world.getBlockAt(1,0,0)).thenReturn(dirtBlock);
        RegionListener regionListener = new RegionListener();
        regionListener.onBlockPlace(event1);
        assertNull(regionManager.getRegionAt(TestUtil.blockUnique.getLocation()));
    }

    @Test
    public void regionShouldBeCreatedWithAllReqs() {
        loadRegionTypeCobble();

        World world = Bukkit.getWorld("world");
        when(world.getBlockAt(1,0,0)).thenReturn(TestUtil.block2);
        when(world.getBlockAt(2,0,0)).thenReturn(TestUtil.block3);
        BlockPlaceEvent event3 = mock(BlockPlaceEvent.class);
        when(event3.getBlockPlaced()).thenReturn(TestUtil.block3);
        BlockPlaceEvent event2 = mock(BlockPlaceEvent.class);
        when(event2.getBlockPlaced()).thenReturn(TestUtil.block2);
        BlockPlaceEvent event1 = mock(BlockPlaceEvent.class);
        when(event1.getPlayer()).thenReturn(TestUtil.player);
        when(event1.getBlockPlaced()).thenReturn(TestUtil.blockUnique);

        RegionListener regionListener = new RegionListener();
        regionListener.onBlockPlace(event2);
        regionListener.onBlockPlace(event3);
        regionListener.onBlockPlace(event1);
        assertEquals("cobble", regionManager.getRegionAt(TestUtil.blockUnique.getLocation()).getType());
    }
    @Test
    public void regionShouldNotBeCreatedWithAllReqsOutOfBounds() {
        loadRegionTypeCobble();

        World world = Bukkit.getWorld("world");
        when(world.getBlockAt(1,50,0)).thenReturn(TestUtil.block2);
        when(world.getBlockAt(11,50,0)).thenReturn(TestUtil.block3);
        when(world.getBlockAt(2,50,0)).thenReturn(TestUtil.blockUnique2);
        BlockPlaceEvent event3 = mock(BlockPlaceEvent.class);
        when(event3.getBlockPlaced()).thenReturn(TestUtil.block3);
        BlockPlaceEvent event2 = mock(BlockPlaceEvent.class);
        when(event2.getBlockPlaced()).thenReturn(TestUtil.block2);
        BlockPlaceEvent event1 = mock(BlockPlaceEvent.class);
        when(event1.getPlayer()).thenReturn(TestUtil.player);
        when(event1.getBlockPlaced()).thenReturn(TestUtil.blockUnique2);

        RegionListener regionListener = new RegionListener();
        regionListener.onBlockPlace(event2);
        regionListener.onBlockPlace(event3);
        regionListener.onBlockPlace(event1);
        assertNull(regionManager.getRegionAt(TestUtil.blockUnique2.getLocation()));
    }
    @Test
    public void regionShouldBeCreatedWithAllReqsFlex() {
        loadRegionTypeCobble();

        BlockPlaceEvent event3 = mock(BlockPlaceEvent.class);
        when(event3.getBlockPlaced()).thenReturn(TestUtil.block6);
        BlockPlaceEvent event2 = mock(BlockPlaceEvent.class);
        when(event2.getBlockPlaced()).thenReturn(TestUtil.block5);
        BlockPlaceEvent event1 = mock(BlockPlaceEvent.class);
        when(event1.getPlayer()).thenReturn(TestUtil.player);
        when(event1.getBlockPlaced()).thenReturn(TestUtil.blockUnique3);

        RegionListener regionListener = new RegionListener();
        regionListener.onBlockPlace(event2);
        regionListener.onBlockPlace(event3);
        regionListener.onBlockPlace(event1);
        Region region = regionManager.getRegionAt(TestUtil.blockUnique3.getLocation());
        assertEquals(3, region.getRadiusXN());
        assertEquals(7, region.getRadiusXP());
        assertEquals(5, region.getRadiusYN());
    }

    @Test
    public void regionShouldNotBeCreatedIfNotAllReqs() {
        loadRegionTypeCobble();

        BlockPlaceEvent event3 = mock(BlockPlaceEvent.class);
        when(event3.getBlockPlaced()).thenReturn(TestUtil.block4);
        BlockPlaceEvent event2 = mock(BlockPlaceEvent.class);
        when(event2.getBlockPlaced()).thenReturn(TestUtil.block2);
        BlockPlaceEvent event1 = mock(BlockPlaceEvent.class);
        when(event1.getPlayer()).thenReturn(TestUtil.player);
        when(event1.getBlockPlaced()).thenReturn(TestUtil.blockUnique);
        World world = Bukkit.getWorld("world");
        Block dirtBlock = mock(Block.class);
        when(dirtBlock.getType()).thenReturn(Material.DIRT);
        when(world.getBlockAt(2,0,0)).thenReturn(dirtBlock);

        RegionListener regionListener = new RegionListener();
        regionListener.onBlockPlace(event2);
        regionListener.onBlockPlace(event3);
        regionListener.onBlockPlace(event1);
        assertNull(regionManager.getRegionAt(TestUtil.blockUnique.getLocation()));
    }

    @Test
    public void regionManagerShouldFindSingleRegion() {
        loadRegionTypeCobble();
        HashSet<UUID> owners = new HashSet<>();
        owners.add(new UUID(1, 4));
        HashSet<UUID> members = new HashSet<>();
        Location location1 = new Location(Bukkit.getWorld("world"), 100, 0, 0);
        Location location2 = new Location(Bukkit.getWorld("world"), 0, 100, 0);
        Location location3 = new Location(Bukkit.getWorld("world"), 100, 100, 50);
        Location location4 = new Location(Bukkit.getWorld("world"), 0, 100, 50);
        Location location5 = new Location(Bukkit.getWorld("world"), 500, 100, 50);
        Location location6 = new Location(Bukkit.getWorld("world"), 100, 1000, 0);
        Location location7 = new Location(Bukkit.getWorld("world"), 1000, 0, 0);
        Location location8 = new Location(Bukkit.getWorld("world"), 1050, 0, 0);
        regionManager.addRegion(new Region("cobble", owners, members, location1, getRadii(), new HashSet<String>()));
        regionManager.addRegion(new Region("cobble", owners, members, location2, getRadii(), new HashSet<String>()));
        regionManager.addRegion(new Region("cobble", owners, members, location3, getRadii(), new HashSet<String>()));
        regionManager.addRegion(new Region("cobble", owners, members, location4, getRadii(), new HashSet<String>()));
        regionManager.addRegion(new Region("cobble", owners, members, location5, getRadii(), new HashSet<String>()));
        regionManager.addRegion(new Region("cobble", owners, members, location6, getRadii(), new HashSet<String>()));
        regionManager.addRegion(new Region("cobble", owners, members, location7, getRadii(), new HashSet<String>()));
        assertSame(location5, regionManager.getRegionAt(location5).getLocation());
        assertSame(location2, regionManager.getRegionAt(location2).getLocation());
        assertSame(location1, regionManager.getRegionAt(location1).getLocation());
        assertSame(location7, regionManager.getRegionAt(location7).getLocation());
        regionManager.addRegion(new Region("cobble", owners, members, location8, getRadii(), new HashSet<String>()));
        assertSame(location8, regionManager.getRegionAt(location8).getLocation());
    }

    @Test
    public void shouldNotBeAbleToCreateARegionOnTopOfAnotherRegion() {
        loadRegionTypeCobble();
        HashSet<UUID> owners = new HashSet<>();
        owners.add(new UUID(1, 4));
        HashSet<UUID> members = new HashSet<>();
        Location location1 = new Location(Bukkit.getWorld("world"), 0, 0, 0);
        Region region = new Region("cobble", owners, members, location1, getRadii(), new HashSet<String>());
        regionManager.addRegion(region);


        World world = Bukkit.getWorld("world");
        when(world.getBlockAt(1,0,0)).thenReturn(TestUtil.block2);
        when(world.getBlockAt(2,0,0)).thenReturn(TestUtil.block3);
        BlockPlaceEvent event3 = mock(BlockPlaceEvent.class);
        when(event3.getBlockPlaced()).thenReturn(TestUtil.block3);
        BlockPlaceEvent event2 = mock(BlockPlaceEvent.class);
        when(event2.getBlockPlaced()).thenReturn(TestUtil.block2);
        BlockPlaceEvent event1 = mock(BlockPlaceEvent.class);
        when(event1.getPlayer()).thenReturn(TestUtil.player);
        when(event1.getBlockPlaced()).thenReturn(TestUtil.blockUnique);

        RegionListener regionListener = new RegionListener();
        regionListener.onBlockPlace(event2);
        regionListener.onBlockPlace(event3);
        regionListener.onBlockPlace(event1);
        assertEquals(region, regionManager.getRegionAt(location1));
    }

    @Test
    public void strangeRegionShapeShouldBuildProperly() {
        loadRegionTypeRectangle();

        BlockPlaceEvent event3 = mock(BlockPlaceEvent.class);
        when(event3.getBlockPlaced()).thenReturn(TestUtil.block8);
        BlockPlaceEvent event2 = mock(BlockPlaceEvent.class);
        when(event2.getBlockPlaced()).thenReturn(TestUtil.block7);
        BlockPlaceEvent event1 = mock(BlockPlaceEvent.class);
        when(event1.getPlayer()).thenReturn(TestUtil.player);
        when(event1.getBlockPlaced()).thenReturn(TestUtil.blockUnique4);


        RegionListener regionListener = new RegionListener();
        regionListener.onBlockPlace(event2);
        regionListener.onBlockPlace(event3);
        regionListener.onBlockPlace(event1);

        assertNotNull(regionManager.getRegionAt(TestUtil.blockUnique4.getLocation()));
    }

    @Test
    public void playerShouldBeInMultipleEffectRadii() {
        loadRegionTypeCobble();
        HashSet<UUID> owners = new HashSet<>();
        owners.add(new UUID(1, 4));
        HashSet<UUID> members = new HashSet<>();
        Location location1 = new Location(Bukkit.getWorld("world"), 0, 0, 0);
        Location location2 = new Location(Bukkit.getWorld("world"), 5, 0, 0);
        regionManager.addRegion(new Region("cobble", owners, members, location1, getRadii(), new HashSet<String>()));
        regionManager.addRegion(new Region("cobble", owners, members, location2, getRadii(), new HashSet<String>()));
        RegionType regionType = (RegionType) ItemManager.getInstance().getItemType("cobble");
        assertEquals(2, regionManager.getRegionEffectsAt(TestUtil.player.getLocation(), regionType.getEffectRadius() - regionType.getBuildRadius()).size());
    }

    @Test
    public void regionsInDifferentWorldsShouldntCollide() {
        loadRegionTypeCobble();
        HashSet<UUID> owners = new HashSet<>();
        owners.add(new UUID(1, 4));
        HashSet<UUID> members = new HashSet<>();
        Location location1 = new Location(Bukkit.getWorld("world"), 0, 0, 0);
        Location location2 = new Location(Bukkit.getWorld("world2"), 0, 0, 0);
        regionManager.addRegion(new Region("cobble", owners, members, location1, getRadii(), new HashSet<String>()));
        regionManager.addRegion(new Region("cobble", owners, members, location2, getRadii(), new HashSet<String>()));
        assertEquals(1, regionManager.getRegionEffectsAt(TestUtil.player.getLocation(), 0).size());
    }

    public static int[] getRadii() {
        int[] radiuses = new int[6];
        radiuses[0] = 5;
        radiuses[1] = 5;
        radiuses[2] = 5;
        radiuses[3] = 5;
        radiuses[4] = 5;
        radiuses[5] = 5;
        return radiuses;
    }

    public static void loadRegionTypeCobble() {
        FileConfiguration config = new YamlConfiguration();
        config.set("name", "cobble");
        ArrayList<String> reqs = new ArrayList<>();
        reqs.add("cobblestone*2");
        config.set("requirements", reqs);
        ArrayList<String> effects = new ArrayList<>();
        effects.add("block_place");
        effects.add("block_break");
        config.set("effects", effects);
        config.set("effect-radius", 7);
        ItemManager.getInstance().loadRegionType(config);
    }
    public static void loadRegionTypeDirt() {
        FileConfiguration config = new YamlConfiguration();
        config.set("name", "dirt");
        ArrayList<String> reqs = new ArrayList<>();
        reqs.add("dirt*1");
        config.set("requirements", reqs);
        ArrayList<String> effects = new ArrayList<>();
        config.set("effects", effects);
        ItemManager.getInstance().loadRegionType(config);
    }
    public static void loadRegionTypeRectangle() {
        FileConfiguration config = new YamlConfiguration();
        config.set("name", "cobble");
        ArrayList<String> reqs = new ArrayList<>();
        reqs.add("cobblestone*2");
        config.set("requirements", reqs);
        ArrayList<String> effects = new ArrayList<>();
        config.set("effects", effects);
        config.set("build-radius", 3);
        config.set("build-radius-z", 10);
        ItemManager.getInstance().loadRegionType(config);
    }

}