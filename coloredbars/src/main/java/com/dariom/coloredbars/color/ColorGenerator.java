package com.dariom.coloredbars.color;

import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;

public class ColorGenerator {

  private final PApplet sketch;

  public ColorGenerator(PApplet sketch) {
    this.sketch = sketch;
  }

  public List<Integer> generateColors(int baseColor, float gradientStep) {
    final List<Integer> colors = new ArrayList<>();
    final float baseColorRed = sketch.red(baseColor);
    final float baseColorBlue = sketch.blue(baseColor);

    for (float i = 0; i < 255; i += gradientStep) {
      colors.add(sketch.color(baseColorRed, i, baseColorBlue));
    }

    return colors;
  }
}
