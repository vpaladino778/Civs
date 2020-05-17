package org.redcastlemedia.multitallented.civs.menus.civclass;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.redcastlemedia.multitallented.civs.civclass.CivClass;
import org.redcastlemedia.multitallented.civs.civclass.ClassManager;
import org.redcastlemedia.multitallented.civs.civilians.Civilian;
import org.redcastlemedia.multitallented.civs.items.CVItem;
import org.redcastlemedia.multitallented.civs.items.ItemManager;
import org.redcastlemedia.multitallented.civs.menus.CivsMenu;
import org.redcastlemedia.multitallented.civs.menus.CustomMenu;
import org.redcastlemedia.multitallented.civs.menus.MenuIcon;
import org.redcastlemedia.multitallented.civs.menus.MenuManager;
import org.redcastlemedia.multitallented.civs.regions.Region;
import org.redcastlemedia.multitallented.civs.regions.RegionManager;
import org.redcastlemedia.multitallented.civs.towns.Town;
import org.redcastlemedia.multitallented.civs.towns.TownManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CivsMenu(name = "class-list") @SuppressWarnings("unused")
public class ClassListMenu extends CustomMenu {
    @Override
    public Map<String, Object> createData(Civilian civilian, Map<String, String> params) {
        Map<String, Object> data = new HashMap<>();

        if (params.containsKey("page")) {
            data.put("page", Integer.parseInt(params.get("page")));
        } else {
            data.put("page", 0);
        }
        List<CivClass> civClasses = new ArrayList<>();

        // Currently adds all classes to data
        // TODO: Determine if adding all classes in necessary. Should this be only unlocked classes?
        for (CivClass civClass : ClassManager.getInstance().getAllCivClasses()) {
            civClasses.add(civClass);
        }

        data.put("civClassMap", new HashMap<ItemStack, Region>());
        data.put("civClasses", civClasses);
        int maxPage = (int) Math.ceil((double) civClasses.size() / (double) itemsPerPage.get("civClasses"));
        maxPage = maxPage > 0 ? maxPage - 1 : 0;
        data.put("maxPage", maxPage);

        for (String key : params.keySet()) {
            if (key.equals("page") || key.equals("maxPage")) {
                continue;
            }
            data.put(key, params.get(key));
        }

        return data;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ItemStack createItemStack(Civilian civilian, MenuIcon menuIcon, int count) {
        if (menuIcon.getKey().equals("civClasses")) {
            List<CivClass> civClasses;
            if (MenuManager.getData(civilian.getUuid(), "civClasses") != null) {
                civClasses = (List<CivClass>) MenuManager.getData(civilian.getUuid(), "civClasses");
            } else {
                civClasses = new ArrayList<>();
                for (CivClass civClass : ClassManager.getInstance().getAllCivClasses()) {
                    // TODO: Determine if we need a check here. Do we need to check if a class is unlocked?
                    civClasses.add(civClass);
                }
            }
            int page = (int) MenuManager.getData(civilian.getUuid(), "page");
            int startIndex = page * menuIcon.getIndex().size();
            CivClass[] civClassArray = new CivClass[civClasses.size()];
            civClassArray = civClasses.toArray(civClassArray);
            if (civClassArray.length <= startIndex + count) {
                return new ItemStack(Material.AIR);
            }
            CivClass civClass = civClassArray[startIndex + count];
            CVItem cvItem = ItemManager.getInstance().getItemType(civClass.getType()).getShopIcon(civilian.getLocale());

            ItemStack itemStack = cvItem.createItemStack();
            ((HashMap<ItemStack, CivClass>) MenuManager.getData(civilian.getUuid(), "civClassMap")).put(itemStack, civClass);
            List<String> actionList = getActions(civilian, itemStack);
            putActions(civilian, menuIcon, itemStack, count);

            return itemStack;
        }
        return super.createItemStack(civilian, menuIcon, count);
    }
}
