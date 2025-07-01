package com.dariom.coloredbars.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import processing.core.PApplet;
import processing.sound.SoundFile;

public class FileLoader {

  private final PApplet sketch;

  public FileLoader(PApplet sketch) {
    this.sketch = sketch;
  }

  public SoundFile loadSound(String filePath) {
    try (InputStream input = getClass().getResourceAsStream("/" + filePath)) {
      if (input == null) {
        throw new RuntimeException("%s not found!".formatted(filePath));
      }

      String baseName;
      String extension;

      int lastSlash = filePath.lastIndexOf('/');
      String fileName = (lastSlash >= 0) ? filePath.substring(lastSlash + 1) : filePath;

      int dotIndex = fileName.lastIndexOf('.');
      if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
        baseName = fileName.substring(0, dotIndex);
        extension = fileName.substring(dotIndex);
      } else {
        baseName = fileName;
        extension = "";
      }

      File tempFile = File.createTempFile(baseName + "_", extension);
      tempFile.deleteOnExit();

      try (OutputStream output = new FileOutputStream(tempFile)) {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) != -1) {
          output.write(buffer, 0, len);
        }
      }

      return new SoundFile(sketch, tempFile.getAbsolutePath());
    } catch (IOException e) {
      throw new RuntimeException("Error loading %s".formatted(filePath), e);
    }
  }

}
