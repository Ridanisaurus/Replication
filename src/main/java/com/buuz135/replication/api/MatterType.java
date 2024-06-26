package com.buuz135.replication.api;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

import java.util.function.Supplier;

public enum MatterType implements IMatterType{

    EMPTY("empty", new float[]{01f, 1f, 1f,1f}, 1),
    EARTH("earth", new float[]{95/256f, 209/256f, 77/256f,1f}, 16),
    NETHER("nether", new float[]{173/256f, 3/256f, 32/256f,1f}, 32),
    ORGANIC("organic",  new float[]{209/256f, 152/256f, 77/256f, 1f}, 16),
    ENDER("ender", new float[]{0, 130/256f, 87/256f,1f}, 32),
    METALLIC("metallic", new float[]{0.75f, 0.75f,0.75f,1f}, 128),
    PRECIOUS("precious", new float[]{237/256f, 222/256f, 121/256f,1f}, 128),
    LIVING("living", new float[]{181/256f, 69/256f, 177/256f, 1f}, 32),
    QUANTUM("quantum", () -> {
        if (Minecraft.getInstance().level != null){
            return new float[]{181/256f, 132/256f, 227/256f, Mth.clamp(Minecraft.getInstance().level.random.nextDouble() < 0.95 ? (float) (Math.sin((Minecraft.getInstance().level.getGameTime() % 300) / 30f) + 1.25 )*0.5f : 1f, 0, 1)};
        }
        return new float[]{181/256f, 132/256f, 227/256f,1f};
    }, 64);

    ;

    private final String name;
    private final Supplier<float[]> color;
    private final int max;


    MatterType(String name, float[] color, int max) {
        this(name, () -> color, max);
    }

    MatterType(String name, Supplier<float[]> color, int max){
        this.name = name;
        this.color = color;
        this.max = max;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Supplier<float[]> getColor() {
        return color;
    }

    @Override
    public int getMax() {
        return max;
    }
}
