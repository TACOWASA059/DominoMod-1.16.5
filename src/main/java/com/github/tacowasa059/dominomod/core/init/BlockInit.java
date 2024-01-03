package com.github.tacowasa059.dominomod.core.init;

import com.github.tacowasa059.dominomod.DominoMod;
import com.github.tacowasa059.dominomod.common.block.DominoBlock;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, DominoMod.MOD_ID);
    public static final RegistryObject<Block> DOMINO= BLOCKS.register("domino",
            DominoBlock::new);
    public static HashMap<String,RegistryObject<Block>> DOMINO_BLOCKS= new HashMap<>();;
}
