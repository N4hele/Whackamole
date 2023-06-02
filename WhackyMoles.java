import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class WhackyMoles extends JPanel implements MouseListener, MouseMotionListener{
    private JFrame myFrame;
    private int clickedX;
    private int clickedY;
    private int powerUpClickX;
    private int powerUpClickY;
    private boolean enableClick = true;
    private boolean paint = true;
    private int score = 0;
    private boolean poweredUp = false;
    private int powerUpCounter;
    private int life;
    private int startCounter = 3;

    ArrayList<Mole> moles = new ArrayList<Mole>();
    

    public WhackyMoles(int initLife, int initDelay, int powerUpDuration, int waveLength){
        if(initLife == 0)
            initLife = 5;
        if(initDelay == 0)
            initDelay = 3000;
        if(powerUpDuration == 0)
            powerUpDuration = 3;
        if(waveLength == 0)
            waveLength = 5;

        life = initLife;
        powerUpCounter = powerUpDuration;

        myFrame = new JFrame("Whackamole");
        myFrame.add(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        myFrame.setVisible(true);
        myFrame.setSize(500, 530);
        myFrame.setBackground(Color.WHITE);

        for (int i = 0; i < 4; i++) {
            waitfor(500);
            startCounter--;
            repaint();
        }

        gameLoop(initDelay, waveLength);
    }

    public void gameLoop(int initDelay, int waveLength){

        int minDelay = 1000;
        int delay = initDelay;
        int moleCounter = 0;
        while(life > 0){
            if(delay < minDelay)
                delay = 1000;
            paint = true;
            if(moleCounter%waveLength == 0 || moleCounter == 0){
                int X = (int)(Math.random()*6 + 1) * 60 + 5;
                int Y = (int)(Math.random()*6 + 1) * 60 + 5;
                moles.add(new Mole(X, Y));
                delay = initDelay;
            }
            for (Mole m : moles) {
                m.setClicked(false);
                m.dig();
            }
            enableClick = true;
            repaint();
            waitfor(delay);
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
            waitfor(1000);
            delay = (int)(delay/1.2);
            moleCounter++;
        }
    }

    public void waitfor(int time){
        try {Thread.sleep(time); //milliseconds
            }catch(Exception e){}
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        g.setFont(new Font ("Ariel", 1, 200));
        g.setColor(Color.black);
        if(startCounter > 0){
            g.drawString(startCounter+"", 185, 300);
        }else if(startCounter==0)
            g.drawString("GO", 85, 300);
        else{    
            paintHoles(g);
            paintPowerUp(g);
            if(paint == true){
                paintMoles(g);
            }
            paintStats(g);
            if(life<1){
                paintGameOver(g);
            }
        } 
    }
    
    public void paintMoles(Graphics g){
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
    public void paintHoles(Graphics g){
        g.setColor(Color.black);
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 6; c++) {
                g.fillOval(60*c+60, 60*r+60, 50, 50);
            }
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

    public void paintStats(Graphics g){
        g.setColor(Color.RED);
        g.setFont(new Font ("Ariel", 1, 40));
        g.drawString("Score: " + score, 65, 45);
        g.drawString("Life: " + life, 270, 45);
    }

    public void paintGameOver(Graphics g){
        g.setColor(Color.black);
        g.fillRect(-100, -100, 600, 600);
        g.setColor(Color.red);
        g.setFont(new Font ("Ariel", 1, 60));
        g.drawString("GAME OVER", 50, 230);
        g.drawString("Score: " + score, 50, 300);
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
    }

    public void mouseMoved(MouseEvent event){
    }

    public void mouseDragged(MouseEvent event){
        powerUpClickX = event.getX();
        powerUpClickY = event.getY();
        if((powerUpClickX>62 && powerUpClickX<(62+350))&&(powerUpClickY>430 && powerUpClickY <(430+35)) && poweredUp == false){
            poweredUp = true;
            repaint();
        }
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
            if(checkClick())
                repaint();
        }
        powerUpClickX = event.getX();
        powerUpClickY = event.getY();
        if((powerUpClickX>62 && powerUpClickX<(62+350))&&(powerUpClickY>430 && powerUpClickY <(430+35)) && poweredUp == false){
            poweredUp = true;
            repaint();
        }
    }
    
    public void mouseEntered (MouseEvent event) {
        
    }
    
    public void mouseExited (MouseEvent event) {
    }
}