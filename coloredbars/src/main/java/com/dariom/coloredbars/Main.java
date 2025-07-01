package com.dariom.coloredbars;

import java.net.URISyntaxException;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.sound.SoundFile;

public class Main extends PApplet {

  // TODO clean up pde files

  // colors generation
  private final int BASE_COLOR = color(255, 0, 0);
  private final float GRADIENT_STEP = 1;

  // misc
  // TODO replace all ArrayList with List interface
  ArrayList<Integer> colors;
  float barWidth;
  SoundFile sortSound;
  SortType sortToRun = SortType.NONE;

  // visible between threads
  // TODO public getters
  volatile boolean needsRedraw = false;
  volatile boolean stopSorting = false;
  volatile boolean isSorting = false;

  // buttons size and position
  int shuffleX = 10, shuffleY = 10, shuffleW = 100, shuffleH = 30;
  int quicksortX = 10, quicksortY = 70, quicksortW = 100, quicksortH = 30;
  int bubblesortX = 10, bubblesortY = 110, bubblesortW = 100, bubblesortH = 30;
  int quitX = 10, quitY, quitW = 100, quitH = 30;

  private ColorGenerator colorGenerator;
  private BarsDrawer barsDrawer;
  private GuiDrawer guiDrawer;
  private QuickSort quickSort;
  private BubbleSort bubbleSort;

  public static void main(String[] args) {
    PApplet.main("com.dariom.coloredbars.Main");
  }

  @Override
  public void settings() {
    fullScreen();
  }

  @Override
  public void setup() {
    colorGenerator = new ColorGenerator(this);
    barsDrawer = new BarsDrawer(this);
    guiDrawer = new GuiDrawer(this);
    quickSort = new QuickSort(this);
    bubbleSort = new BubbleSort(this);

    colors = colorGenerator.generateColors(BASE_COLOR, GRADIENT_STEP);
    barWidth = (float) width / colors.size();

    String sortSoundPath;
    try {
      sortSoundPath = getClass().getResource("/sort.wav").toURI().getPath();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    sortSound = new SoundFile(this, sortSoundPath);
    quitY = height - 50;

    println("Window size: " + width + " x " + height + " px");
    println("Number of generated colors: " + colors.size());
    println("Bar width: " + barWidth + " px");

    barsDrawer.shuffleColorsAndDrawBars(colors, barWidth);
  }

  @Override
  public void draw() {
    if (needsRedraw) {
      barsDrawer.drawBars(colors, barWidth);
      sortSound.play();
      needsRedraw = false;
    }

    guiDrawer.drawButton("Shuffle", shuffleX, shuffleY, shuffleW, shuffleH);
    guiDrawer.drawButton("Quick sort", quicksortX, quicksortY, quicksortW, quicksortH);
    guiDrawer.drawButton("Bubble sort", bubblesortX, bubblesortY, bubblesortW, bubblesortH);
    guiDrawer.drawButton("Quit", quitX, quitY, quitW, quitH);
  }

  @Override
  public void mousePressed() {
    // shuffle button
    if (mouseX > shuffleX && mouseX < shuffleX + shuffleW &&
        mouseY > shuffleY && mouseY < shuffleY + shuffleH) {
      stopSorting = true;
      barsDrawer.shuffleColorsAndDrawBars(colors, barWidth);
    }

    // quicksort button
    if (!isSorting && mouseX > quicksortX && mouseX < quicksortX + quicksortW &&
        mouseY > quicksortY && mouseY < quicksortY + quicksortH) {
      stopSorting = false;
      sortToRun = SortType.QUICK;
      thread("runSortThread");
    }

    // bubblesort button
    if (!isSorting && mouseX > bubblesortX && mouseX < bubblesortX + bubblesortW &&
        mouseY > bubblesortY && mouseY < bubblesortY + bubblesortH) {
      stopSorting = false;
      sortToRun = SortType.BUBBLE;
      thread("runSortThread");
    }

    // quit button
    if (mouseX > quitX && mouseX < quitX + quitW &&
        mouseY > quitY && mouseY < quitY + quitH) {
      exit();
    }
  }

  public void runSortThread() {
    println("Sorting started");
    isSorting = true;

    switch (sortToRun) {
      case QUICK:
        quickSort.quickSort(colors, 0, colors.size() - 1);
        break;
      case BUBBLE:
        bubbleSort.bubbleSort(colors);
        break;
      default:
        break;
    }

    isSorting = false;
    sortToRun = SortType.NONE;
    println("Sorting finished");
  }

}
