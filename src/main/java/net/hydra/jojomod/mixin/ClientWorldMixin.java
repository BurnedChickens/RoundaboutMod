package net.hydra.jojomod.mixin;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.entity.StandEntity;
import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {


    @Inject(method = "tickEntity", at = @At(value = "TAIL"))
    private void tickEntity2(Entity entity, CallbackInfo ci) {

        this.standTickCheck(entity);
        for (Entity entity2 : entity.getPassengerList()) {
            this.standTickCheck(entity2);
        }
    }

    private void standTickCheck(Entity entity){
        if (entity.isLiving()) {
            LivingEntity livingEntity = (LivingEntity) entity;
            StandUserComponent standUserData = MyComponents.STAND_USER.get(livingEntity);
            if (livingEntity.isPlayer()){
                RoundaboutMod.LOGGER.info("GS: "+standUserData.getStand());
                if (standUserData.getStand() != null) {
                    RoundaboutMod.LOGGER.info("GF: "+standUserData.getStand().getFollowing());
                }
                RoundaboutMod.LOGGER.info(""+standUserData.getStand());
                RoundaboutMod.LOGGER.info(""+standUserData.getStand());
            }
            if (standUserData.getStand() != null) {
                StandEntity stand = standUserData.getStand();
                if (stand.getFollowing() != null && stand.getFollowing().getId() == livingEntity.getId()){
                    this.tickStandIn(livingEntity, stand);
                }
            }
        }
    }

    private void tickStandIn(LivingEntity entity, StandEntity passenger) {
        if (passenger.isRemoved() || passenger.getMaster() != entity) {
            passenger.dismountMaster();
            return;
        }
        passenger.resetPosition();
        ++passenger.age;
        passenger.tickStandOut();
    }

}
