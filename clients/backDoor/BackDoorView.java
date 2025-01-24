package clients.backDoor;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import middle.MiddleFactory;
import middle.StockReadWriter;

import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 */

public class BackDoorView extends Region implements Observer
{
  private static final String RESTOCK  = "Add";
  private static final String CLEAR    = "Clear";
  private static final String QUERY    = "Query";
 
  private static final int H = 300;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels

  private final Label pageTitle = new Label("Staff check and manage stock");
  private final Label theAction = new Label(" ");
  private final TextField theInput = new TextField();
  private final TextField theInputNo = new TextField();
  private final TextArea theOutput = new TextArea();  
  private final ScrollPane theSP = new ScrollPane(theOutput);
  private final Button theBtClear = new Button(CLEAR);
  private final Button theBtRStock = new Button(RESTOCK);
  private final Button theBtQuery = new Button(QUERY);
  
  private StockReadWriter theStock     = null;
  private BackDoorController cont= null;

  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  public BackDoorView(  Stage stage, MiddleFactory mf)
  {
    try                                             // 
    {      
      theStock = mf.makeStockReadWriter();          // Database access
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }

    // Set up the VBox layout
    //VBox root = new VBox(10);
    //root.setPrefSize(W, H);
    GridPane grid = new GridPane();                 // Layout configuration
    grid.setPadding(new Insets(5, 10, 5, 10));              // Padding around grid
    grid.setHgap(10);                               // Horizontal gap
    grid.setVgap(10);                               // Vertical gap

    grid.add(pageTitle, 1, 0);                      // Title label
    GridPane.setHalignment(pageTitle, HPos.CENTER); // Center the title

    grid.add(theSP, 1, 2, 1, 2);                // Action Label
    theSP.setFitToWidth(true);
    GridPane.setVgrow(theSP, Priority.ALWAYS);

    HBox hbox = new HBox(10);                       // Horizontal box
    hbox.getChildren().addAll(theInput, theInputNo);// Add input fields to hbox
    HBox.setHgrow(theInput, Priority.ALWAYS);
    HBox.setHgrow(theInputNo, Priority.ALWAYS);
    grid.add(hbox, 1, 1);                           // Add hbox to grid

    // Buttons
    grid.add(theBtQuery, 0, 1);                     // Query button
    grid.add(theBtRStock, 0, 2);                    // Restock button
    grid.add(theBtClear, 0, 3);                     // Clear button
    theBtQuery.setPrefWidth(300);                   // Setting width to avoid truncation
    theBtClear.setPrefWidth(300);
    theBtRStock.setPrefWidth(300);
    theBtQuery.setStyle("-fx-padding: 10; -fx-font-size: 14px;"); // Button styling
    theBtRStock.setStyle("-fx-padding: 10; -fx-font-size: 14px;");
    theBtClear.setStyle("-fx-padding: 10; -fx-font-size: 14px;");

    // Output Text Area (with scroll)
    theOutput.setEditable(false);
    theSP.setFitToWidth(true);

    // Button Actions
    theBtQuery.setOnAction(e -> cont.doQuery(theInput.getText()));
    theBtRStock.setOnAction(e -> cont.doRStock(theInput.getText(), theInputNo.getText()));
    theBtClear.setOnAction(e -> cont.doClear());

    // Set the scene and stage
    Scene scene = new Scene(grid, 400, 300);        // Create a scene
    stage.setScene(scene);
    stage.setTitle("Back Door Management MVC");
    stage.show();
    theInput.requestFocus();                        // Focus is here
  }
  
  public void setController( BackDoorController c )
  {
    cont = c;
  }

  /**
   * Update the view, called by notifyObservers(theAction) in model,
   * @param modelC   The observed model
   * @param arg      Specific args 
   */
  @Override
  public void update( Observable modelC, Object arg )  
  {
    BackDoorModel model  = (BackDoorModel) modelC;
    String        message = (String) arg;
    theAction.setText( message );
    
    theOutput.setText( model.getBasket().getDetails() );
    theInput.requestFocus();
  }

}