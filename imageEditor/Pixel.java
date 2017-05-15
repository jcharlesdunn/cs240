/**
 * Created by jcdunnMac on 5/3/17.
 */

public class Pixel {
    int r_val;
    int g_val;
    int b_val;
    public Pixel()
    {
        r_val = 0;
        g_val = 0;
        b_val = 0;
    }

    public Pixel(int r_in, int g_in, int b_in)
    {
        r_val = r_in;
        g_val = g_in;
        b_val = b_in;
    }
    public String toString()
    {
        return r_val + "\n" + g_val + "\n" + b_val;
    }

}
