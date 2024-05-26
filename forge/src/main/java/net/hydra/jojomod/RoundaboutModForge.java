package net.hydra.jojomod;

import net.hydra.jojomod.registry.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

@Mod(Roundabout.MOD_ID)
public class RoundaboutModForge {

    public RoundaboutModForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ForgeEntities.ENTITY_TYPES.register(bus);
        ForgeItems.ITEMS.register(bus);
        ForgeBlocks.BLOCKS.register(bus);
        ForgeCreativeTab.TABS.register(bus);
        ForgeSounds.SOUNDS.register(bus);

        Roundabout.LOGGER.info("Hello Forge world!");
        Roundabout.init();
    }


}