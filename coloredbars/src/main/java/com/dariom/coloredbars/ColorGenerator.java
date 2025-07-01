package com.dariom.coloredbars;

import java.util.ArrayList;
import processing.core.PApplet;

public class ColorGenerator {

  private final PApplet sketch;

  public ColorGenerator(PApplet sketch) {
    this.sketch = sketch;
  }

  public ArrayList<Integer> generateColors(int baseColor, float gradientStep) {
    ArrayList<Integer> colors = new ArrayList<>();
    final float baseColorRed = sketch.red(baseColor);
    final float baseColorBlue = sketch.blue(baseColor);

    for (float i = 0; i < 255; i += gradientStep) {
      float green = i;
      colors.add(sketch.color(baseColorRed, green, baseColorBlue));
    }

    return colors;
  }
}
