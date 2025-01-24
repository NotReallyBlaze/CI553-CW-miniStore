package clients;

import clients.backDoor.BackDoorController;
import clients.backDoor.BackDoorModel;
import clients.backDoor.BackDoorView;
import clients.cashier.CashierController;
import clients.cashier.CashierModel;
import clients.cashier.CashierView;
import clients.customer.CustomerController;
import clients.customer.CustomerModel;
import clients.customer.CustomerView;
import clients.packing.PackingController;
import clients.packing.PackingModel;
import clients.packing.PackingView;
import middle.LocalMiddleFactory;
import middle.MiddleFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Starts all the clients (user interface)  as a single application.
 * Good for testing the system using a single application.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 * @author  Shine University of Brighton
 * @version year-2024
 */

public class Main extends Application
{
  public static void main (String args[])
  {
    String DB_URL = "jdbc:derby:catshop.db;create=true";
        
    try (Connection conn = DriverManager.getConnection(DB_URL);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM ProductTable")) {

        while (rs.next()) {
            String productNo = rs.getString("productNo");
            String description = rs.getString("description");
            byte[] imageData = rs.getBytes("picture");
            float price = rs.getFloat("price");

            // Process the retrieved data
            System.out.println("Product No: " + productNo);
            System.out.println("Description: " + description);
            System.out.println("Image Data Length: " + imageData.length);
            System.out.println("Price: " + price);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    launch(args);
  }
  @Override
  public void start(Stage primaryStage)
  {
    primaryStage.setTitle("Ministore");

    BorderPane root = new BorderPane();
    Scene scene = new Scene(root, 800, 600);

    primaryStage.setScene(scene);
    primaryStage.hide();
    begin();
  }

  /**
   * Starts the system (Non distributed)
   */
  public void begin()
  {
    //DEBUG.set(true); /* Lots of debug info */
    MiddleFactory mlf = new LocalMiddleFactory();  // Direct access
    startCustomerGUI_MVC( mlf );
    startCashierGUI_MVC( mlf );
    startCashierGUI_MVC( mlf ); // you can create multiple clients
    startPackingGUI_MVC( mlf );
    startBackDoorGUI_MVC( mlf );
  }
  
  /**
  * start the Customer client, -search product
  * @param mlf A factory to create objects to access the stock list
  */
  public void startCustomerGUI_MVC(MiddleFactory mlf )
  {
    Stage window = new Stage();
    window.setTitle( "Customer Client MVC");
    
    CustomerModel model      = new CustomerModel(mlf);
    CustomerView view        = new CustomerView(window, mlf);
    CustomerController cont  = new CustomerController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model, ---view is observer, model is Observable
    window.show();         // start Screen
  }

  /**
   * start the cashier client - customer check stock, buy product
   * @param mlf A factory to create objects to access the stock list
   */
  public void startCashierGUI_MVC(MiddleFactory mlf )
  {
    Stage  window = new Stage();
    window.setTitle( "Cashier Client MVC");
    
    CashierModel model      = new CashierModel(mlf);
    CashierView view        = new CashierView( window, mlf);
    CashierController cont  = new CashierController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model
    window.show();         // Make window visible
    model.askForUpdate();            // Initial display
  }

  /**
   * start the Packing client - for warehouse staff to pack the bought order for customer, one order at a time
   * @param mlf A factory to create objects to access the stock list
   */
  
  public void startPackingGUI_MVC(MiddleFactory mlf)
  {
    Stage  window = new Stage();

    window.setTitle( "Packing Client MVC");
    
    PackingModel model      = new PackingModel(mlf);
    PackingView view        = new PackingView( window, mlf);
    PackingController cont  = new PackingController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model
    window.show();         // Make window visible
  }
  
  /**
   * start the BackDoor client - store staff to check and update stock
   * @param mlf A factory to create objects to access the stock list
   */
  public void startBackDoorGUI_MVC(MiddleFactory mlf )
  {
    Stage  window = new Stage();

    window.setTitle( "BackDoor Client MVC");
//  window.setDefaultCloseOperation( Stage.EXIT_ON_CLOSE );
//  Dimension pos = PosOnScrn.getPos();
    
    BackDoorModel model      = new BackDoorModel(mlf);
    BackDoorView view        = new BackDoorView( window, mlf);
    BackDoorController cont  = new BackDoorController( model, view );
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model
    window.show();         // Make window visible
  }
  
}
