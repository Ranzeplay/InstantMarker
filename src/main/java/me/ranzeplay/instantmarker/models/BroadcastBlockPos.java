package me.ranzeplay.instantmarker.models;

import net.minecraft.util.math.BlockPos;

public class BroadcastBlockPos {
    private final int x;
    private final int y;
    private final int z;

    public BroadcastBlockPos(BlockPos pos) {
        x = pos.getX();
        y = pos.getY();
        z = pos.getZ();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public BroadcastBlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
