void bubbleSort(ArrayList<Integer> items) {
  int n = items.size();
  boolean swapped;

  for (int i = 0; i < n - 1; i++) {
    swapped = false;

    for (int j = 0; j < n - 1 - i; j++) {
      if (stopSorting) {
        break;
      }

      if (items.get(j) > items.get(j + 1)) {
        int temp = items.get(j);
        items.set(j, items.get(j + 1));
        items.set(j + 1, temp);
        swapped = true;

        // redraw UI
        needsRedraw = true;
        delay(BUBBLESORT_DRAW_DELAY_MS);
      }
    }

    if (!swapped) {
      break;
    }
  }
}
