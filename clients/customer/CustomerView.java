package clients.customer;

import catalogue.Basket;
import catalogue.BetterBasket;
import clients.Picture;
import middle.MiddleFactory;
import middle.StockReader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import java.util.Observable;
import java.util.Observer;


/**
 * Implements the Customer view.
 */

public class CustomerView implements Observer
{
  class Name                              // Names of buttons
  {
    public static final String CHECK  = "Check";
    public static final String CLEAR  = "Clear";
  }

  private final Label      pageTitle  = new Label("Search products");
  private final Label      theAction  = new Label(" ");
  private final TextField  theInput   = new TextField();
  private final TextArea   theOutput  = new TextArea();
  private final ScrollPane theSP      = new ScrollPane(theOutput);
  private final Button     theBtCheck = new Button( Name.CHECK );
  private final Button     theBtClear = new Button( Name.CLEAR );
  private final ImageView  thePicture = new ImageView();

  private StockReader theStock   = null;
  private CustomerController cont= null;

  /**
   * Construct the view
   * @param rpc   JavaFx Stage
   * @param mf    Factor to deliver order and stock objects
   */
  
  public CustomerView( Stage rpc, MiddleFactory mf)
  {
    try                                             // 
    {      
      theStock  = mf.makeStockReader();             // Database Access
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }



    GridPane grid = new GridPane();                 // Layout configuration
    grid.setPadding(new Insets(10));                // Padding around grid
    grid.setHgap(10);                               // Horizontal gap
    grid.setVgap(10);                               // Vertical gap
    
    // Add components to the layout
    grid.add(pageTitle, 1, 0);                // Title label
    GridPane.setHalignment(pageTitle, HPos.CENTER); // Center the title horizontally
    //grid.add(new Label("Product ID:"), 0, 1);  // Product ID label
    grid.add(theInput, 1, 1);                       // Input field

    grid.add(theSP, 1, 2, 1, 2);
    theSP.setFitToWidth(true);
    

    grid.add(theBtCheck, 0, 1);                     // Check button
    grid.add(theBtClear, 0, 2);                     // Clear button

    thePicture.setFitHeight(80);                    // Picture height
    thePicture.setFitWidth(80);                     // Picture width

    grid.add(thePicture, 0, 3);                     // Picture area
    grid.add(theAction, 1, 4, 1, 1);                // Message area
    GridPane.setHalignment(theAction, HPos.CENTER);

    // Button event handler
    theBtCheck.setOnAction(e -> cont.doCheck(theInput.getText())); // Check action
    theBtClear.setOnAction(e -> cont.doClear());                   // Clear action
    
    theOutput.setEditable(false);             // To not be changed
    thePicture.setFitHeight(80);                    // Picture height
    thePicture.setFitWidth(80);                     // Picture width

    Scene scene = new Scene(grid, 400, 300);        // Create a scene

    // Set the scene to the stage
    rpc.setScene(scene);
    rpc.setTitle("Customer Client MVC");
    rpc.show();
  }

   /**
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */

  public void setController( CustomerController c )
  {
    cont = c;
  }

  /**
   * Update the view
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
  @Override 
  public void update( Observable modelC, Object arg )
  {
    CustomerModel model  = (CustomerModel) modelC;
    String        message = (String) arg;
    theAction.setText( message );

    if (model.getPicture() != null) {
      thePicture.setImage(model.getPicture());
  } else {
      thePicture.setImage(null);
  }

    theOutput.setText( model.getBasket().getDetails() );
    theInput.requestFocus();               // Focus is here
  }

}
