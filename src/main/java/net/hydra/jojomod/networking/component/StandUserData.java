package net.hydra.jojomod.networking.component;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.StandEntity;
import net.hydra.jojomod.networking.MyComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class StandUserData implements StandUserComponent {
    //StandUserComponent standUserData = (StandUserComponent) MyComponents.STAND.get(player);
    private final LivingEntity User;
    @Nullable
    private StandEntity Stand;
    private boolean standActive;

    //StandID is clientside only
    private int StandID = -1;

    public StandUserData(LivingEntity entity) {
        this.User = entity;
    }
    public void sync() {
        MyComponents.STAND_USER.sync(this.User);
    }

    public void setActive(boolean active){
     this.standActive = active;
        this.sync();
    }

    public boolean getActive() {
        return this.standActive;
    }

    public void standMount(StandEntity StandSet){
        this.Stand = StandSet;
        StandSet.setMaster(User);
        this.sync();
    }
    public void setStand(StandEntity StandSet){
        this.Stand = StandSet;
        this.sync();
    }

    public void summonStand(World theWorld, boolean forced, boolean sound){
        boolean active;
        if ((!this.getActive() && !forced) || (forced && this.getActive())) {
            //world.getEntity
            StandEntity stand = ModEntities.THE_WORLD.create(User.getWorld());
            if (stand != null) {
                Vec3d spos = stand.getStandOffsetVector(User);
                stand.updatePosition(spos.getX(), spos.getY(), spos.getZ());

                theWorld.spawnEntity(stand);

                if (sound) {
                    stand.playSummonSound();
                }

                this.standMount(stand);
            }

            active=true;
        } else {
            active=false;
        }
        this.setActive(active);
    }

    @Nullable
    public StandEntity getStand(){
        if (this.User.getWorld().isClient) {
            if ((this.Stand == null || this.Stand.isRemoved()) && this.StandID > -1) {
                this.Stand = (StandEntity) User.getWorld().getEntityById(this.StandID);
            }
        }
        return this.Stand;
    }
    public boolean hasStandOut() {
        return (Stand != null && Stand.isAlive() && !Stand.isRemoved());
    }

    public void setDI(int forward, int strafe){
        //RoundaboutMod.LOGGER.info("MF:"+ forward);
        if (Stand != null){
            if (!User.isSneaking() && User.isSprinting()){
                forward*=2;}
            Stand.setMoveForward(forward);
        }
    }

    public void updateStandOutPosition(StandEntity passenger) {
        this.updateStandOutPosition(passenger, Entity::setPosition);
    }

    public void updateStandOutPosition(StandEntity passenger, Entity.PositionUpdater positionUpdater) {
        if (!(this.hasStandOut())) {
            return;
        }
        Vec3d grabPos = passenger.getStandOffsetVector(User);
        positionUpdater.accept(passenger, grabPos.x, grabPos.y, grabPos.z);
        passenger.setYaw(User.getHeadYaw());
        passenger.setPitch(User.getPitch());
        passenger.setBodyYaw(User.getHeadYaw());
        passenger.setHeadYaw(User.getHeadYaw());
    }


    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.standActive = buf.readBoolean();
        int stID = buf.readInt();
        this.StandID = stID;
        this.Stand = (StandEntity) User.getWorld().getEntityById(stID);
    }


    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeBoolean(this.standActive);
        int stID; if (this.Stand == null){stID=-1;} else {stID = this.Stand.getId();}
        buf.writeInt(stID);
    }
    public void onStandOutLookAround(StandEntity passenger) {
    }

    public void removeStandOut() {
        this.Stand = null;
        //this.emitGameEvent(GameEvent.ENTITY_DISMOUNT, passenger);
    }







    public void readFromNbt(NbtCompound tag) {

    }

    public void writeToNbt(NbtCompound tag) {

    }

}
