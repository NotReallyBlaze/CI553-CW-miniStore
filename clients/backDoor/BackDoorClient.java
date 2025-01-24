package clients.backDoor;

import javax.swing.JFrame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

/**
 * The standalone BackDoor Client
 */


public class BackDoorClient extends Application
{
   public static void main (String args[])
   {
      launch(args);
   }
  @Override
    public void start(Stage primaryStage) {
        // Get the stock and order URLs either from arguments or default
        String stockURL = getParameters().getRaw().size() < 1     // URL of stock RW
                ? Names.STOCK_RW      // default location
                : getParameters().getRaw().get(0);            // supplied location
        String orderURL = getParameters().getRaw().size() < 2     // URL of order
                ? Names.ORDER         // default location
                : getParameters().getRaw().get(1);            // supplied location

        // Create RemoteMiddleFactory instance
        RemoteMiddleFactory mrf = new RemoteMiddleFactory();
        mrf.setStockRWInfo(stockURL);
        mrf.setOrderInfo(orderURL);

        // Create and display the GUI
        displayGUI(primaryStage, mrf);
    }
  private static void displayGUI(Stage window, MiddleFactory mf)
  {     
    VBox root = new VBox(10);
    
    window.setTitle( "BackDoor Client (MVC RMI)");
    
    BackDoorModel      model = new BackDoorModel(mf);
    BackDoorView       view  = new BackDoorView( window, mf);
    BackDoorController cont  = new BackDoorController( model, view );
    view.setController( cont );
    Scene scene = new Scene(root, 800, 600);
    root.getChildren().add(view);
    model.addObserver( view );       // Add observer to the model - view is observer, model is Observable
    window.setScene(scene);
    window.show();         // Display Screen
  }
}
