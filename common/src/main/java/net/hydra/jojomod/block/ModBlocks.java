package net.hydra.jojomod.block;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class ModBlocks {
    /**This is where blocks are listed and called upon.
     * Forge and fabric files must define these variables so they are not empty.**/
    public static final IntegerProperty GAS_CAN_LEVEL = IntegerProperty.create("level", 0, 2);
    public static final BooleanProperty IGNITED = BooleanProperty.create("ignited");
    public static Block METEOR_BLOCK;

    public static Block LOCACACA_BLOCK;

    public static Block GASOLINE_SPLATTER;
    public static Block WIRE_TRAP;
    public static Block BARBED_WIRE;
    public static Block BARBED_WIRE_BUNDLE;
    public static Block METEOR_BLOCK_PROPERTIES = new Block(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
    );

    public static Block LOCACACA_BLOCK_PROPERTIES = new LocacacaBlock(
            BlockBehaviour.Properties.of()
                            .mapColor(MapColor.PLANT)
                            .noCollission()
                            .randomTicks()
                            .instabreak()
                            .sound(SoundType.CROP)
                            .pushReaction(PushReaction.DESTROY)
            );

    public static GasolineBlock GASOLINE_SPLATTER_PROPERTIES = new GasolineBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NONE)
                    .instrument(NoteBlockInstrument.BANJO)
                    .strength(0.1F, 1.0F)
                    .sound(SoundType.SLIME_BLOCK)
                    .replaceable()
                    .pushReaction(PushReaction.DESTROY)
                    .ignitedByLava()
                    .speedFactor(0.6F)
    );
    public static BarbedWireBlock WIRE_TRAP_PROPERTIES = new BarbedWireBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOL)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .strength(0.1F, 1.0F)
                    .sound(SoundType.VINE)
                    .forceSolidOn().noCollission().pushReaction(PushReaction.DESTROY),
            1F
    );
    public static BarbedWireBlock BARBED_WIRE_BLOCK_PROPERTIES = new BarbedWireBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .strength(0.5F, 1.0F)
                    .sound(SoundType.METAL)
                    .forceSolidOn().noCollission().requiresCorrectToolForDrops(),
            1.2F
    );
    public static BarbedWireBundleBlock BARBED_WIRE_BUNDLE_PROPERTIES = new BarbedWireBundleBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .strength(1.5F, 1.0F)
                    .sound(SoundType.METAL)
                    .forceSolidOn().noCollission().requiresCorrectToolForDrops()
    );

}
