public class Mole {

    private int x;
    private int y;
    private boolean clicked;

    public Mole(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void dig(){
        x = (int)(Math.random()*6 + 1) * 60 + 5;
        y = (int)(Math.random()*6 + 1) * 60 + 5;
    }
    
    public boolean getClicked(){
        return clicked;
    }
    public void setClicked(boolean c){
        clicked = c;
    }
}
