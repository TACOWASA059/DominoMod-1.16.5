package com.github.tacowasa059.dominomod.core.itemgroup;

import com.github.tacowasa059.dominomod.core.init.ItemInit;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup extends ItemGroup {

    public static final ModItemGroup Domino_MOD = new ModItemGroup(ModItemGroup.GROUPS.length,
            "domino_mod");

    public ModItemGroup(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemInit.DOMINO_ITEM.get());
    }

}