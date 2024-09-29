package net.hydra.jojomod.event.powers.stand;

import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.KnifeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.event.powers.stand.presets.TWAndSPSharedPowers;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class PowersTheWorld extends TWAndSPSharedPowers {

    public PowersTheWorld(LivingEntity self) {
        super(self);
    }

    @Override
    protected SoundEvent getSummonSound() {
        return ModSounds.WORLD_SUMMON_SOUND_EVENT;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersTheWorld(entity);
    }

    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.THE_WORLD.create(this.getSelf().level());
    }

    @Override
    public SoundEvent getLastHitSound(){
        return ModSounds.STAND_THEWORLD_MUDA3_SOUND_EVENT;
    }
    @Override
    public SoundEvent getLastRejectionHitSound(){
        return ModSounds.STAND_THEWORLD_MUDA3_SOUND_EVENT;
    }

    /**Assault Ability*/
    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide && !this.isClashing() && this.getActivePower() != PowerIndex.POWER_2
                && (this.getActivePower() != PowerIndex.POWER_2_EXTRA || this.getAttackTimeDuring() < 0) && !hasEntity()
                && (this.getActivePower() != PowerIndex.POWER_2_SNEAK || this.getAttackTimeDuring() < 0) && !hasBlock()) {
            if (!((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                if (!this.onCooldown(PowerIndex.SKILL_1)) {
                    if (keyIsDown) {
                        if (!options.keyShift.isDown()) {

                        } else {
                        }
                    }
                }
            }
        }
    }




    @Override
    public void playBarrageClashSound(){
        if (!this.self.level().isClientSide()) {
            playSoundsIfNearby(BARRAGE_NOISE_2, 32, false);
        }
    }
    @Override
    public boolean tryPower(int move, boolean forced) {
        return super.tryPower(move,forced);
    }

    @Override
    public int setCurrentMaxTSTime(int chargedTSSeconds){
        if (chargedTSSeconds >= 100){
            this.maxChargeTSTime = 180;
            this.setChargedTSTicks(180);
            return 80;
        } else if (chargedTSSeconds == 20) {
            this.maxChargeTSTime = 20;
        } else {
            this.maxChargeTSTime = 100;
        }
        return 0;
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {

        return super.setPowerOther(move,lastMove);
    }


    @Override
    public void tickPower(){

        super.tickPower();
        if (this.getSelf().isAlive() && !this.getSelf().isRemoved()) {

        }
    }

    /**Charge up Time Stop*/
    @Override
    public boolean canChangePower(int move, boolean forced){
        if (!this.isClashing() || move == PowerIndex.CLASH_CANCEL) {
            if ((this.getActivePower() == PowerIndex.NONE || forced) &&
                    (!this.isDazed(this.getSelf()) || move == PowerIndex.BARRAGE_CLASH)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public byte chooseBarrageSound(){
        double rand = Math.random();
        if (rand > 0.5) {
            return BARRAGE_NOISE;
        } else {
            return BARRAGE_NOISE_2;
        }
    }

    public int teleportTime = 0;
    public int postTPStall = 0;

    @Override
    public void tickMobAI(LivingEntity attackTarget){
        if (attackTarget != null && attackTarget.isAlive()){
            if (this.attackTimeDuring <= -1) {
                if (!this.getSelf().isPassenger()) {
                    teleportTime = Math.max(0,teleportTime-1);
                    if (teleportTime == 0) {
                        double dist = attackTarget.distanceTo(this.getSelf());
                        if (dist <= 8 && !(this.getSelf() instanceof Creeper)) {
                            Vec3 pos = this.getSelf().position().add(0,this.getSelf().getEyeHeight(),0);
                            float p = 0;
                            float y = 0;
                            if (this.getSelf() instanceof Villager){
                                p =getLookAtEntityPitch(this.getSelf(), attackTarget);
                                y = getLookAtEntityYaw(this.getSelf(), attackTarget);
                            }
                            if (this.teleport()){
                                if (this.getSelf() instanceof Villager){
                                    for (int i = 0; i< 4; i++) {
                                        KnifeEntity $$7 = new KnifeEntity(this.getSelf().level(), this.getSelf(), ModItems.KNIFE.getDefaultInstance(),pos);
                                        $$7.pickup = AbstractArrow.Pickup.DISALLOWED;
                                        $$7.shootFromRotationWithVariance(this.getSelf(),
                                                p,
                                                y,
                                                -0.5F, 1.5F, 1.0F);
                                        this.getSelf().level().addFreshEntity($$7);
                                    }
                                }
                                teleportTime = 200;
                                postTPStall = 8;
                            }
                        } else if (dist < 40) {
                            if (this.teleportTowards(attackTarget)) {
                                if (this.getSelf() instanceof Creeper){
                                    this.teleportTime = 100;
                                } else {
                                    this.teleportTime = 200;
                                }
                                postTPStall = 8;
                            }
                        }

                    }
                }
            }
        }
        postTPStall = Math.max(0,postTPStall-1);
        if (postTPStall == 0) {
            if (!(this.getSelf() instanceof Creeper)) {
                super.tickMobAI(attackTarget);
            }
        }
    }


    protected boolean teleport() {
        if (!this.getSelf().level().isClientSide() && this.getSelf().isAlive()) {
            double $$0 = this.getSelf().getX() + (this.getSelf().getRandom().nextDouble() - 0.5) * 19.0;
            double $$1 = this.getSelf().getY() + (double)(this.getSelf().getRandom().nextInt(16) - 8);
            double $$2 = this.getSelf().getZ() + (this.getSelf().getRandom().nextDouble() - 0.5) * 19.0;
            return this.teleport($$0, $$1, $$2);
        } else {
            return false;
        }
    }

    boolean teleportTowards(Entity $$0) {
        Vec3 $$1 = new Vec3(this.getSelf().getX() - $$0.getX(), this.getSelf().getY(0.5) - $$0.getEyeY(), this.getSelf().getZ() - $$0.getZ());
        $$1 = $$1.normalize();
        double $$2 = 16.0;
        double $$3 = this.getSelf().getX() + (this.getSelf().getRandom().nextDouble() - 0.5) * 8.0 - $$1.x * 16.0;
        double $$4 = this.getSelf().getY() + (double)(this.getSelf().getRandom().nextInt(16) - 8) - $$1.y * 16.0;
        double $$5 = this.getSelf().getZ() + (this.getSelf().getRandom().nextDouble() - 0.5) * 8.0 - $$1.z * 16.0;
        return this.teleport($$3, $$4, $$5);
    }

    private boolean teleport(double $$0, double $$1, double $$2) {
        BlockPos.MutableBlockPos $$3 = new BlockPos.MutableBlockPos($$0, $$1, $$2);

        while ($$3.getY() > this.getSelf().level().getMinBuildHeight() && !this.getSelf().level().getBlockState($$3).blocksMotion()) {
            $$3.move(Direction.DOWN);
        }

        BlockState $$4 = this.getSelf().level().getBlockState($$3);
        boolean $$5 = $$4.blocksMotion();
        boolean $$6 = $$4.getFluidState().is(FluidTags.WATER);
        if ($$5 && !$$6) {
            Vec3 $$7 = this.getSelf().position();
            boolean $$8 = randomTeleport($$0, $$1, $$2, true);
            if ($$8) {
                if (!this.getSelf().isSilent()) {
                    this.getSelf().level().playSound(null, this.getSelf().xo, this.getSelf().yo,
                            this.getSelf().zo, ModSounds.TIME_SNAP_EVENT, this.getSelf().getSoundSource(), 2.0F, 1.0F);
                    this.getSelf().playSound(ModSounds.TIME_SNAP_EVENT, 2.0F, 1.0F);
                }
            }

            return $$8;
        } else {
            return false;
        }
    }

    public boolean randomTeleport(double $$0, double $$1, double $$2, boolean $$3) {
        double $$4 = this.getSelf().getX();
        double $$5 = this.getSelf().getY();
        double $$6 = this.getSelf().getZ();
        double $$7 = $$1;
        boolean $$8 = false;
        BlockPos $$9 = BlockPos.containing($$0, $$1, $$2);
        Level $$10 = this.getSelf().level();
        if ($$10.hasChunkAt($$9)) {
            boolean $$11 = false;

            while (!$$11 && $$9.getY() > $$10.getMinBuildHeight()) {
                BlockPos $$12 = $$9.below();
                BlockState $$13 = $$10.getBlockState($$12);
                if ($$13.blocksMotion()) {
                    $$11 = true;
                } else {
                    $$7--;
                    $$9 = $$12;
                }
            }

            if ($$11) {
                AABB bb2 = this.getSelf().getDimensions(this.getSelf().getPose()).makeBoundingBox($$0,$$7,$$2);
                if ($$10.noCollision(null,bb2) && !$$10.containsAnyLiquid(bb2)) {
                    $$8 = true;
                    packetNearby(new Vector3f((float) $$0, (float) $$7, (float) $$2));
                    this.getSelf().teleportTo($$0, $$7, $$2);
                    packetNearby(new Vector3f((float) $$0, (float) $$7, (float) $$2));
                }
            }
        }
        if (!$$8) {
            this.getSelf().teleportTo($$4, $$5, $$6);
            return false;
        } else {

            if (this.getSelf() instanceof PathfinderMob) {
                ((PathfinderMob)this.getSelf()).getNavigation().stop();
            }

            return true;
        }
    }


    public final void packetNearby(Vector3f blip) {
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(),  this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, 100)) {
                    ModPacketHandler.PACKET_ACCESS.sendBlipPacket(serverPlayerEntity, (byte) 2, this.getSelf().getId(),blip);
                }
            }
        }
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == BARRAGE_NOISE) {
            return ModSounds.STAND_THEWORLD_MUDA5_SOUND_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_2){
            return ModSounds.STAND_THEWORLD_MUDA1_SOUND_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE) {
            return ModSounds.TIME_STOP_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_2) {
            return ModSounds.TIME_STOP_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_3) {
            return ModSounds.TIME_STOP_THE_WORLD3_EVENT;
        } else if (soundChoice == SoundIndex.SPECIAL_MOVE_SOUND_2) {
            return ModSounds.TIME_RESUME_EVENT;
        } else if (soundChoice == TIME_STOP_CHARGE){
            return ModSounds.TIME_STOP_CHARGE_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE_2){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE_3){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD3_EVENT;
        } else if (soundChoice == TIME_STOP_ENDING_NOISE){
            return ModSounds.TIME_STOP_RESUME_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_ENDING_NOISE_2){
            return ModSounds.TIME_STOP_RESUME_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_RESUME_NOISE){
            return ModSounds.TIME_RESUME_EVENT;
        } else if (soundChoice == TIME_STOP_TICKING){
            return ModSounds.TIME_STOP_TICKING_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    //public void setSkillIcon(GuiGraphics context, int x, int y, ResourceLocation rl, boolean dull, @Nullable CooldownInstance cooldownInstance){

    @Override
    public void renderIcons(GuiGraphics context, int x, int y){
        if (this.getSelf().isCrouching()){

            setSkillIcon(context, x, y, 2, StandIcons.THE_WORLD_GRAB_ITEM, PowerIndex.SKILL_2);

            boolean done = false;
            if (((StandUser)this.getSelf()).roundabout$getLeapTicks() > -1){

                if (!this.getSelf().onGround() && canStandRebound()) {
                        done=true;
                        setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_REBOUND_WORLD, PowerIndex.SKILL_3_SNEAK);
                }

            } else {

                if (!this.getSelf().onGround()){
                    if (canVault()){
                        done=true;
                        setSkillIcon(context, x, y, 3, StandIcons.THE_WORLD_LEDGE_GRAB, PowerIndex.SKILL_3_SNEAK);
                    } else if (this.getSelf().fallDistance > 3){
                        done=true;
                        setSkillIcon(context, x, y, 3, StandIcons.THE_WORLD_FALL_CATCH, PowerIndex.SKILL_EXTRA);
                    }
                }
            }
            if (!done){
                setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_WORLD, PowerIndex.SKILL_3_SNEAK);
            }
        } else {


            //setSkillIcon(context, x, y, 1, StandIcons.THE_WORLD_ASSAULT, PowerIndex.SKILL_1);

            /*If it can find a mob to grab, it will*/
            Entity targetEntity = MainUtil.getTargetEntity(this.getSelf(),2.1F);
            if (targetEntity != null && canGrab(targetEntity)) {
                setSkillIcon(context, x, y, 2, StandIcons.THE_WORLD_GRAB_MOB, PowerIndex.SKILL_2);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.THE_WORLD_GRAB_BLOCK, PowerIndex.SKILL_2);
            }


            if (((StandUser)this.getSelf()).roundabout$getLeapTicks() > -1 && !this.getSelf().onGround() && canStandRebound()) {
                setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_REBOUND_WORLD, PowerIndex.SKILL_3_SNEAK);
            } else {
                if (!(((StandUser)this.getSelf()).roundabout$getLeapTicks() > -1) && !this.getSelf().onGround() && canVault()) {
                    setSkillIcon(context, x, y, 3, StandIcons.THE_WORLD_LEDGE_GRAB, PowerIndex.SKILL_3_SNEAK);
                } else if (!this.getSelf().onGround() && this.getSelf().fallDistance > 3){
                    setSkillIcon(context, x, y, 3, StandIcons.THE_WORLD_FALL_CATCH, PowerIndex.SKILL_EXTRA);
                } else {
                    setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
                }
            }
        }

        if (((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf())) {
            setSkillIcon(context, x, y, 4, StandIcons.THE_WORLD_TIME_STOP_RESUME, PowerIndex.NO_CD);
        } else if (this.getSelf().isCrouching()){
            setSkillIcon(context, x, y, 4, StandIcons.THE_WORLD_TIME_STOP_IMPULSE, PowerIndex.SKILL_4);
        } else {
            setSkillIcon(context, x, y, 4, StandIcons.THE_WORLD_TIME_STOP, PowerIndex.SKILL_4);
        }
    }

    protected void clampRotation(Entity $$0) {
        $$0.setYBodyRot(this.getSelf().getYRot());
        float $$1 = Mth.wrapDegrees($$0.getYRot() - this.getSelf().getYRot());
        float $$2 = Mth.clamp($$1, -105.0F, 105.0F);
        $$0.yRotO += $$2 - $$1;
        $$0.setYRot($$0.getYRot() + $$2 - $$1);
        $$0.setYHeadRot($$0.getYRot());
    }

    public static final byte DODGE_NOISE = 19;

}
