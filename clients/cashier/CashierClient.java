package clients.cashier;

import catalogue.*;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


/**
 * The standalone Cashier Client.
 */


public class CashierClient extends Application
{
   public static void main (String args[])
   {
     launch(args); //Starts JavaFX
   }

  @Override
  public void start (Stage primaryStage) { //Where the orignal nain was
    String stockURL = getParameters().getRaw().size() < 1     // URL of stock RW
                    ? Names.STOCK_RW      //  default  location
                    : getParameters().getRaw().get(0);            //  supplied location
    String orderURL = getParameters().getRaw().size() < 2     // URL of order
                    ? Names.ORDER         //  default  location
                    : getParameters().getRaw().get(1);            //  supplied location
                    
  RemoteMiddleFactory mrf = new RemoteMiddleFactory();
  mrf.setStockRWInfo( stockURL );
  mrf.setOrderInfo  ( orderURL );        //
  displayGUI(primaryStage, mrf);         // Create GUI
  }

  private static void displayGUI(Stage window, MiddleFactory mf)
  {         
    CashierModel      model = new CashierModel(mf);
    CashierView       view  = new CashierView( window, mf);
    CashierController cont  = new CashierController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model
    window.show();         // Dispclay Screen
    model.askForUpdate();
  }
}
