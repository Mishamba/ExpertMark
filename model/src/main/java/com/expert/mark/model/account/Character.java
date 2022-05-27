package com.expert.mark.model.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Character {
    PSYCHIC(90, (86400 * 7) / 3),
    RARE_BUT_PRECISION(90, (86400 * 7) * 3 / 4),
    EXPERIENCED(80, (86400 * 7) / 3),
    FORECAST_MACHINE(70, (86400 * 7) / 2),
    NEWBIE(50, 0);

    private final float minAccuracy;
    private final long maxCreationInterval;

    public static Character provideCharacter(float accuracy, long creationInterval) {
        if (accuracy > PSYCHIC.minAccuracy && creationInterval < PSYCHIC.maxCreationInterval) {
            return PSYCHIC;
        }
        if (accuracy > RARE_BUT_PRECISION.minAccuracy && creationInterval < RARE_BUT_PRECISION.maxCreationInterval) {
            return RARE_BUT_PRECISION;
        }
        if (accuracy > EXPERIENCED.minAccuracy && creationInterval < EXPERIENCED.maxCreationInterval) {
            return RARE_BUT_PRECISION;
        }
        if (accuracy > FORECAST_MACHINE.minAccuracy && creationInterval < FORECAST_MACHINE.maxCreationInterval) {
            return RARE_BUT_PRECISION;
        }
        return NEWBIE;
    }
}
