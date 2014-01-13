package com.portraitmax.presentation;

import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class FilesAddedToPresentationEvent extends AbstractPresentationEvent {
  private final List<File> filesToAdd;

  public FilesAddedToPresentationEvent( final UUID presentationId, final List<File> filesToAdd ) {
    super( presentationId );
    checkNotNull( filesToAdd );
    this.filesToAdd = filesToAdd;
  }

  public List<File> getFilesToAdd() {
    return filesToAdd;
  }

  @Override
  public boolean equals( final Object o ) {
    if( this == o ) return true;
    if( o == null || getClass() != o.getClass() ) return false;
    final FilesAddedToPresentationEvent that = (FilesAddedToPresentationEvent) o;
    if( !getPresentationId().equals( that.getPresentationId() ) ||
      !filesToAdd.equals( that.filesToAdd ) ) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append( filesToAdd ).append( getPresentationId() ).toHashCode();
  }
}
