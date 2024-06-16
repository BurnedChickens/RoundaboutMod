package net.hydra.jojomod.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Inventory.class)
public interface ZInventoryAccess {
    @Accessor("compartments")
    List<NonNullList<ItemStack>> roundaboutGetCompartments();
}