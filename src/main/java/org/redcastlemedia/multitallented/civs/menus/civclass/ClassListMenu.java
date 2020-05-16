package org.redcastlemedia.multitallented.civs.menus.civclass;

import org.redcastlemedia.multitallented.civs.civilians.Civilian;
import org.redcastlemedia.multitallented.civs.menus.CivsMenu;
import org.redcastlemedia.multitallented.civs.menus.CustomMenu;

import java.util.Map;

@CivsMenu(name = "class-list") @SuppressWarnings("unused")
public class ClassListMenu extends CustomMenu {
    @Override
    public Map<String, Object> createData(Civilian civilian, Map<String, String> params) {
        return null;
    }
}
