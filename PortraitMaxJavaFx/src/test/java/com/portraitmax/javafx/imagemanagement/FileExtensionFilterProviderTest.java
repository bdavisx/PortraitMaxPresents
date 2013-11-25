package com.portraitmax.javafx.imagemanagement;

import javafx.stage.FileChooser;

import java.util.Arrays;
import java.util.List;

public class FileExtensionFilterProviderTest {

  @org.junit.Test
  public void getImageFileExtensionFilters() throws Exception {
    FileExtensionFilterProvider provider = new FileExtensionFilterProvider();
    List<FileChooser.ExtensionFilter> filters = provider.getExtensionFilters();
    boolean match = false;
    for( FileChooser.ExtensionFilter filter : filters ) {
      filter.getExtensions().containsAll( Arrays.asList( new String[] { "*.jpg", "*.png", "*.psd" } ) );
    }
  }

}
