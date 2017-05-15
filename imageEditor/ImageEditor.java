/**
 * Created by jcdunnMac on 5/3/17.
 */

import java.util.Scanner;
import java.io.*;

import java.io.*;


public class ImageEditor {
    public static void main(String[] args) {

        // get file name
        //DO SOME USAGE ERROR CHECKING
        boolean usageError = true;
        if ((args.length == 3) || (args.length == 4)) {
            if (args[0].contains(".ppm") && args[1].contains(".ppm")) {
                if ((args.length == 3) && (args[2].equals("invert") || args[2].equals("grayscale") || args[2].equals("emboss")))
                {
                    usageError = false;
                }
                else if ((args.length == 4) && (args[2].equals("motionblur")))
                {
                    int temp = 0;
                    try {
                        temp = Integer.parseInt(args[3]);
                        usageError = false;
                    } catch (NumberFormatException e) {
                        usageError = true;
                    }

                }
            }
        }
        if (usageError)
        {
            System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
        }
        else // All the arguments are valid
        {
            String fileName = args[0];
            Scanner myscan = null;
            try {
                myscan = new Scanner(new File(fileName)).useDelimiter("(\\s+)(#[^\\n]*\\n)?(\\s*)|(#[^\\n]*\\n)(\\s*)");
            } catch (FileNotFoundException e) {
                System.err.println("An IException was caught!");
                e.printStackTrace(System.out);
            }

            // temp Pixel array
            Pixel[][] pixel_array;

            // Scan header info
            String p3 = myscan.next();
            int w = myscan.nextInt();
            int h = myscan.nextInt();
            int v = myscan.nextInt();

            // Counters for populating the temporary Pixel array
            int w_cnt = 0;
            int h_cnt = 0;

            // instantiate pixel array with proper height and width
            pixel_array = new Pixel[h][w];

            while (myscan.hasNext()) {
                // grab RGB value
                int r = myscan.nextInt();
                int g = myscan.nextInt();
                int b = myscan.nextInt();
                // create a pixel
                Pixel p = new Pixel(r, g, b);

                // put the pixel in the pixel array
                pixel_array[h_cnt][w_cnt] = p;

                // logic for updating where to put pixel in pixel array
                // row major ordering
                w_cnt++;
                if (w_cnt == w) // if reached end of row
                {
                    h_cnt++;   // go down one row
                    w_cnt = 0; // go back to beginning of row
                }
                //System.out.println(p.toString());
            }
            Image img = new Image(pixel_array, h, w);

            String usrOption = args[2];
            System.out.println(usrOption);
            if (usrOption.equals("invert"))
                img.invertImg();
            else if (usrOption.equals("grayscale"))
                img.grayScaleImg();
            else if (usrOption.equals("emboss"))
                img.embossImg();
            else if (usrOption.equals("motionblur")) {
                int n_blr = Integer.parseInt(args[3]);
                img.blurImage(n_blr);
            }


            try {
                PrintWriter writer = new PrintWriter(args[1]);
                writer.println(p3);
                writer.println(w + " " + h);
                writer.println(255);
                for (int i = 0; i < h; i++)
                    for (int j = 0; j < w; j++)
                        writer.println(img.pixel_array[i][j].toString());
                writer.close();
            } catch (IOException e) {
                System.err.println("An OException was caught!");
                e.printStackTrace(System.out);
            }

        }
    }

}
