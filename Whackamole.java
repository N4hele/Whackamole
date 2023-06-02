import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Whackamole extends JPanel implements MouseListener, MouseMotionListener{
    private JFrame myFrame;
    private int clickedX;
    private int clickedY;
    private int counter;
    private Color white = new Color(255, 255, 255);
    private int X = 60;
    private int Y = 60;
    private boolean enableClick = true;
    private boolean paint = true;
    private int score = 0;
    private boolean poweredUp = false;
    private int powerUpCounter = 3;
    private int life = 5;

    ArrayList<Mole> moles = new ArrayList<Mole>();
    

    public Whackamole(){
        X = (int)(Math.random()*6 + 1) * 60 + 5;
        Y = (int)(Math.random()*6 + 1) * 60 + 5;
        moles.add(new Mole(X, Y));
        myFrame = new JFrame("Whackamole");
        myFrame.add(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        myFrame.setVisible(true);
        myFrame.setSize(500, 530);
        myFrame.setBackground(white);

        
        counter = 2000;
        int moleCounter = 1;
        while(life > 0){
            paint = true;
            if(moleCounter%5 == 0){
                X = (int)(Math.random()*6 + 1) * 60 + 5;
                Y = (int)(Math.random()*6 + 1) * 60 + 5;
                moles.add(new Mole(X, Y));
                counter = 2000;
            }
            System.out.println(counter);
            
            for (Mole m : moles) {
                m.setClicked(false);
                m.dig();
            }
            enableClick = true;
            repaint();
            waitfor(counter+1000);
            paint = false;
            for (Mole m : moles) {
                if(!m.getClicked())
                    life--;
            }
            repaint();
            if(poweredUp)
                powerUpCounter--;
            
            enableClick = false;
            clickedX = -100;
            clickedY = -100;
            waitfor(1500);
            counter = (int)(counter/1.2);
            moleCounter++;
        }
    }
    public void waitfor(int time){
        try {Thread.sleep(time); //milliseconds
            }catch(Exception e){}
    }

    
    
    public void paint(Graphics g) {
        super.paint(g);
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 6; c++) {
                g.fillOval(60*c+60, 60*r+60, 50, 50);
            }
        }

        paintPowerUp(g);
        
        if(paint == true){
            for (Mole m : moles) {
                
                int mX = m.getX();
                int mY = m.getY();
                if((((clickedX>mX && clickedX <(mX+40))&&(clickedY>mY && clickedY <(mY+40))) || m.getClicked())){
                    g.setColor(Color.RED);
                    g.fillRect(mX, mY, 40, 40);
                    if(!m.getClicked())
                        score++;
                    m.setClicked(true);
                }
                else {
                    g.setColor(Color.YELLOW);
                    g.fillRect(mX, mY, 40, 40);
                }
            }
        }
        g.setColor(Color.RED);
        Font f = new Font ("Ariel", 1, 40);
        g.setFont(f);
        g.drawString("Score: " + score, 65, 45);
        g.drawString("Life: " + life, 270, 45);
        if(life<1){
            g.setColor(Color.black);
            g.fillRect(-100, -100, 600, 600);
            g.setColor(Color.red);
            g.setFont(new Font ("Ariel", 1, 60));
            g.drawString("GAME OVER", 50, 250);
        }
    }

    public void paintPowerUp(Graphics g){
        Font f1 = new Font ("Ariel", 1, 30);
        Font f2 = new Font ("Ariel", 1, 25);
        if(powerUpCounter < 1){
            g.setColor(Color.black);
            g.drawRoundRect(62, 430, 350, 35,20,20);
            g.setFont(f1);
            g.drawString("PowerUp Used", 130, 458);
        }else if(poweredUp){
            g.setColor(Color.red);
            g.fillRoundRect(62, 430, 350, 35,20,20);
            g.setColor(Color.black);
            g.drawRoundRect(62, 430, 350, 35,20,20);
            g.setFont(f2);
            g.drawString("Deactivating in: " + powerUpCounter + " rounds", 90, 457);
        }else{
            g.setColor(Color.yellow);
            g.fillRoundRect(62, 430, 350, 35,20,20);
            g.setColor(Color.black);
            g.drawRoundRect(62, 430, 350, 35,20,20);
            g.setFont(f1);
            g.drawString("Drag Mode Power-Up", 85, 458);
        }
    }

    public boolean checkClick(){
        for (Mole m : moles) {
            int mX = m.getX();
            int mY = m.getY();
            if((clickedX>mX && clickedX <(mX+40))&&(clickedY>mY && clickedY <(mY+40))){
                return true;
            }
        }
        return false;
    }

    public void mouseClicked (MouseEvent event) {
        if(life>0){
            int cX = event.getX();
            int cY = event.getY();
            if((cX>62 && cX<(62+350))&&(cY>430 && cY <(430+35)) && poweredUp == false){
                poweredUp = true;
                repaint();
            }
        }
    }

    public void mouseMoved(MouseEvent event){
    }

    public void mouseDragged(MouseEvent event){
        if(poweredUp && powerUpCounter > 0 && enableClick){
            clickedX = event.getX();
            clickedY = event.getY();
            if(checkClick())
                repaint();
        } 
    }
  
    public void mouseReleased (MouseEvent event) {
    }
    
    public void mousePressed (MouseEvent event) {
        if(enableClick){
            clickedX = event.getX();
            clickedY = event.getY();
            if((clickedX>62 && clickedX <(62+350))&&(clickedY>430 && clickedY <(430+35)) && poweredUp == false){
                poweredUp = true;
                repaint();
            }
            if(checkClick())
                repaint();
        }
    }
    
    public void mouseEntered (MouseEvent event) {
        
    }
    
    public void mouseExited (MouseEvent event) {
    }
}