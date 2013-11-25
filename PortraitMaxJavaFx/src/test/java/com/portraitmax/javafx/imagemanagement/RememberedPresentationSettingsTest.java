package com.portraitmax.javafx.imagemanagement;

import java.io.File;

public class RememberedPresentationSettingsTest {
  private File currentImageDirectory;

  public File getCurrentImageDirectory() { return currentImageDirectory; }

  public void setCurrentImageDirectory( File currentImageDirectory ) {
    this.currentImageDirectory = currentImageDirectory;
  }
}
