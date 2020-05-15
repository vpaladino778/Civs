package org.redcastlemedia.multitallented.civs.civclass;

import org.redcastlemedia.multitallented.civs.items.CivItem;
import org.redcastlemedia.multitallented.civs.items.CVItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

public class ClassType extends CivItem {
    private final List<String> children;
    private final int manaPerSecond;
    private final int maxMana;
    @Getter
    private final Map<String, Integer> allowedActions = new HashMap<>();

    public ClassType(List<String> reqs,
                     String name,
                     CVItem icon,
                     CVItem shopIcon,
                     double price,
                     String permission,
                     List<String> children,
                     List<String> groups,
                     int manaPerSecond,
                     int maxMana,
                     boolean isInShop,
                     int level) {
        super(reqs,
                false,
                ItemType.CLASS,
                name,
                icon.getMat(),
                shopIcon,
                children.isEmpty() ? 1 : children.size(),
                0,
                1,
                price,
                permission,
                groups,
                isInShop,
                level);
        this.children = children;
        this.manaPerSecond = manaPerSecond;
        this.maxMana = maxMana;
    }

    public List<String> getChildren() {
        return children;
    }
    public int getManaPerSecond() {
        return manaPerSecond;
    }
    public int getMaxMana() {
        return maxMana;
    }
}
