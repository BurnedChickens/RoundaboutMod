package net.hydra.jojomod.client;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.gui.PowerInventoryMenu;
import net.hydra.jojomod.client.gui.PowerInventoryScreen;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Vector3f;

public class ClientUtil {

    /**
     * A generalized packet for sending ints to the client. Context is what to do with the data int
     */
    public static void handleIntPacketS2C(LocalPlayer player, int data, byte context) {
        if (context == 1) {
            ((StandUser) player).roundabout$setGasolineTime(data);
        } else if (context == PacketDataIndex.S2C_INT_OXYGEN_TANK) {
            ((StandUser) player).roundabout$getStandPowers().setAirAmount(data);
        } else if (context== PacketDataIndex.S2C_INT_GRAB_ITEM){
            Entity target = player.level().getEntity(data);
            if (target instanceof ItemEntity IE) {
                IE.getItem().shrink(1);
            }
        } else if (context== PacketDataIndex.S2C_INT_ATD){
            ((StandUser) player).roundabout$getStandPowers().setAttackTimeDuring(data);
        }
    }

    public static void handleIntPacketS2C(int data, byte context) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            handleIntPacketS2C(player, data, context);
        }
    }

    /**
     * A generalized packet for sending ints to the client. Context is what to do with the data int
     */
    public static void handleBlipPacketS2C(LocalPlayer player, int data, byte context, Vector3f vec) {
        if (context == 2) {
            /*This code makes the world using mobs appear to teleport by skipping interpolation*/
            Entity target = player.level().getEntity(data);
            if (target instanceof LivingEntity LE) {
                ((StandUser) target).roundabout$setBlip(vec);

                StandEntity SE = ((StandUser) target).roundabout$getStand();
                if (SE != null) {
                    ((StandUser) SE).roundabout$setBlip(vec);
                }
            }
        }
    }

    public static void clashUpdatePacket(int clashOpID, float progress) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ((StandUser) player).roundabout$getStandPowers().setClashOp((LivingEntity) player.level().getEntity(clashOpID));
            ((StandUser) player).roundabout$getStandPowers().setClashOpProgress(progress);
        }
    }

    public static void handlePlaySoundPacket(int startPlayerID, byte soundQue) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Entity User = player.level().getEntity(startPlayerID);
            ((StandUserClient) User).roundabout$clientQueSound(soundQue);
        }
    }

    public static float getDelta() {
        Minecraft mc = Minecraft.getInstance();
        return mc.getDeltaFrameTime();
    }
    public static void handlePowerFloatPacket(byte activePower, float data){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ((StandUser) player).roundabout$getStandPowers().updatePowerFloat(activePower,data);
        }
    }
    public static void handlePowerIntPacket(byte activePower, int data){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ((StandUser) player).roundabout$getStandPowers().updatePowerInt(activePower,data);
        }
    }
    public static void handleGuardUpdate(float guardPoints, boolean guardBroken){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ((StandUser) player).roundabout$setGuardPoints(guardPoints);
            ((StandUser) player).roundabout$setGuardBroken(guardBroken);
        }
    }
    public static void CDSyncPacket(int attackTime, int attackTimeMax, int attackTimeDuring,
                                    byte activePower, byte activePowerPhase){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            MainUtil.syncCooldownsForAttacks(attackTime, attackTimeMax, attackTimeDuring,
                    activePower, activePowerPhase, player);
        }
    }

    public static void skillCDSyncPacket(byte power, int cooldown){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            StandPowers powers = ((StandUser) player).roundabout$getStandPowers();
            powers.setCooldown(power,cooldown);
        }
    }

    public static void handleEntityResumeTsPacket(Vec3i vec3i){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            BlockEntity openedBlock = player.level().getBlockEntity(new BlockPos(vec3i.getX(),vec3i.getY(),vec3i.getZ()) );
            if (openedBlock != null){
                ((TimeStop) player.level()).processTSBlockEntityPacket(openedBlock);
            }
        }
    }

    public static void updateDazePacket(byte dazeTime){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ((StandUser) player).roundabout$setDazeTime(dazeTime);
        }
    }
    public static void handleBlipPacketS2C(int data, byte context, Vector3f vec){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
             handleBlipPacketS2C(player,data,context,vec);
        }
    }
    public static void handleBundlePacketS2C(byte context, byte one, byte two, byte three){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            handleBundlePacketS2C(player,context,one,two,three);
        }
    }

    public static void handleTimeStoppingEntityPacket(int timeStoppingEntity, double x, double y, double z, double range, int duration, int maxDuration){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ((TimeStop) player.level()).processTSPacket(timeStoppingEntity,x,y,z,range,duration,maxDuration);
        }
    }

    public static void processTSRemovePacket(int entityID){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ((TimeStop) player.level()).processTSRemovePacket(entityID);
        }
    }
    public static void handleStopSoundPacket(int data, byte context){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Entity User = player.level().getEntity(data);
            ((StandUserClient)User).roundabout$clientQueSoundCanceling(context);
        }
    }


    /**A generalized packet for sending bytes to the client. Only a context is provided.*/
    public static void handleBundlePacketS2C(LocalPlayer player, byte context, byte byte1, byte byte2, byte byte3){
        if (context == PacketDataIndex.S2C_BUNDLE_POWER_INV){
            IPlayerEntity ple = ((IPlayerEntity) player);
            StandUser se = ((StandUser) player);
            if (byte2 > 0) {
                ple.roundabout$setUnlockedBonusSkin(true);
            } else {
                ple.roundabout$setUnlockedBonusSkin(false);
            }
            se.roundabout$setStandSkin(byte1);

        }
    }
    /**A generalized packet for sending bytes to the client. Only a context is provided.*/
    public static void handleSimpleBytePacketS2C(LocalPlayer player, byte context){
        if (context == 1) {
            ((StandUser) player).roundabout$setGasolineTime(context);
        } else if (context == PacketDataIndex.S2C_SIMPLE_FREEZE_STAND) {
            if (((StandUser)player).roundabout$getStandPowers().hasCooldowns()) {
                ((StandUser) player).roundabout$setMaxSealedTicks(300);
                ((StandUser) player).roundabout$setSealedTicks(300);
            } else {
                ((StandUser) player).roundabout$setMaxSealedTicks(100);
                ((StandUser) player).roundabout$setSealedTicks(100);
            }
        } else if (context == PacketDataIndex.S2C_SIMPLE_SUSPEND_RIGHT_CLICK) {
            ((StandUser) player).roundabout$getStandPowers().suspendGuard = true;
            ((StandUser) player).roundabout$getStandPowers().scopeLevel = 0;
        }
    } public static void handleSimpleBytePacketS2C(byte context){
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            handleSimpleBytePacketS2C(player,context);
        }
    }
}
