package com.ultreon.mods.exitconfirmation.config.gui;

import com.ultreon.craft.client.gui.widget.Slider;
import org.checkerframework.common.value.qual.IntRange;

public abstract class ValueSliderButton extends Slider {
    public ValueSliderButton(int value, int min, int max) {
        super(value, min, max);
    }

    public ValueSliderButton(@IntRange(from = 21L) int width, int value, int min, int max) {
        super(width, value, min, max);
    }
}
