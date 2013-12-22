package org.loosefx.eventsourcing;

public class AggregateVersion {
  private final int version;

  public AggregateVersion( final int version ) { this.version = version; }

  public int getVersion() { return version; }
}
