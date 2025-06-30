ArrayList<Integer> generateColors(int baseColor, float gradientStep) {
  ArrayList<Integer> colors = new ArrayList<Integer>();
  final float baseColorRed = red(baseColor);
  final float baseColorBlue = blue(baseColor);

  for (float i = 0; i < 255; i += gradientStep) {
    float green = i;
    colors.add(color(baseColorRed, green, baseColorBlue));
  }

  return colors;
}
