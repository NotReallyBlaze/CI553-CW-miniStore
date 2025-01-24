package clients.cashier;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockReadWriter;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import java.util.Observable;
import java.util.Observer;


/**
 * View of the model 
 */
public class CashierView implements Observer
{
  
  private static final String CHECK  = "Check";
  private static final String BUY    = "Buy";
  private static final String BOUGHT = "Bought/Pay";

  private final Label      pageTitle  = new Label("Buy Products");
  private final Label      theAction  = new Label(" ");
  private final TextField  theInput   = new TextField();
  private final TextArea   theOutput  = new TextArea();
  private final ScrollPane theSP      = new ScrollPane(theOutput);
  private final Button     theBtCheck = new Button( CHECK );
  private final Button     theBtBuy   = new Button( BUY );
  private final Button     theBtBought= new Button( BOUGHT );

  private StockReadWriter theStock     = null;
  private OrderProcessing theOrder     = null;
  private CashierController cont       = null;
  
  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   */
          
  public CashierView(  Stage stage,  MiddleFactory mf)
  {
    try                                           // 
    {      
      theStock = mf.makeStockReadWriter();        // Database access
      theOrder = mf.makeOrderProcessing();        // Process order
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }

    // The layout
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10));
    grid.setHgap(10);
    grid.setVgap(10);

    grid.add(pageTitle, 0, 0, 2, 1);     // Adding layout components
    pageTitle.setText("Thank You for Shopping at MiniStore");
    grid.add(theInput, 0, 2, 2, 1);
    grid.add(theBtCheck, 0, 3);
    grid.add(theBtBuy, 0, 4);
    grid.add(theBtBought, 0, 5);
    grid.add(theSP, 1, 3, 2, 3);
    grid.add(theAction, 0, 5, 2, 1);
    Label welcomeLabel = new Label("Welcome");
    grid.add(welcomeLabel, 0, 1, 2, 1);

    // Button event handlers
    theBtCheck.setOnAction(e -> cont.doCheck(theInput.getText()));
    theBtBuy.setOnAction(e -> cont.doBuy());
    theBtBought.setOnAction(e -> cont.doBought());
    theOutput.setEditable(false); // To not be changed
    theOutput.setFont(Font.font("Monospaced", 12)); // Sets the font
    
    // Setting the scene
    Scene scene = new Scene(grid, 400, 300);
    stage.setScene(scene);
    stage.setTitle("Cashier Client");
    stage.show();
    theAction.setVisible(false);

    theInput.requestFocus();                        // Focus is here
  }

  /**
   * The controller object, used so that an interaction can be passed to the controller
   * @param c   The controller
   */

  public void setController( CashierController c )
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
    CashierModel model  = (CashierModel) modelC;
    String      message = (String) arg;
    theAction.setText( message );
    Basket basket = model.getBasket();
    if ( basket == null )
      theOutput.setText( "Customers order" );
    else
      theOutput.setText( basket.getDetails() );
    
    theInput.requestFocus();               // Focus is here
  }

}
