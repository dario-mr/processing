package com.dariom.coloredbars.domain;

public record Button(
    String text,
    int x,
    int y,
    int width,
    int height
) {

  public boolean isClicked(int mouseX, int mouseY) {
    return mouseX > x && mouseX < x + width
        && mouseY > y && mouseY < y + height;
  }

}
