package com.dariom.coloredbars;

import static com.dariom.coloredbars.domain.SortType.BUBBLE;
import static com.dariom.coloredbars.domain.SortType.QUICK;

import com.dariom.coloredbars.color.ColorGenerator;
import com.dariom.coloredbars.domain.Button;
import com.dariom.coloredbars.domain.SortType;
import com.dariom.coloredbars.draw.BarsDrawer;
import com.dariom.coloredbars.draw.GuiDrawer;
import com.dariom.coloredbars.file.FileLoader;
import com.dariom.coloredbars.sort.BubbleSort;
import com.dariom.coloredbars.sort.QuickSort;
import java.util.List;
import processing.core.PApplet;
import processing.sound.SoundFile;

public class Main extends PApplet {

  // TODO move properties to property file?

  // band colors generation
  private final int BASE_COLOR = color(255, 0, 0);
  private static final float GRADIENT_STEP = 1;

  // re-draw delays
  private static final int BUBBLESORT_DRAW_DELAY_MS = 1;
  private static final int QUICKSORT_DRAW_DELAY_MS = 10;

  // misc
  private List<Integer> colors;
  private float barWidth;
  private SoundFile sortSound;

  // variables visible between threads
  private volatile boolean needsRedraw = false;
  private volatile boolean shouldStopSorting = false;
  private volatile boolean isSorting = false;

  // buttons
  private Button shuffleBtn;
  private Button quickSortBtn;
  private Button bubbleSortBtn;
  private Button quitBtn;

  // constructor injections
  private ColorGenerator colorGenerator;
  private BarsDrawer barsDrawer;
  private GuiDrawer guiDrawer;
  private QuickSort quickSort;
  private BubbleSort bubbleSort;
  private FileLoader fileLoader;

  public static void main(String[] args) {
    PApplet.main("com.dariom.coloredbars.Main");
  }

  @Override
  public void settings() {
    fullScreen();
    pixelDensity(displayDensity());
  }

  @Override
  public void setup() {
    // injection
    colorGenerator = new ColorGenerator(this);
    barsDrawer = new BarsDrawer(this);
    guiDrawer = new GuiDrawer(this);
    quickSort = new QuickSort(
        () -> shouldStopSorting,
        () -> {
          needsRedraw = true;
          delay(QUICKSORT_DRAW_DELAY_MS);
        });
    bubbleSort = new BubbleSort(
        () -> shouldStopSorting,
        () -> {
          needsRedraw = true;
          delay(BUBBLESORT_DRAW_DELAY_MS);
        });
    fileLoader = new FileLoader(this);

    // buttons
    shuffleBtn = new Button("Shuffle", 10, 10, 100, 30);
    quickSortBtn = new Button("Quick sort", 10, 70, 100, 30);
    bubbleSortBtn = new Button("Bubble sort", 10, 110, 100, 30);
    quitBtn = new Button("Quit", 10, height - 50, 100, 30);

    colors = colorGenerator.generateColors(BASE_COLOR, GRADIENT_STEP);
    barWidth = (float) width / colors.size();
    sortSound = fileLoader.loadSound("sort.wav");

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

    guiDrawer.drawButton(shuffleBtn);
    guiDrawer.drawButton(quickSortBtn);
    guiDrawer.drawButton(bubbleSortBtn);
    guiDrawer.drawButton(quitBtn);
  }

  @Override
  public void mousePressed() {
    // shuffle button
    if (shuffleBtn.isClicked(mouseX, mouseY)) {
      shouldStopSorting = true;
      barsDrawer.shuffleColorsAndDrawBars(colors, barWidth);
    }

    // quit button
    if (quitBtn.isClicked(mouseX, mouseY)) {
      exit();
    }

    // quicksort button
    if (!isSorting && quickSortBtn.isClicked(mouseX, mouseY)) {
      sort(QUICK, colors);
    }

    // bubblesort button
    if (!isSorting && bubbleSortBtn.isClicked(mouseX, mouseY)) {
      sort(BUBBLE, colors);
    }
  }

  private void sort(SortType sortType, List<Integer> colors) {
    new Thread(() -> {
      println("Sorting started");
      shouldStopSorting = false;
      isSorting = true;

      switch (sortType) {
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
      println("Sorting finished");
    }).start();
  }

}
