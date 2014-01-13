package com.portraitmax.javafx.imagemanagement;

import javafx.stage.FileChooser;

import java.util.Arrays;
import java.util.List;

public class FileExtensionFilterProviderTest {

  @org.junit.Test
  public void getImageFileExtensionFilters() throws Exception {
    final FileExtensionFilterProvider provider = new FileExtensionFilterProvider();
    final List<FileChooser.ExtensionFilter> filters = provider.getExtensionFilters();
    final boolean match = false;
    for( final FileChooser.ExtensionFilter filter : filters ) {
      filter.getExtensions().containsAll( Arrays.asList( new String[] { "*.jpg", "*.png", "*.psd" } ) );
    }
  }

}
