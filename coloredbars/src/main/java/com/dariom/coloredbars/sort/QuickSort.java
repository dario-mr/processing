package com.dariom.coloredbars.sort;

import java.util.List;
import java.util.function.Supplier;

public class QuickSort {

  private final Supplier<Boolean> shouldCancel;
  private final Runnable triggerRedraw;

  public QuickSort(Supplier<Boolean> shouldCancel, Runnable triggerRedraw) {
    this.shouldCancel = shouldCancel;
    this.triggerRedraw = triggerRedraw;
  }

  public void quickSort(List<Integer> items, int low, int high) {
    if (shouldCancel.get()) {
      return;
    }

    if (low < high) {
      int pi = partition(items, low, high);
      quickSort(items, low, pi - 1);
      quickSort(items, pi + 1, high);
    }
  }

  private int partition(List<Integer> list, int low, int high) {
    int pivot = list.get(high);
    int i = low - 1;

    for (int j = low; j < high; j++) {
      if (shouldCancel.get()) {
        break;
      }

      if (list.get(j) <= pivot) {
        i++;
        swap(list, i, j);
      }
    }
    swap(list, i + 1, high);
    return i + 1;
  }

  private void swap(List<Integer> list, int i, int j) {
    int temp = list.get(i);
    list.set(i, list.get(j));
    list.set(j, temp);

    // redraw UI
    triggerRedraw.run();
  }

}
