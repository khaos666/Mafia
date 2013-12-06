package com.freeze.graphics;

/**
 *
 * @author Bernd Fritz
 */
public class Screen 
{
    private int width;
    private int height;
    
    public int[] pixels;
    
    int time = 0;
    int counter = 0;
    
    public Screen(int width, int height)
    {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
    }
    
    public void clear()
    {
        for(int i = 0; i < pixels.length; i++)
        {
            pixels[i] = 0;
        }
    }
    
    public void render()
    {
        counter++;
        if(counter % 100 == 0)
        {
            time++;
        }
        
        for(int y = 0; y < this.height; y++)
        {
            for(int x = 0; x < this.width; x++)
            {
                pixels[time + time * width] = 0xff00ff;
            }
        }
    }
}
