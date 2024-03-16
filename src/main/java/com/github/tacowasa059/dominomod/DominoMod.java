package com.github.tacowasa059.dominomod;

import com.github.tacowasa059.dominomod.common.block.DominoBlock;
import com.github.tacowasa059.dominomod.common.tileentity.DominoTileEntity;
import com.github.tacowasa059.dominomod.core.init.BlockInit;
import com.github.tacowasa059.dominomod.core.init.DominoList;
import com.github.tacowasa059.dominomod.core.init.ItemInit;
import com.github.tacowasa059.dominomod.core.init.TileEntityTypesInit;
import com.github.tacowasa059.dominomod.core.itemgroup.ModItemGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

import static com.github.tacowasa059.dominomod.core.init.BlockInit.DOMINO_BLOCKS;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DominoMod.MOD_ID)
public class DominoMod {
    public static final String MOD_ID="dominomod";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public DominoMod() {
        // Register the setup method for modloading
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        bus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        bus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        bus.addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        for (DominoList domino : DominoList.values()) {
            DOMINO_BLOCKS.put(domino.name(),BlockInit.BLOCKS.register(domino.name(), DominoBlock::new));
            ItemInit.ITEMS.register(domino.name(), () -> new BlockItem(BlockInit.DOMINO_BLOCKS.get(domino.name()).get(), new Item.Properties()
                    .group(ModItemGroup.Domino_MOD)));
            TileEntityTypesInit.TILE_ENTITY_TYPE.register(domino.name(), ()-> TileEntityType.Builder.create(DominoTileEntity::new,BlockInit.DOMINO_BLOCKS.get(domino.name()).get()).build(null));
        }
        BlockInit.BLOCKS.register(bus);
        ItemInit.ITEMS.register(bus);
        TileEntityTypesInit.TILE_ENTITY_TYPE.register(bus);

    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("dominomod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
