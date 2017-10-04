package org.redcastlemedia.multitallented.civs.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.redcastlemedia.multitallented.civs.LocaleManager;
import org.redcastlemedia.multitallented.civs.civilians.Civilian;
import org.redcastlemedia.multitallented.civs.civilians.CivilianManager;
import org.redcastlemedia.multitallented.civs.scheduler.CommonScheduler;
import org.redcastlemedia.multitallented.civs.util.CVItem;

public class MainMenu extends Menu {
    private static final String MENU_NAME = "CivsMenu";
    public MainMenu() {
        super(MENU_NAME);
    }

    @Override
    void handleInteract(InventoryClickEvent event) {
        event.setCancelled(true);

        ItemStack clickedStack = event.getCurrentItem();
        if (clickedStack.getItemMeta() == null) {
            return;
        }
        if (clickedStack.getItemMeta().getDisplayName() == null) {
            return;
        }
        ItemMeta im = clickedStack.getItemMeta();
        String itemName = im.getDisplayName();
        LocaleManager localeManager = LocaleManager.getInstance();
        Civilian civilian = CivilianManager.getInstance().getCivilian(event.getWhoClicked().getUniqueId());
        String locale = civilian.getLocale();
        if (itemName.equals(localeManager.getTranslation(locale, "language-menu"))) {
            event.getWhoClicked().closeInventory();
            event.getWhoClicked().openInventory(LanguageMenu.createMenu(locale));
            return;
        }
        if (itemName.equals(localeManager.getTranslation(locale, "shop"))) {
            event.getWhoClicked().closeInventory();
            event.getWhoClicked().openInventory(ShopMenu.createMenu(locale));
            return;
        }
        if (itemName.equals(localeManager.getTranslation(locale, "items"))) {
            event.getWhoClicked().closeInventory();
            event.getWhoClicked().openInventory(ItemsMenu.createMenu(civilian));
            return;
        }
        if (itemName.equals(localeManager.getTranslation(locale, "community"))) {
            event.getWhoClicked().closeInventory();
            event.getWhoClicked().openInventory(CommunityMenu.createMenu(locale));
            return;
        }
    }

    public static Inventory createMenu(String locale) {
        Inventory inventory = Bukkit.createInventory(null, 18, MENU_NAME);

        LocaleManager localeManager = LocaleManager.getInstance();
        CVItem cvItem = new CVItem(Material.GRASS, 1, -1, 100, localeManager.getTranslation(locale, "language-menu"));
        inventory.setItem(17, cvItem.createItemStack());

        CVItem cvItem1 = new CVItem(Material.EMERALD, 1, -1, 100, localeManager.getTranslation(locale, "shop"));
        inventory.setItem(0, cvItem1.createItemStack());

        CVItem cvItem2 = new CVItem(Material.CHEST, 1, -1, 100, localeManager.getTranslation(locale, "items"));
        inventory.setItem(1, cvItem2.createItemStack());

        CVItem cvItem3 = new CVItem(Material.BOOKSHELF, 1, -1, 100, localeManager.getTranslation(locale, "community"));
        inventory.setItem(2, cvItem3.createItemStack());

//        inventory.setItem(0, new ItemStack(Material.MAP));
        return inventory;
    }

}