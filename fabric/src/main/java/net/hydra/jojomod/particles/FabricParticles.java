package net.hydra.jojomod.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.ModParticles;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class FabricParticles {
    public static final SimpleParticleType HIT_IMPACT = FabricParticleTypes.simple();
    public static final SimpleParticleType BLOOD = FabricParticleTypes.simple();
    public static final SimpleParticleType BLUE_BLOOD = FabricParticleTypes.simple();
    public static final SimpleParticleType ENDER_BLOOD = FabricParticleTypes.simple();
    public static void registerParticles(){
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(Roundabout.MOD_ID, "hit_impact"), HIT_IMPACT);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(Roundabout.MOD_ID, "blood"), BLOOD);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(Roundabout.MOD_ID, "blue_blood"), BLUE_BLOOD);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(Roundabout.MOD_ID, "ender_blood"), ENDER_BLOOD);

        ModParticles.BLOOD = BLOOD;
        ModParticles.BLUE_BLOOD = BLUE_BLOOD;
        ModParticles.ENDER_BLOOD = ENDER_BLOOD;
        ModParticles.HIT_IMPACT = HIT_IMPACT;
    }
}