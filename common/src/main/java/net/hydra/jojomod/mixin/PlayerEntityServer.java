package net.hydra.jojomod.mixin;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerEntityServer;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ServerPlayer.class)
public abstract class PlayerEntityServer extends Player implements IPlayerEntityServer {

    public PlayerEntityServer(Level world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Shadow
    private void initMenu(AbstractContainerMenu $$0) {
    }

    @Override
    @Unique
    public void roundabout$initMenu(AbstractContainerMenu $$0){
        initMenu($$0);
    }

    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V", at = @At(value = "TAIL"))
    public void roundabout$teleportTo(ServerLevel p_9000_, double p_9001_, double p_9002_, double p_9003_, float p_9004_, float p_9005_, CallbackInfo info) {
        if (p_9000_ == this.level()) {
            ((StandUser) this).roundabout$getFollowers().forEach($$0 -> {
                for (StandEntity $$1 : ((StandUser) this).roundabout$getFollowers()) {
                    $$1.moveTo(this.getX(),this.getY(),this.getZ());
                }
            });
            StandUser standUserData = ((StandUser) this);
            if (standUserData.roundabout$hasStandOut()) {

                standUserData.roundabout$updateStandOutPosition(standUserData.roundabout$getStand(), Entity::moveTo);
            }
        }
    }

    /**Make NBT save on death*/
    /** respawn */
    @Inject(method = "restoreFrom(Lnet/minecraft/server/level/ServerPlayer;Z)V", at = @At(value = "TAIL"))
    public void roundabout$respawn(ServerPlayer $$0, boolean $$1, CallbackInfo info) {
        ((StandUser)this).roundabout$setStandDisc(MainUtil.saveToDiscData($$0,((StandUser)$$0).roundabout$getStandDisc()));
        ((IPlayerEntity)this).roundabout$setMaskInventory(((IPlayerEntity)$$0).roundabout$getMaskInventory());
        ((IPlayerEntity)this).roundabout$getMaskInventory().update();
    }

}