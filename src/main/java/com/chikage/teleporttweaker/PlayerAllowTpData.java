package com.chikage.teleporttweaker;

import java.util.UUID;

public class PlayerAllowTpData {
    private boolean canTp;
    private boolean isParmanent;
    private java.util.UUID UUID;

    public PlayerAllowTpData(UUID UUID, boolean canTp, boolean isParmanent) {
        this.UUID = UUID;
        this.canTp = canTp;
        this.isParmanent = isParmanent;
    }

    public boolean canTp() {
        return canTp;
    }

    public boolean isParmanent() {
        return isParmanent;
    }

    public UUID getUUID() {
        return UUID;
    }
}
