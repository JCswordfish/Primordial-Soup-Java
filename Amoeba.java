import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.Font;
public class Amoeba{
    int damagePoints;
    Color species;
    int x, y;
    public Amoeba(Color species, int x, int y, int damagePoints){
        this.damagePoints = damagePoints;
        this.x = x;
        this.y = y;
        this.species = species;
    }
    public Color getSpecies(){
        return species;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void drawMe(Graphics g, int order){
        g.setColor(species);
        Font font = new Font("Arial", Font.PLAIN, 15);
        g.setFont(font);
        if(order/6 == 0){
            g.fillOval(x*100+150 + (order%3) * 33,y*85+50 + (order/3) * 40,33,33);
            g.setColor(Color.BLACK);
            g.drawString(damagePoints+"",x*100+163 + (order%3) * 33,y*85+72 + (order/3) * 40);
            g.drawOval(x*100+150 + (order%3) * 33,y*85+50 + (order/3) * 40,33,33);
        } else if(order < 9){
            g.fillOval(x*100+150 + ((order - 6)%3) * 33,y*85+50 + ((order - 6)/3) * 40+15,33,33);
            g.setColor(Color.BLACK);
            g.drawString(damagePoints+"",x*100+163 + ((order - 6)%3) * 33,y*85+72 + ((order - 6)/3) * 40+15);
            g.drawOval(x*100+150 + ((order-6)%3) * 33,y*85+50 + ((order-6)/3) * 40+15,33,33);

        }
    }
    public void drawMe(Graphics g, int order, boolean highlight){
        g.setColor(species.darker());
        Font font = new Font("Arial", Font.PLAIN, 15);
        g.setFont(font);
        if(order/6 == 0){
            g.fillOval(x*100+150 + (order%3) * 33,y*85+50 + (order/3) * 40,33,33);
            g.setColor(Color.WHITE);
            g.drawString(damagePoints+"",x*100+163 + (order%3) * 33,y*85+72 + (order/3) * 40);
            g.drawOval(x*100+150 + (order%3) * 33,y*85+50 + (order/3) * 40,33,33);
        } else if(order < 9){
            g.fillOval(x*100+150 + ((order - 6)%3) * 33,y*85+50 + ((order - 6)/3) * 40+15,33,33);
            g.setColor(Color.WHITE);
            g.drawString(damagePoints+"",x*100+163 + ((order - 6)%3) * 33,y*85+72 + ((order - 6)/3) * 40+15);
            g.drawOval(x*100+150 + ((order-6)%3) * 33,y*85+50 + ((order-6)/3) * 40+15,33,33);

        }
    }
    public void drift(Environment env, Species species){
        int tempX = x;
        int tempY = y;
        switch(env.getDirection()){
            case 0:
                y--;
                break;
            case 1:
                x++;
                break;
            case 2:
                y++;
                break;
            case 3:
                x--;
                break;
            case 4:
                break;
        }
        if(!((x == 1 && y == 4) || (0 <= x && x <= 4 && 0 <= y && y <= 3))){
            x = tempX;
            y = tempY;
            if(species.hasGene("Rebound")){
                switch(env.getDirection()){
                    case 0:
                        y++;
                        break;
                    case 1:
                        x--;
                        break;
                    case 2:
                        y--;
                        break;
                    case 3:
                        x++;
                        break;
                    case 4:
                        break;
                }
            }
            
        }
    }
    public int move(Species species){
        int tempX = x;
        int tempY = y;
        int rand = (int)(Math.random() * 6);
        switch(rand){
            case 0:
                y--;
                break;
            case 1:
                x++;
                break;
            case 2:
                y++;
                break;
            case 3:
                x--;
                break;
            case 4:
                break;
        }
        if(!((x == 1 && y == 4) || (0 <= x && x <= 4 && 0 <= y && y <= 3))){
            x = tempX;
            y = tempY;
            if(species.hasGene("Rebound")){
                switch(rand){
                    case 0:
                        y++;
                        break;
                    case 1:
                        x--;
                        break;
                    case 2:
                        y--;
                        break;
                    case 3:
                        x++;
                        break;
                    case 4:
                        break;
                }
            }
            
        }
        return rand;
    }
    public void move(int direction, Species species){
        int tempX = x;
        int tempY = y;
        switch(direction){
            case 0:
                y--;
                break;
            case 1:
                x++;
                break;
            case 2:
                y++;
                break;
            case 3:
                x--;
                break;
            case 4:
                break;
        }
        if(!((x == 1 && y == 4) || (0 <= x && x <= 4 && 0 <= y && y <= 3))){
            x = tempX;
            y = tempY;
            if(species.hasGene("Rebound")){
                switch(direction){
                    case 0:
                        y++;
                        break;
                    case 1:
                        x--;
                        break;
                    case 2:
                        y--;
                        break;
                    case 3:
                        x++;
                        break;
                    case 4:
                        break;
                }
            }
            
        }
    }
    public void damage(int points){
        damagePoints += points;
    }
    public int getDP(){
        return damagePoints;
    }
    public void setLocation(int x, int y){
        this.x = x;
        this.y = y;
    }
}
