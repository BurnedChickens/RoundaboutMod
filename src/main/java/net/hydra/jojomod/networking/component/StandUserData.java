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

/** This code is attached to every single
 * @see LivingEntity
 * and is to store the stand that the mob may or may not have.
 * It is also a cleaner method than a mixin.*/
public class StandUserData implements StandUserComponent {
    //StandUserComponent standUserData = (StandUserComponent) MyComponents.STAND.get(player);
    private final LivingEntity User;
    @Nullable
    private StandEntity Stand;
    private boolean standActive;

    /** StandID is used clientside only*/
    private int StandID = -1;

    public boolean getActive() {
        return this.standActive;
    }

    public StandUserData(LivingEntity entity) {
        this.User = entity;
    }

    /** Calling sync sends packets which update data on the client side.
     * @see #applySyncPacket */
    public void sync() {
        MyComponents.STAND_USER.sync(this.User);
    }


    /** Turns your stand "on". This updates the HUD, and is necessary in case the stand doesn't have a body.*/
    public void setActive(boolean active){
     this.standActive = active;
        this.sync();
    }

    /** Sets a stand to a user, and a user to a stand.*/
    public void standMount(StandEntity StandSet){
        this.Stand = StandSet;
        StandSet.setMaster(User);
        this.sync();
    }

    /**Only sets a user's stand. Distinction may be important depending on when it is called.*/
    public void setStand(StandEntity StandSet){
        this.Stand = StandSet;
        this.sync();
    }

    /** Code that brings out a user's stand, based on the stand's summon sounds and conditions. */
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

    /** Returns the stand of a User, and makes necessary checks to reload the stand on a client
     * if the client does not have the stand loaded*/
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

    /** Set Direction input. This is part of stand rendering as leaning.
     * @see StandEntity#setMoveForward */
     public void setDI(int forward, int strafe){
        //RoundaboutMod.LOGGER.info("MF:"+ forward);
        if (Stand != null){
            if (!User.isSneaking() && User.isSprinting()){
                forward*=2;}
            Stand.setMoveForward(forward);
        }
    }

    /** Retooled vanilla riding code to update the location of a stand every tick relative to the entity it
     * is the user of.
     * @see StandEntity#getAnchorPlace */
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

    /** This is where the server writes out the id of the user's stand, to send to the client as a packet.*/
    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeBoolean(this.standActive);
        int stID; if (this.Stand == null){stID=-1;} else {stID = this.Stand.getId();}
        buf.writeInt(stID);
    }

    /** This is where the client reads the entity ids sent by the server and puts them into code.
     * Basically, it's how the client learns the user's stand, and any other stand following them.*/
    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.standActive = buf.readBoolean();
        int stID = buf.readInt();
        this.StandID = stID;
        this.Stand = (StandEntity) User.getWorld().getEntityById(stID);
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