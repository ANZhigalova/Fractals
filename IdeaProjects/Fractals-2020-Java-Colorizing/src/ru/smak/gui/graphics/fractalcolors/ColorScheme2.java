package ru.smak.gui.graphics.fractalcolors;

import java.awt.*;

public class ColorScheme2 implements Colorizer{
    @Override
    public Color getColor(float x) {
        var r = (float)Math.abs(Math.sin(54+17*x)*Math.cos(11-21*(1-x)));
        var g = (float)Math.abs(Math.sin(22-14*x)*Math.sin(45-17*(x)));
        var b = (float)Math.abs(Math.cos(11+5*(1-x)));
        return new Color(r,g,b);
    }
}
