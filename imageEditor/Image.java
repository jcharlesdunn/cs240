/**
 * Created by jcdunnMac on 5/3/17.
 */

import java.lang.Math.*;

public class Image {
    public Pixel[][] pixel_array;
    int img_width;
    int img_height;
    
    public Image()
    {
        pixel_array = null;
        img_height = 0;
        img_width = 0;
    }
    public Image(Pixel[][] pixel_in, int h_in, int w_in)
    {
        img_height = h_in;
        img_width = w_in;
        pixel_array = new Pixel[img_height][img_width];
        for(int i = 0; i < img_height; i++)
        {
            for(int j = 0; j< img_width; j++)
            {
                pixel_array[i][j] = pixel_in[i][j];
            }
        }
    }

    public void grayScaleImg()
    {
        for(int i = 0; i < img_height; i++)
        {
            for(int j = 0; j< img_width; j++)
            {
                Pixel p = pixel_array[i][j];
                int gray_val = (p.r_val + p.g_val + p.b_val) / 3;
                p.r_val = gray_val;
                p.g_val = gray_val;
                p.b_val = gray_val;
            }
        }
    }
    public void invertImg()
    {
        for(int i = 0; i < img_height; i++)
        {
            for(int j = 0; j< img_width; j++)
            {
                Pixel p = pixel_array[i][j];
                double diff_r = (127.5-p.r_val);
                double diff_g = (127.5-p.g_val);
                double diff_b = (127.5-p.b_val);
                p.r_val = p.r_val + (int)(2* diff_r);
                p.g_val = p.g_val + (int)(2* diff_g);
                p.b_val = p.b_val + (int)(2* diff_b);
            }
        }
    }

    public void embossImg()
    {
        Pixel[][] temp_array;
        temp_array = new Pixel[img_height][img_width];
        for(int i = 0; i < img_height; i++)
        {
            for(int j = 0; j< img_width; j++)
            {
                temp_array[i][j] = new Pixel();
            }
        }
        for(int i = 0; i < img_height; i++)
        {
            for(int j = 0; j< img_width; j++)
            {
                int emb_val = 0;
                Pixel p = pixel_array[i][j];
                if(i == 0 || j == 0)
                {
                    emb_val = 128;
                }
                else {
                    int diff_r = p.r_val - pixel_array[i - 1][j - 1].r_val;
                    int diff_g = p.g_val - pixel_array[i - 1][j - 1].g_val;
                    int diff_b = p.b_val - pixel_array[i - 1][j - 1].b_val;
                    int maxDiff = diff_b;
                    if (java.lang.Math.abs(diff_g) >= java.lang.Math.abs(maxDiff)) {
                        maxDiff = diff_g;
                    }
                    if (java.lang.Math.abs(diff_r) >= java.lang.Math.abs(maxDiff)) {
                        maxDiff = diff_r;
                    }
                    emb_val = 128 + maxDiff;
                    if (emb_val < 0)
                        emb_val = 0;
                    if (emb_val > 255)
                        emb_val = 255;
                }
                temp_array[i][j].r_val = emb_val;
                temp_array[i][j].g_val = emb_val;
                temp_array[i][j].b_val = emb_val;
            }
        }
        for(int i = 0; i < img_height; i++)
        {
            for(int j = 0; j< img_width; j++)
            {
                pixel_array[i][j] = temp_array[i][j];
            }
        }
    }

    public void blurImage(int n_blr) {
        if (n_blr > 0) {
            Pixel[][] temp_array;
            temp_array = new Pixel[img_height][img_width];
            for (int i = 0; i < img_height; i++) {
                for (int j = 0; j < img_width; j++) {
                    temp_array[i][j] = new Pixel();
                }
            }
            for (int i = 0; i < img_height; i++) {
                for (int j = 0; j < img_width; j++) {
                    if ((n_blr + j) > img_width) {
                        // nothing yet
                        // sum up the values;
                        int r_sum = 0;
                        int g_sum = 0;
                        int b_sum = 0;

                        int fudge = img_width - j; //fudge is like the new n_blr number

                        for (int k = 0; k < fudge; k++) {
                            r_sum += pixel_array[i][j + k].r_val;
                            g_sum += pixel_array[i][j + k].g_val;
                            b_sum += pixel_array[i][j + k].b_val;
                        }
                        temp_array[i][j].r_val = r_sum / fudge;//n_blr;
                        temp_array[i][j].g_val = g_sum / fudge;//n_blr;
                        temp_array[i][j].b_val = b_sum / fudge;//n_blr;
                    } else {
                        // sum up the values;
                        int r_sum = 0;
                        int g_sum = 0;
                        int b_sum = 0;

                        for (int k = 0; k < n_blr; k++) {
                            r_sum += pixel_array[i][j + k].r_val;
                            g_sum += pixel_array[i][j + k].g_val;
                            b_sum += pixel_array[i][j + k].b_val;
                        }
                        temp_array[i][j].r_val = r_sum / n_blr;
                        temp_array[i][j].g_val = g_sum / n_blr;
                        temp_array[i][j].b_val = b_sum / n_blr;

                    }
                }
            }
            for (int i = 0; i < img_height; i++) {
                for (int j = 0; j < img_width; j++) {
                    pixel_array[i][j] = temp_array[i][j];
                }
            }
        }
    }
}
