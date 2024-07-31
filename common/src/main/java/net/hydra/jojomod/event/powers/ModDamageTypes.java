package net.hydra.jojomod.event.powers;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ModDamageTypes {
        /**
         * Store the RegistryKey of our DamageType into a new constant called CUSTOM_DAMAGE_TYPE
         * The Identifier in use here points to our JSON file we created earlier.
         */
        public static final ResourceKey<DamageType> STAND = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Roundabout.MOD_ID, "stand"));
        public static final ResourceKey<DamageType> TIME = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Roundabout.MOD_ID, "time"));
        public static final ResourceKey<DamageType> KNIFE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Roundabout.MOD_ID, "knife"));
        public static final ResourceKey<DamageType> MATCH = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Roundabout.MOD_ID, "match"));
        public static final ResourceKey<DamageType> BARBED_WIRE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Roundabout.MOD_ID, "barbed_wire"));
        public static final ResourceKey<DamageType> HEART = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Roundabout.MOD_ID, "heart"));
        public static final ResourceKey<DamageType> FUSION = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Roundabout.MOD_ID, "fusion"));
        public static final ResourceKey<DamageType> STATUE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Roundabout.MOD_ID, "statue"));
        public static final ResourceKey<DamageType> HARPOON = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Roundabout.MOD_ID, "harpoon"));
        public static final ResourceKey<DamageType> GASOLINE_EXPLOSION = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Roundabout.MOD_ID, "gas_explosion"));

        public static DamageSource of(Level world, ResourceKey<DamageType> key, Entity attacker) {
            return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key), attacker, attacker);
        }
        public static DamageSource of(Level world, ResourceKey<DamageType> key, Entity attacker, Entity attacker2) {
                return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key), attacker, attacker2);
        }
        public static DamageSource of(Level world, ResourceKey<DamageType> key) {
                return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
        }
}
