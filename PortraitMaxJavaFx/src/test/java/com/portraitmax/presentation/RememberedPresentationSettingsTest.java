package com.portraitmax.presentation;

import java.io.File;

public class RememberedPresentationSettingsTest {
  private File currentImageDirectory;

  public File getCurrentImageDirectory() { return currentImageDirectory; }

  public void setCurrentImageDirectory( final File currentImageDirectory ) {
    this.currentImageDirectory = currentImageDirectory;
  }
}
