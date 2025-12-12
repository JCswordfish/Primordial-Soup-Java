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

public class Species{
    private Color speciesColor;
    private ArrayList<Amoeba> amoebas;
    private ArrayList<Gene> geneList; 
    private int score;
    private int bioPoints;
    public Species(Color color, int score){
        speciesColor = color;
        amoebas = new ArrayList<Amoeba>();
        geneList = new ArrayList<Gene>();
        this.score = score;
        bioPoints = 4;
    }
    public Color getColor(){
        return speciesColor;
    }
    public int size(){
        return amoebas.size();
    }
    public void addAmoeba(Amoeba amoeba){
        amoebas.add(amoeba);
    }
    public Amoeba get(int index){
        return amoebas.get(index);
    }
    public String toString(){
        return score + " points, " + bioPoints + " BP";
    }
    public int getBP(){
        return bioPoints;
    }
    public void changeBP(int num){
        bioPoints += num;
    }
    public void driftAmoeba(Environment env, int index){
        amoebas.get(index).drift(env, this);
    }
    public int moveAmoeba(int index){
        return amoebas.get(index).move(this);
    }
    public void drawMe(Graphics g, int x, int y, int order){
        g.setColor(speciesColor);
        String placement = "";
        switch(order){
            case 0:
                placement = "last";
                break;
            case 1:
                placement = "second";
                break;
        }
        if(speciesColor.equals(Color.RED)){
            g.drawString("Red came "+placement+" with " + score + " points",x,y);
        } else if(speciesColor.equals(Color.GREEN)){
            g.drawString("Green came "+placement+" with " + score + " points",x,y);
        } else if(speciesColor.equals(Color.YELLOW)){
            g.drawString("Yellow came "+placement+" with " + score + " points",x,y);
        }
    }
    public void moveAmoeba(int index, int direction){
        amoebas.get(index).move(direction, this);
    }
    public boolean hasGene(String geneName){
        for(Gene each: geneList){
            if(each.getName().equals(geneName)){
                return true;
            } else if (each.isAdvanced()){
                if(each.getReq().equals(geneName) || each.getReq2().equals(geneName)){
                    return true;
                }
            }
        }
        return false;
    }
    public ArrayList<Gene> getGenes(){
        return geneList;
    }
    public void killAmoebas(Food[][] foods){
        for(int i = 0; i < amoebas.size(); i++){
            int x = amoebas.get(i).getX();
            int y = amoebas.get(i).getY();
            if((amoebas.get(i).getDP() >= 2 && !(hasGene("Life Expectancy"))) || (amoebas.get(i).getDP() >= 3 && hasGene("Life Expectancy"))){
                if(speciesColor.equals(Color.RED)){
                    foods[y][x].addFood(2,2,2);
                } else if(speciesColor.equals(Color.GREEN)){
                    foods[y][x].addFood(2,2,2);
                } else if(speciesColor.equals(Color.YELLOW)){
                    foods[y][x].addFood(2,2,2);
                }
                amoebas.remove(i);
                i--;
            }
        }
    }
    public int updateScore(){
        int geneCount = 0;
        int scoreToBeAdded = 0;
        for(int i = 0; i < geneList.size(); i++){
            if(geneList.get(i).isAdvanced()){
                geneCount += 2;
            } else if (!hasGene("Ray Protection")){
                geneCount += 1;
            }
        }
        if(geneCount > 2 && geneCount <= 6){
            scoreToBeAdded += geneCount - 2;
        } else if (geneCount > 6){
            scoreToBeAdded += 4;
        }
        if(amoebas.size() > 2){
            scoreToBeAdded += amoebas.size() - 2;
        }
        if(amoebas.size() > 4){
            scoreToBeAdded += 1;
        }
        score += scoreToBeAdded;
        return scoreToBeAdded;

    }
    public void addScore(int pts){
        score += pts;
    }
    public int getScore(){
        return score;
    }
    public void addGene(Gene gene){
        geneList.add(gene);
    }
    public Gene getGene(int index){
        return geneList.get(index);
    }
    public int geneListLength(){
        return geneList.size();
    }
    public Gene removeGene(int index){
        return geneList.remove(index);
    }
    public boolean removeGene(String name){
        for(int i = 0; i < geneList.size(); i++){
            if(geneList.get(i).getName().equals(name)){
                geneList.remove(i);
                return true;
            }
        }
        return false;
    }
    public boolean eatAmoeba(int index){
        if(hasGene("Armor")){
            return false;
        } else if(hasGene("Escape")){
            amoebas.get(index).move((int)(Math.random() * 6),this);
        } else if(hasGene("Defense")){
            if(Math.random() <= 0.75 && hasGene("Persistence")){
                if (hasGene("More than a Mouthful")) {
                    amoebas.get(index).damage(1);
                } else {
                    amoebas.remove(index);
                }
                return true;
            } else if(Math.random() <= 0.5){
                if (hasGene("More than a Mouthful")) {
                    amoebas.get(index).damage(1);
                } else {
                    amoebas.remove(index);
                }
                return true;
            }
        } else if (hasGene("More than a Mouthful")) {
            amoebas.get(index).damage(1);
            return true;
        } else {
            amoebas.remove(index);
            return true;
        }
        return false;
        
    }
    public boolean aggressiveEatAmoeba1(int index){
        if(hasGene("Armor")){
            return false;
        } else if(hasGene("Escape")){
            return false;
        } else if(hasGene("Defense")){
            if(Math.random() <= 0.75 && hasGene("Persistence")){
                if (hasGene("More than a Mouthful")) {
                    amoebas.get(index).damage(1);
                } else {
                    amoebas.remove(index);
                }
                return true;
            } else if(Math.random() <= 0.5){
                if (hasGene("More than a Mouthful")) {
                    amoebas.get(index).damage(1);
                } else {
                    amoebas.remove(index);
                }
                return true;
            }
        } else if (hasGene("More than a Mouthful")) {
            amoebas.get(index).damage(1);
            return true;
        } else {
            amoebas.remove(index);
            return true;
        }
        return false;
        
    }
    public boolean aggressiveEatAmoeba(int index){
        if(hasGene("Armor")){
            amoebas.get(index).damage(1);
            return true;
        } else if(hasGene("Defense")){
            if(Math.random() <= 0.5){
                if (hasGene("More than a Mouthful")) {
                    amoebas.get(index).damage(1);
                } else {
                    amoebas.remove(index);
                }
                return true;
            }
        } else if (hasGene("More than a Mouthful")) {
            amoebas.get(index).damage(1);
            return true;
        } else {
            amoebas.remove(index);
            return true;
        }
        return false;
        
    }
    
    public boolean camoEatAmoeba(int index){
        if(hasGene("Armor")){
            return false;
        } else {
            amoebas.remove(index);
            return true;
        }
        
    }
    public boolean camoAggressiveEatAmoeba(int index){
        if(hasGene("Armor")){
            amoebas.get(index).damage(1);
            return true;
        } else {
            amoebas.remove(index);
            return true;
        }
    }
    public int getMPs(){
        
        int total = 0;
        for(Gene each:geneList){
            total += each.getMP();
        }
        return total;
    }
}