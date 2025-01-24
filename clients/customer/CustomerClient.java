package clients.customer;

import clients.customer.CustomerController;
import clients.customer.CustomerModel;
import clients.customer.CustomerView;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javafx.stage.Stage;
import javafx.application.Application;

/**
 * The standalone Customer Client
 */
public class CustomerClient extends Application
{
  public static void main (String args[])
  {
    launch(args);
  //String stockURL = args.length < 1         // URL of stock R
  //                ? Names.STOCK_R           //  default  location
  //                : args[0];                //  supplied location
    
  //RemoteMiddleFactory mrf = new RemoteMiddleFactory();
  //mrf.setStockRInfo( stockURL );
  //displayGUI(mrf);                          // Create GUI
  }
   
  @Override
  public void start(Stage primaryStage)
  {
    String stockURL = getParameters().getRaw().size() < 1
                      ? Names.STOCK_R
                      : getParameters().getRaw().get(0);

    RemoteMiddleFactory mrf = new RemoteMiddleFactory();
    mrf.setStockRInfo( stockURL );
    displayGUI(primaryStage, mrf);
  }

  private static void displayGUI(Stage window, MiddleFactory mf)
  {   
    CustomerModel model = new CustomerModel(mf);
    CustomerView  view  = new CustomerView( window, mf);
    CustomerController cont  = new CustomerController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model
  }
}
