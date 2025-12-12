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
import java.awt.event.*;
public class Food{
    int redFood, greenFood, yellowFood;
    Font font;
    public Food(){
        font = new Font("Arial", Font.PLAIN, 20);
        redFood = 2;
        greenFood = 2;
        yellowFood = 2;
    }
    public void eat(Color speciesColor, boolean preference){
        // preference eats more of the left food
        if(speciesColor.equals(Color.RED)){
            if(preference){
                greenFood -= 2;
                yellowFood -= 1;
            } else {
                greenFood -= 1;
                yellowFood -= 2;
            }
        } else if(speciesColor.equals(Color.GREEN)){
            if(preference){
                redFood -= 2;
                yellowFood -= 1;
            } else {
                redFood -= 1;
                yellowFood -= 2;
            }
        } else if(speciesColor.equals(Color.YELLOW)){
            if(preference){
                greenFood -= 1;
                redFood -= 2;
            } else {
                greenFood -= 2;
                redFood -= 1;
            }
        } 
    }
    public void eat(Color speciesColor){
        if(speciesColor.equals(Color.RED)){
            if(greenFood >= 2 && yellowFood >= 1){
                greenFood -= 2;
                yellowFood -= 1;
            } else {
                greenFood -= 1;
                yellowFood -= 2;
            }
        } else if(speciesColor.equals(Color.GREEN)){
            if(yellowFood >= 2 && redFood >= 1){
                yellowFood -= 2;
                redFood -= 1;
            } else {
                yellowFood -= 1;
                redFood -= 2;
            }
        } else if(speciesColor.equals(Color.YELLOW)){
            if(greenFood >= 2 && redFood >= 1){
                greenFood -= 2;
                redFood -= 1;
            } else {
                greenFood -= 1;
                redFood -= 2;
            }
        } 
    }
    public void excrete(Color speciesColor){
        if(speciesColor.equals(Color.RED)){
            redFood += 2;
        } else if(speciesColor.equals(Color.GREEN)){
            greenFood += 2;
        } else if(speciesColor.equals(Color.YELLOW)){
            yellowFood += 2;
        } 
    }
    public int waysToEat(Color speciesColor){
        if(speciesColor.equals(Color.RED)){
            if(greenFood >= 2 && yellowFood >= 2){
                return 2;
            } else if ((greenFood <= 1 && yellowFood <= 1) || (greenFood == 0) || (yellowFood == 0)){
                return 0;
            } else {
                return 1;
            }
        } else if(speciesColor.equals(Color.GREEN)){
            if(redFood >= 2 && yellowFood >= 2){
                return 2;
            } else if ((redFood <= 1 && yellowFood <= 1) || (redFood == 0) || (yellowFood == 0)){
                return 0;
            } else {
                return 1;
            }
        } else if(speciesColor.equals(Color.YELLOW)){
            if(greenFood >= 2 && redFood >= 2){
                return 2; 
            } else if ((greenFood <= 1 && redFood <= 1) || (redFood == 0) || (greenFood == 0)){
                return 0;
            } else {
                return 1;
            }
        }
        return 0;
    }
    public void drawMe(Graphics g, int x, int y){
        g.setFont(font);
        g.setColor(Color.RED);
        g.drawString(redFood + "",x*100+155,y*85+50+82);
        g.setColor(Color.GREEN);
        g.drawString(greenFood + "",x*100+188,y*85+50+82);
        g.setColor(Color.YELLOW);
        g.drawString(yellowFood + "",x*100+221,y*85+50+82);
    }
    public void addFood(int red, int green, int yellow){
        redFood += red;
        greenFood += green;
        yellowFood += yellow;
    }
}