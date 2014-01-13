package spikes;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class FileChooserManualCheck extends Application {

  private final Desktop desktop = Desktop.getDesktop();

  @Override
  public void start( final Stage stage ) {
    stage.setTitle( "File Chooser Sample" );

//    final FileChooser fileChooser = new FileChooser();
    final DirectoryChooser directoryChooser = new DirectoryChooser();

    final Button openButton = new Button( "Open a Picture..." );
    final Button openMultipleButton = new Button( "Open Pictures..." );

    openButton.setOnAction(
      new EventHandler<ActionEvent>() {
        @Override
        public void handle( final ActionEvent e ) {
          final File file = directoryChooser.showDialog( stage );
          if( file != null ) {
            openFile( file );
          }
        }
      } );


    final GridPane inputGridPane = new GridPane();

    GridPane.setConstraints( openButton, 0, 0 );
    GridPane.setConstraints( openMultipleButton, 1, 0 );
    inputGridPane.setHgap( 6 );
    inputGridPane.setVgap( 6 );
    inputGridPane.getChildren().addAll( openButton, openMultipleButton );

    final Pane rootGroup = new VBox( 12 );
    rootGroup.getChildren().addAll( inputGridPane );
    rootGroup.setPadding( new Insets( 12, 12, 12, 12 ) );

    stage.setScene( new Scene( rootGroup ) );
    stage.show();
  }

  public static void main( final String[] args ) {
    Application.launch( args );
  }

  private void openFile( final File file ) {
    try {
      desktop.open( file );
    } catch( final IOException ex ) {
      Logger.getLogger(
        FileChooserManualCheck.class.getName() ).log(
        Level.SEVERE, null, ex
      );
    }
  }
}
