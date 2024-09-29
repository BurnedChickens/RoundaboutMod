package net.hydra.jojomod.access;

import org.joml.Vector3f;

public interface ILivingEntityAccess {
    public double getLerpX();
    public double getLerpY();
    public double getLerpZ();
    public void setLerp(Vector3f lerp);
    public double getLerpXRot();
    public double getLerpYRot();

    void setAnimStep(float animStep);

    void setAnimStepO(float animStepO);

    float getAnimStep();

    float getAnimStepO();

    int getLerpSteps();

    void setLerpSteps(int lerpSteps);

    void roundaboutPushEntities();

    int roundaboutDecreaseAirSupply(int amt);
}
