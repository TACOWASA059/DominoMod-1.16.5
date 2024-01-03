package com.github.tacowasa059.dominomod.core.init;

import com.github.tacowasa059.dominomod.DominoMod;
import com.github.tacowasa059.dominomod.common.tileentity.DominoTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityTypesInit {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPE =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, DominoMod.MOD_ID);
    public static final RegistryObject<TileEntityType<DominoTileEntity>> DOMINO_TILE_ENTITY_TYPE =
            TILE_ENTITY_TYPE.register("domino",()->TileEntityType.Builder.create(DominoTileEntity::new,BlockInit.DOMINO.get()).build(null));

}
