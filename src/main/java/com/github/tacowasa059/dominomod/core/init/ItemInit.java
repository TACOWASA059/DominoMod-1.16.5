package com.github.tacowasa059.dominomod.core.init;

import com.github.tacowasa059.dominomod.DominoMod;
import com.github.tacowasa059.dominomod.common.tileentity.DominoTileEntity;
import com.github.tacowasa059.dominomod.core.itemgroup.ModItemGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            DominoMod.MOD_ID);
    public static final RegistryObject<Item> DOMINO_ITEM = ITEMS.register("domino", () -> new BlockItem(BlockInit.DOMINO.get(), new Item.Properties()
            .group(ModItemGroup.Domino_MOD)));

}