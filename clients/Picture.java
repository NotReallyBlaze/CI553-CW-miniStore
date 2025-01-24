package clients;

import javax.swing.ImageIcon;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * A class to display a picture in a client
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class Picture extends StackPane
{
  private static final long serialVersionUID = 1;
  private int   width      = 260;
  private int   height     = 260;
  private ImageView imageView;

  public Picture()
  {
    setPrefSize( width, height );
    imageView = new ImageView();
    getChildren().add(imageView);
    setupBackground();
  }
  
  public Picture(int aWidth, int aHeight)
  {
    width = aWidth;
    height= aHeight;
    setPrefSize( width, height );
    imageView = new ImageView();
    getChildren().add(imageView);
    setupBackground();
  }

  public void set( String imagePath )
  {
    Image image = new Image( imagePath );
    imageView.setImage( image );
  }
    
  public void clear()
  {
    imageView.setImage(null);                  // clear picture
  }

  private void setupBackground()
  {
    Rectangle background = new Rectangle( width, height, Color.WHITE );
    getChildren().add(0, background);;
  }
}
