package clients.packing;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;


/**
 * The standalone warehouse Packing Client. warehouse staff to pack the bought order
 * @author  Mike Smith University of Brighton
 * @version 2.0
 * @author  Shine University of Brighton
 * @version year 2024
 */
public class PackingClient extends Application
{
  public static void main (String args[])
  {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage)
  {
    String stockURL = getParameters().getRaw().size() < 1     // URL of stock RW
                     ? Names.STOCK_RW      //  default  location
                     : getParameters().getRaw().get(0);            //  supplied location
     String orderURL = getParameters().getRaw().size() < 2     // URL of order
                     ? Names.ORDER         //  default  location
                     : getParameters().getRaw().get(1);            //  supplied location
     
    RemoteMiddleFactory mrf = new RemoteMiddleFactory();
    mrf.setStockRWInfo( stockURL );
    mrf.setOrderInfo  ( orderURL );        //
    displayGUI(mrf, primaryStage);                       // Create GUI;
  }

  public static void displayGUI(MiddleFactory mf, Stage window)
  {     
     
    VBox root = new VBox(10);
    
    

    PackingModel      model = new PackingModel(mf);
    PackingView       view  = new PackingView( window, mf);
    PackingController cont  = new PackingController( model, view );
    view.setController( cont );
    root.getChildren().add(view);
    model.addObserver( view );       // Add observer to the model

    Scene scene = new Scene(root, 800, 600);
    window.setScene(scene);
    window.setTitle( "Packing Client (RMI MVC)");
    window.show();         // Display Screen
  }
}