package net.mcbbs.lh_lshen.chronicler.capabilities.impl;

import net.mcbbs.lh_lshen.chronicler.capabilities.api.ICapabilityStellarisEnergy;
import net.minecraft.nbt.CompoundNBT;

public class CapabilityStellarisEnergy implements ICapabilityStellarisEnergy {
    private int energy;
    private int maxEnergy = 10000;
    private boolean isDirty;
    @Override
    public int getEnergyPoint() {
        return energy;
    }

    @Override
    public int getEnergyMax() {
        return maxEnergy;
    }

    @Override
    public void setEnergyPoint(int point) {
        this.energy = point;
    }

    @Override
    public void setEnergyMax(int point) {
        this.maxEnergy = point;
    }

    @Override
    public void charge(int point) {
        if (canCharge()) {
            if (this.energy+point<=this.maxEnergy) {
                this.energy += point;
            }else {
                this.energy = maxEnergy;
            }
            this.setDirty(true);
        }
    }

    @Override
    public void cost(int point) {
        if (canCost(point)) {
            this.energy -= point;
            this.setDirty(true);
        }
    }

    @Override
    public boolean canCharge() {
        return this.energy <= this.maxEnergy;
    }

    @Override
    public boolean canCost(int point) {
        return this.energy - point >= 0;
    }

    @Override
    public boolean isDirty() {
        return isDirty;
    }

    @Override
    public void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("energy",this.energy);
        nbt.putInt("max",this.maxEnergy);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.energy = nbt.getInt("energy");
        this.maxEnergy = nbt.getInt("max");
    }
}
