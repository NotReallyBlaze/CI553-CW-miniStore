package clients.packing;

import catalogue.Basket;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import middle.MiddleFactory;
import middle.OrderProcessing;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Packing view.

 */

public class PackingView extends Region implements Observer
{
  private static final String PACKED = "Packed";

  private static final int H = 300;       // Height of window pixels
  private static final int W = 400;       // Width  of window pixels

  private final Label      pageTitle  = new Label();
  private final Label      theAction  = new Label();
  private final TextArea   theOutput  = new TextArea();
  private final ScrollPane theSP      = new ScrollPane();
  private final Button     theBtPack= new Button( PACKED );
 
  private OrderProcessing theOrder     = null;
  
  private PackingController cont= null;

  /**
   * Construct the view
   * @param rpc   Window in which to construct
   * @param mf    Factor to deliver order and stock objects
   * @param x     x-cordinate of position of window on screen 
   * @param y     y-cordinate of position of window on screen  
   */
  public PackingView(  Stage rpc, MiddleFactory mf)
  {
    try                                           // 
    {      
      theOrder = mf.makeOrderProcessing();        // Process order
    } catch ( Exception e )
    {
      System.out.println("Exception: " + e.getMessage() );
    }
    
    VBox root = new VBox(10); // VBox for layout
    root.setPrefSize(W, H);   // Set preferred size for VBox
    
    pageTitle.setText("Packing Bought Order");// Title
    root.getChildren().add(pageTitle);

    // Action label
    theAction.setText("");
    root.getChildren().add(theAction);

    // Scrolling output pane
    theOutput.setEditable(false); // Make the TextArea non-editable
    theSP.setContent(theOutput);  // Add TextArea to ScrollPane
    root.getChildren().add(theSP);

    // Packed Button
    theBtPack.setOnAction(e -> cont.doPacked());  // Event handler for the button
    root.getChildren().add(theBtPack);

    Scene scene = new Scene(root, W, H);  // Set scene with VBox root
    rpc.setScene(scene);                   // Set the scene to the stage
    rpc.setTitle("Packing Client (RMI MVC)");
    //rpc.setX(x);  // Set window X position
    //rpc.setY(y);  // Set window Y position
    rpc.show();   // Show the window
  }
  
  public void setController( PackingController c )
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
	  PackingModel model  = (PackingModel) modelC;
    String        message = (String) arg;
    theAction.setText( message );
    
    Basket basket =  model.getBasket();
    if ( basket != null )
    {
      theOutput.setText( basket.getDetails() );
    } else {
      theOutput.setText("");
    }
  }

}

