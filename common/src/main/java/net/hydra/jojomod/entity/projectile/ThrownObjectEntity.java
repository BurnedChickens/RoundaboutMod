package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.access.IMinecartTNT;
import net.hydra.jojomod.block.BarbedWireBlock;
import net.hydra.jojomod.block.BarbedWireBundleBlock;
import net.hydra.jojomod.block.GasolineBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.item.GlaiveItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.ScissorItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownObjectEntity extends ThrowableItemProjectile {
    private static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(ThrownObjectEntity.class, EntityDataSerializers.ITEM_STACK);
    public final boolean places;

    public ThrownObjectEntity(EntityType<? extends ThrowableItemProjectile> $$0, Level $$1) {
        super($$0, $$1);
        this.places = false;
    }

    public ThrownObjectEntity(LivingEntity living, Level $$1) {
        super(ModEntities.THROWN_OBJECT, living, $$1);
        places = false;
    }

    public ThrownObjectEntity(LivingEntity living, Level $$1, ItemStack itemStack, boolean places) {
        super(ModEntities.THROWN_OBJECT, living, $$1);
        this.entityData.set(ITEM_STACK, itemStack);
        this.places = places;
    }

    public ThrownObjectEntity(Level world, double p_36862_, double p_36863_, double p_36864_, ItemStack itemStack, boolean places) {
        super(ModEntities.THROWN_OBJECT, p_36862_, p_36863_, p_36864_, world);
        this.entityData.set(ITEM_STACK, itemStack);
        this.places = places;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ITEM_STACK, ItemStack.EMPTY);
    }
    @Override
    protected Item getDefaultItem() {
        return this.entityData.get(ITEM_STACK).getItem();
    }

    Direction tempDirection = Direction.UP;

    public boolean tryHitBlock(BlockHitResult $$0, BlockPos pos, BlockState state){

        if ((state.isAir() || state.canBeReplaced()) && !((this.getOwner() instanceof Player &&
                (((Player) this.getOwner()).blockActionRestricted(this.getOwner().level(), pos, ((ServerPlayer)
                        this.getOwner()).gameMode.getGameModeForPlayer()))) ||
                !this.getOwner().level().mayInteract(((Player) this.getOwner()), pos))){

            if (this.getDefaultItem() instanceof BlockItem) {
                Direction direction = this.getDirection();
                if (direction.getAxis() == Direction.Axis.X){
                    direction = direction.getOpposite();
                }
                if (((BlockItem) this.getDefaultItem()).getBlock() instanceof RotatedPillarBlock){
                    direction = $$0.getDirection();
                }

                if (((BlockItem)this.getDefaultItem()).place(new DirectionalPlaceContext(this.level(),
                        pos,
                        direction, this.entityData.get(ITEM_STACK),
                        direction)) != InteractionResult.FAIL){
                    this.tempDirection = direction;
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);


        if (!this.level().isClientSide) {
            if (!this.entityData.get(ITEM_STACK).isEmpty()) {
                BlockPos pos = $$0.getBlockPos().relative($$0.getDirection());
                BlockState state = this.level().getBlockState(pos);
                if (this.entityData.get(ITEM_STACK).getItem() instanceof BlockItem) {
                    if (this.places) {
                        if (tryHitBlock($$0, pos, state)) {
                        } else if (tryHitBlock($$0, pos.relative(this.tempDirection), state)) {
                        } else if (tryHitBlock($$0, pos.above(), state)) {
                        } else {
                            dropItem(pos);
                        }
                    }
                } else {
                    dropItem(pos);
                }
            }
        }
        this.discard();

    }

    public void dropItem(BlockPos pos){
        ItemEntity $$4 = new ItemEntity(this.level(), pos.getX() + 0.5F,
                pos.getY() + 0.25F, pos.getZ() + 0.5F,
                this.entityData.get(ITEM_STACK));
        $$4.setPickUpDelay(40);
        $$4.setThrower(this.getUUID());
        $$4.setDeltaMovement(Math.random() * 0.1F - 0.05F, 0.2F, Math.random() * 0.18F - 0.05F);
        this.level().addFreshEntity($$4);
    }

    public float getDamage(Entity ent){
        float damage = 1;
        if (this.getItem().getItem() instanceof BlockItem){
            float DT =((BlockItem)this.getItem().getItem()).getBlock().defaultDestroyTime();
            if ((((BlockItem) this.getItem().getItem()).getBlock()) instanceof GlassBlock) {
                damage = 12;
            } else if ((((BlockItem) this.getItem().getItem()).getBlock()) instanceof CactusBlock) {
                    damage = 10;
            } else if ((((BlockItem) this.getItem().getItem()).getBlock()) instanceof AnvilBlock) {
                damage = 20.5F;
            } else if (DT <= 0.4) {
                damage = 1;
            } else if (DT <= 1){
                damage = 4;
            } else if (DT <= 1.5){
                damage = 8;
            } else if (DT <= 2){
                damage = 10;
            } else if (DT <= 3){
                damage = 11;
            } else if (DT <= 5){
                damage = 12;
            } else if (DT <= 10){
                damage = 14;
            } else if (DT <= 25){
                damage = 15;
            } else {
                damage = 17;
            }
                    //stone = 2 seconds, obsidian = 50 seconds, dirt = 0.5 seconds
        } else {
            boolean enchant = false;
            if (this.getItem().getItem() instanceof SwordItem){
                damage = (float) (5+ (((SwordItem)this.getItem().getItem()).getDamage()*1.5));
                enchant = true;
            } else if (this.getItem().getItem() instanceof DiggerItem){
                damage = (float) (5+ (((DiggerItem)this.getItem().getItem()).getAttackDamage()*1.5));
                enchant = true;
            } else if (this.getItem().getItem() instanceof GlaiveItem){
                damage = (float) (5+ (((GlaiveItem)this.getItem().getItem()).getDamage()*1.5));
                enchant = true;
            }
            if (enchant){
                if (ent instanceof LivingEntity){
                    damage+= EnchantmentHelper.getDamageBonus(this.entityData.get(ITEM_STACK), ((LivingEntity)ent).getMobType());
                } else {
                    damage+= EnchantmentHelper.getDamageBonus(this.entityData.get(ITEM_STACK), MobType.UNDEFINED);
                }
            }
        }
        return damage;
    }


    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        //
        Entity $$1 = $$0.getEntity();

        Entity $$4 = this.getOwner();

        DamageSource $$5 = ModDamageTypes.of($$1.level(), ModDamageTypes.THROWN_OBJECT, $$4);

        Vec3 DM = $$1.getDeltaMovement();
        if ($$1.hurt($$5, this.getDamage($$1))) {
            if ($$1.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (!this.entityData.get(ITEM_STACK).isEmpty()) {
                int ench = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, this.entityData.get(ITEM_STACK));
                if (ench >= 1) {
                    $$1.setSecondsOnFire((ench) * 4);
                } else if (this.entityData.get(ITEM_STACK).getItem() instanceof FlintAndSteelItem
                        || this.entityData.get(ITEM_STACK).is(Items.MAGMA_BLOCK)
                || this.entityData.get(ITEM_STACK).getItem() instanceof FireChargeItem){
                    $$1.setSecondsOnFire(4);
                } else if (this.entityData.get(ITEM_STACK).is(Items.LAVA_BUCKET)){
                    $$1.setSecondsOnFire(8);
                }


                if (this.getDefaultItem() instanceof GlaiveItem || this.getDefaultItem() instanceof ScissorItem) {
                    MainUtil.makeBleed($$1, 0, 300, this);
                }

                if (this.getDefaultItem() instanceof BlockItem && MainUtil.isThrownBlockItem(this.getDefaultItem())) {
                    Block blk = ((BlockItem) this.getDefaultItem()).getBlock();
                    if (blk instanceof CactusBlock || blk instanceof GlassBlock || blk instanceof BarbedWireBlock
                            || blk instanceof BarbedWireBundleBlock) {
                        MainUtil.makeBleed($$1, 0, 300, this);
                    }

                    SoundEvent SE = (blk).
                            defaultBlockState().getSoundType().getBreakSound();
                    this.playSound(SE, 1.0F, 1.0F);
                    if ($$1 instanceof LivingEntity $$7) {
                        $$7.knockback(0.3f, this.getX() - $$7.getX(), this.getZ() - $$7.getZ());
                    }
                } else {
                    if ($$1 instanceof LivingEntity $$7) {
                        $$7.knockback(0.15f, this.getX() - $$7.getX(), this.getZ() - $$7.getZ());
                    }
                }

                if (this.entityData.get(ITEM_STACK).isDamageableItem()){
                    if (!this.entityData.get(ITEM_STACK).hurt(1,this.level().getRandom(),null)){
                        this.dropItem($$1.getOnPos());
                    }
                }
            }
        } else {
            this.dropItem($$1.getOnPos());
        }
        this.discard();

    }


    public void shootWithVariance(double $$0, double $$1, double $$2, float $$3, float $$4) {
        Vec3 $$5 = new Vec3($$0, $$1, $$2)
                .normalize()
                .add(
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4),
                        this.random.triangle(0.0, 0.13 * (double)$$4)
                )
                .scale((double)$$3);
        this.setDeltaMovement($$5);
        double $$6 = $$5.horizontalDistance();
        this.setYRot((float)(Mth.atan2($$5.x, $$5.z) * 180.0F / (float)Math.PI));
        this.setXRot((float)(Mth.atan2($$5.y, $$6) * 180.0F / (float)Math.PI));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }


    public void shootFromRotationWithVariance(Entity $$0, float $$1, float $$2, float $$3, float $$4, float $$5) {
        float $$6 = -Mth.sin($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        float $$7 = -Mth.sin(($$1 + $$3) * (float) (Math.PI / 180.0));
        float $$8 = Mth.cos($$2 * (float) (Math.PI / 180.0)) * Mth.cos($$1 * (float) (Math.PI / 180.0));
        this.shootWithVariance((double)$$6, (double)$$7, (double)$$8, $$4, $$5);
        Vec3 $$9 = $$0.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add($$9.x, $$0.onGround() ? 0.0 : $$9.y, $$9.z));
    }
}
