package middle;

/**
 * Facade for read access to the stock list.
 * The actual implementation of this is held on the middle tier.
 * The actual stock list is held in a relational DataBase on the 
 * third tier.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

import catalogue.Product;
import debug.DEBUG;
import remote.RemoteStockR_I;

import java.io.ByteArrayInputStream;
import javafx.scene.image.Image;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * Setup connection to the middle tier
 */

public class F_StockR implements StockReader
{
  private RemoteStockR_I aR_StockR   = null;
  private String         theStockURL = null;

  public F_StockR( String url )
  {
    DEBUG.trace("F_StockR: %s", url );
    theStockURL = url;
  }
  
  private void connect() throws StockException
  {
    try                                             // Setup
    {                                               //  connection
      aR_StockR =                                   //  Connect to
        (RemoteStockR_I) Naming.lookup(theStockURL);// Stub returned
    }
    catch ( Exception e )                           // Failure to
    {                                               //  attach to the
      aR_StockR = null;
      throw new StockException( "Com: " + 
                                e.getMessage()  );  //  object
      
    }
  }

  /**
   * Checks if the product exits in the stock list
   * @return true if exists otherwise false
   */

  public synchronized boolean exists( String number )
         throws StockException
  {
    DEBUG.trace("F_StockR:exists()" );
    try
    {
      if ( aR_StockR == null ) connect();
      return aR_StockR.exists( number );
    } catch ( RemoteException e )
    {
      aR_StockR = null;
      throw new StockException( "Net: " + e.getMessage() );
    }
  }

  /**
   * Returns details about the product in the stock list
   * @return StockNumber, Description, Price, Quantity
   */

  public synchronized Product getDetails( String number )
         throws StockException
  {
    DEBUG.trace("F_StockR:getDetails()" );
    try
    {
      if ( aR_StockR == null ) connect();
      return aR_StockR.getDetails( number );
    } catch ( RemoteException e )
    {
      aR_StockR = null;
      throw new StockException( "Net: " + e.getMessage() );
    }
  }
  
  
  
  /**
   * Returns image data of the product in the stock list as a byte array
   * @param number Product number
   * @return Byte array representing the image
   * @throws StockException if there's an issue
   * @throws RemoteException if there's a remote error
   */
  @Override
  public synchronized byte[] getImageData(String pNum) throws StockException, RemoteException {
    DEBUG.trace("F_StockR:getImageData()");
    try {
      if (aR_StockR == null) connect();
      return aR_StockR.getImageData(pNum);
    } catch (RemoteException e) {
      aR_StockR = null;
      throw new StockException("Net: " + e.getMessage());
    }
  }

  /**
   * Returns an Image object from the byte array
   * @param number Product number
   * @return Image representation of the product
   * @throws StockException if there's an issue
   */
  public synchronized Image getImage(String number) throws StockException {
    DEBUG.trace("F_StockR:getImage()");
    try {
      if (aR_StockR == null) connect(); // Ensure connection to remote stock
      byte[] imageData = aR_StockR.getImageData(number); // Fetch image data
      if (imageData != null && imageData.length > 0) {
        // Convert byte[] to an Image
        return new Image(new ByteArrayInputStream(imageData));
      } else {
        throw new StockException("Image data is empty or null for product: " + number);
      }
    } catch (RemoteException e) {
      aR_StockR = null;
      throw new StockException("Net: " + e.getMessage());
    }
  }
}
