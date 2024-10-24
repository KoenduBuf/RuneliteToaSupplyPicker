package com.example;

public enum Supply {
    NECTAR(27315),
    TEARS(27327),
    AMBROSIA(27347),
    SILK(27323),
    SCARAB(27335),
    SALTS(27343),
    ADRENALINE(27339);

    public final int value;
    Supply(int value) {
        this.value = value;
    }

}
