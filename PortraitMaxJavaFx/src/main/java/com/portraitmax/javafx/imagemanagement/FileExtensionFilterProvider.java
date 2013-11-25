package com.portraitmax.javafx.imagemanagement;

import javafx.stage.FileChooser;

import java.util.Arrays;
import java.util.List;

public class FileExtensionFilterProvider {
  private final List<FileChooser.ExtensionFilter> extensionFilters;

  public FileExtensionFilterProvider() {
    extensionFilters = Arrays.asList( new FileChooser.ExtensionFilter[] {
      new FileChooser.ExtensionFilter( "Image Files", "*.jpg", "*.png", "*.psd" ) } );
  }

  public List<FileChooser.ExtensionFilter> getExtensionFilters() { return extensionFilters; }
}
