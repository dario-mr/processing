void drawButton(String btnText, int x, int y, int btnWidth, int btnHeight) {
  // rectangle
  strokeWeight(0.8);
  stroke(0);
  fill(200);
  rect(x, y, btnWidth, btnHeight, 5);

  // text
  fill(0);
  textAlign(CENTER, CENTER);
  text(btnText, x + btnWidth/2, y + btnHeight/2);
}
