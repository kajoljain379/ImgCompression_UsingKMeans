package kajol;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class KMeans {
  public static void main( String [] args ) {
  if (args.length < 3) {
System.out.println("Usage: Kmeans <input-image> <k> <output-image>");
return;
}
  try {
    
    BufferedImage originalImage = ImageIO.read( new File((args[0])) );
    int k = Integer.parseInt(args[1]);
    BufferedImage NewJpg = kmeans_helper( originalImage,k );
    ImageIO.write( NewJpg, "jpg", new File((args[2])) );
  }

  catch ( IOException e ) {
    System.out.println( e.getMessage() );
  }
  }

  
  private static BufferedImage kmeans_helper( BufferedImage originalImage, int k ) {
  int w = originalImage.getWidth();
  int h = originalImage.getHeight();
  BufferedImage kmeansImage = new BufferedImage( w, h, originalImage.getType() );
  Graphics2D g = kmeansImage.createGraphics();
  g.drawImage( originalImage, 0, 0, w, h, null );

  // Read rgb values from the image.
  int[] imageRGB = new int[(w*h)];
  int counter = 0;
  for ( int iterator = 0; iterator < w; iterator++ ) {
    for( int innerLooper = 0; innerLooper < h; innerLooper++ ) {
         imageRGB[counter++] = kmeansImage.getRGB(iterator,innerLooper);
    }
  }

  // Call kmeans algorithm: update the rgb values to compress image.
  kmeans( imageRGB,k,w,h );

  // Write the new rgb values to the image.
  counter = 0;
  for( int iterator = 0; iterator < w; iterator++ ) {
    for( int innerLooper = 0; innerLooper < h; innerLooper++ ) {
        kmeansImage.setRGB( iterator, innerLooper, imageRGB[counter++] );
      
    }
  }

  // Return the compressed image
  return kmeansImage;
  }

  //Your k-means code goes here
  // Update the array rgb by assigning each entry in the rgb array to its cluster center
  private static void kmeans( int[] pixels, int k,int w,int h ) {

    int[] previousCentroids = new int[k];   
    int[] currentCentroids = new int[k];   
    int[] noOfPixels = new int[k];  
    int[] noOfRedInCluster = new int[k];   
    int[] noOfGreenInCluster = new int[k]; 
    int[] noOfBlueInCluster = new int[k];  
    int[] clusterAssignment = new int[pixels.length]; 
     
    
    //Dummy variable for testing purpose. Remove it before submitting       

    double maximumDistance = Double.MAX_VALUE;   
    double currentDistance = 0;                   
    int closestCenter = 0;               
    currentCentroids[0]=0;
    currentCentroids[1]=(int)(h*(w-1)+0.3*w);
    currentCentroids[2]=w;
    currentCentroids[3]=h*w;
    currentCentroids[4]=(int)(0.5*w*h+0.5*h);
    currentCentroids[5]=(int)(0.5*w*h); 
    currentCentroids[6]=(int)(0.1*w+0.5*w);
    currentCentroids[7]=(int)(0.1*w+w);
    currentCentroids[8]=(int)(h*w*0.5);
    currentCentroids[9]=(int)(0.5*w*w);
    currentCentroids[10]=(int)(0.5*h*h);
    currentCentroids[11]=(int)(0.5*w*w+0.5*h);
    currentCentroids[12]=(int)(0.15*h+0.5*w);
    currentCentroids[13]=(int)(0.10*h+0.5*h);
    currentCentroids[14]=(int)(5*h+0.5*h);
    currentCentroids[15]=(int)(0.2*w*w+0.5*w);
    currentCentroids[16]=(int)(0.25*w*w+0.25*w);
    currentCentroids[17]=(int)(0.5*w*w+0.5*w);
    currentCentroids[18]=(int)(0.6*w*w+0.5*w);
    currentCentroids[19]=(int)(0.9*w*w+0.5*w);
    
    
   
   
    for ( int iterator = 0; iterator < k; iterator++ ) {
   
     System.out.println( currentCentroids[iterator]);
    }

 
    do {
      for ( int iterator = 0; iterator < currentCentroids.length; iterator++ ) {
        previousCentroids[iterator] = currentCentroids[iterator];
        noOfPixels[iterator] = 0;
        noOfRedInCluster[iterator] = 0;
        noOfGreenInCluster[iterator] = 0;
        noOfBlueInCluster[iterator] = 0;
      }

      for ( int iterator = 0; iterator < pixels.length; iterator++ ) {
        maximumDistance = Double.MAX_VALUE;

        for ( int innerLooper = 0; innerLooper < currentCentroids.length; innerLooper++ ) {
          currentDistance = calculatePixelDistance( pixels[iterator], currentCentroids[innerLooper] );
          if ( currentDistance < maximumDistance ) {
            maximumDistance = currentDistance;
            closestCenter = innerLooper;
          }
        }

        clusterAssignment[iterator] = closestCenter;
        noOfPixels[closestCenter]++;
        noOfRedInCluster[closestCenter] +=   ((pixels[iterator] & 0x00FF0000) >>> 16);
        noOfGreenInCluster[closestCenter] += ((pixels[iterator] & 0x0000FF00) >>> 8);
        noOfBlueInCluster[closestCenter] +=  ((pixels[iterator] & 0x000000FF) >>> 0);
      }

      for ( int iterator = 0; iterator < currentCentroids.length; iterator++ ) {
        int averageOfRed =   (int)((double)noOfRedInCluster[iterator] /   (double)noOfPixels[iterator]);
        int averageOfGreen = (int)((double)noOfGreenInCluster[iterator] / (double)noOfPixels[iterator]);
        int averageOfBlue =  (int)((double)noOfBlueInCluster[iterator] /  (double)noOfPixels[iterator]);

        currentCentroids[iterator] = 
                            ((averageOfRed & 0x000000FF) << 16) |
                            ((averageOfGreen & 0x000000FF) << 8) |
                            ((averageOfBlue & 0x000000FF) << 0);
      }
    } while( !isConverged(previousCentroids, currentCentroids) );

    for ( int iterator = 0; iterator < pixels.length; iterator++ ) {
      pixels[iterator] = currentCentroids[clusterAssignment[iterator]];
    }
  }

  private static boolean isConverged( int[] previousCentroids, int[] currentCentroids ) {
    for ( int iterator = 0; iterator < currentCentroids.length; iterator++ )
      if ( previousCentroids[iterator] != currentCentroids[iterator] )
        return false;

    return true;
  }

  private static double calculatePixelDistance( int pixelA, int pixelB ) {
    int differenceOfRed = ((pixelA & 0x00FF0000) >>> 16) - ((pixelB & 0x00FF0000) >>> 16);
    int differenceOfGreen = ((pixelA & 0x0000FF00) >>> 8)  - ((pixelB & 0x0000FF00) >>> 8);
    int differenceOfBlue = ((pixelA & 0x000000FF) >>> 0)  - ((pixelB & 0x000000FF) >>> 0);
    return Math.sqrt( differenceOfRed*differenceOfRed + differenceOfGreen*differenceOfGreen + differenceOfBlue*differenceOfBlue );
  }
}
