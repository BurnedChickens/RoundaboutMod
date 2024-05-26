package net.hydra.jojomod.mixin;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ServerLevel.class)
public class ServerWorldMixin {


    /** Called every tick on the Server. Checks if a mob has a stand out, and updates the position of the stand.
     * @see StandEntity#tickStandOut */

    @Inject(method = "tickNonPassenger", at = @At(value = "TAIL"))
    private void tickEntity2(Entity $$0, CallbackInfo ci) {
        if ($$0.showVehicleHealth()) {
            LivingEntity livingEntity = (LivingEntity) $$0;
            if (((StandUser) $$0).hasStandOut()) {
                this.tickStandIn(livingEntity, Objects.requireNonNull(((StandUser) $$0).getStand()));
            }
        }
    }

    private void tickStandIn(LivingEntity entity, StandEntity passenger) {
        if (passenger == null || passenger.isRemoved() || passenger.getUser() != entity) {
            return;
        }
        byte ot = passenger.getOffsetType();
        if (OffsetIndex.OffsetStyle(ot) != OffsetIndex.LOOSE_STYLE) {
            passenger.setOldPosAndRot();
            ++passenger.tickCount;
            passenger.tickStandOut();
        }
    }

}