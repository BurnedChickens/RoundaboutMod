package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

public class TheWorldEntity extends StandEntity {
    public TheWorldEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }
    @Override
    protected SoundEvent getSummonSound() {
        return ModSounds.WORLD_SUMMON_SOUND_EVENT;
    }

}