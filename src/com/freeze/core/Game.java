package com.freeze.core;

import com.freeze.graphics.Screen;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Bernd Fritz
 */
public class Game
extends Canvas
implements Runnable
{
    public static int WIDTH = 300;
    public static int HEIGHT = WIDTH / 16 * 9;
    public static int SCALE = 3;
    public static String GAMETITLE = "Mafia Game";
    
    private boolean running = false;
    
    private JFrame frame;
    private Thread thread;
    private Screen screen;
    
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    
    
    public Game()
    {
        Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
        setPreferredSize(size);
        
        screen = new Screen(WIDTH, HEIGHT);
        
        frame = new JFrame();
    }
    
    public synchronized void start()
    {
        running = true;
        thread = new Thread(this);
        thread.start();
    }
    
    public synchronized void stop()
    {
        running = false;
        try
        {
            thread.join();
        } catch (InterruptedException ex)
        {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run()
    {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        
        int frames = 0;
        int ticks = 0;
        
        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            
            while(delta >= 1)
            {
                tick();
                ticks++;
                delta--;
            }
            
            render();
            frames++;
            
            if(System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                frame.setTitle(GAMETITLE + " | " + ticks + " ups, " + frames + " fps");
                ticks = 0;
                frames = 0;
            }
        }
    }
    
    public static void main(String[] args)
    {
        Game game = new Game();
        game.frame.setResizable(false);
        game.frame.setTitle(GAMETITLE);
        game.frame.add(game);
        game.frame.pack();
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLocationRelativeTo(null);
        game.frame.setVisible(true);
        
        game.start();
    }

    private void tick()
    {
    }

    private void render()
    {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null)
        {
            createBufferStrategy(3);
            return;
        }
        
        screen.clear();
        screen.render();
        
        for(int i = 0; i < pixels.length; i++)
        {
            pixels[i] = screen.pixels[i];
        }
        
        Graphics g = bs.getDrawGraphics();
        //RENDER TIME

        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        
        ///
        g.dispose();
        bs.show();
    }
}
