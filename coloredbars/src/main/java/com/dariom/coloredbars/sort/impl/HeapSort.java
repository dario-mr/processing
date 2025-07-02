package com.dariom.coloredbars.sort.impl;

import com.dariom.coloredbars.sort.Sort;
import java.util.List;
import java.util.function.Supplier;

public class HeapSort implements Sort {

  private final Supplier<Boolean> shouldCancel;
  private final Runnable triggerRedraw;

  public HeapSort(Supplier<Boolean> shouldCancel, Runnable triggerRedraw) {
    this.shouldCancel = shouldCancel;
    this.triggerRedraw = triggerRedraw;
  }

  @Override
  public void sort(List<Integer> items) {
    if (items == null || items.size() <= 1) {
      return;
    }

    int n = items.size();

    // Build max heap
    for (int i = n / 2 - 1; i >= 0; i--) {
      if (shouldCancel.get()) {
        return;
      }
      heapify(items, n, i);
    }

    // Heap sort
    for (int i = n - 1; i > 0; i--) {
      if (shouldCancel.get()) {
        return;
      }

      // Move current root to end
      swap(items, 0, i);
      triggerRedraw.run();

      // Call max heapify on the reduced heap
      heapify(items, i, 0);
    }
  }

  private void heapify(List<Integer> items, int heapSize, int rootIndex) {
    int largest = rootIndex;
    int left = 2 * rootIndex + 1;
    int right = 2 * rootIndex + 2;

    if (shouldCancel.get()) {
      return;
    }

    if (left < heapSize && items.get(left) > items.get(largest)) {
      largest = left;
    }

    if (right < heapSize && items.get(right) > items.get(largest)) {
      largest = right;
    }

    if (largest != rootIndex) {
      swap(items, rootIndex, largest);
      triggerRedraw.run();

      heapify(items, heapSize, largest);
    }
  }

  private void swap(List<Integer> items, int i, int j) {
    int temp = items.get(i);
    items.set(i, items.get(j));
    items.set(j, temp);
  }
}
