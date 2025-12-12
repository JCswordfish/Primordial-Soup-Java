import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
public class Gene{
    private String name,desc;
    private int cost, mutPts;
    public Gene(String name, int cost, int mutPts, String desc){
        this.name = name;
        this.cost = cost;
        this.mutPts = mutPts;
        this.desc = desc;
    }
    public boolean isAdvanced(){
        return false;
    }
    public String getName(){
        return name;
    }
    public void drawMe(Graphics g, int x, int y){
        g.setColor(Color.WHITE);
        g.fillRect(x,y,80,100);
        g.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.PLAIN, 8);
        g.setFont(font);
        for(int i = 0; i <= desc.length()/16; i++){
            if(i == desc.length()/16){
                g.drawString(desc.substring(16*i),x+5,y+41+8*i);
            } else {
                g.drawString(desc.substring(16*i,16*i+16),x+5,y+41+8*i);
            }
            
        }
        font = new Font("Arial", Font.PLAIN, 12);
        g.setFont(font);
        g.drawString("price " + cost,x+2,y+95);
        g.drawString("MP: " + mutPts,x+47,y+95);
        if(isAdvanced()){
            g.setColor(Color.RED);
        }
        if(name.length() > 10){
            g.drawString(name.substring(0,10),x+10,y+13);
            g.drawString(name.substring(10),x+10,y+28);
        } else {
            g.drawString(name,x+10,y+13);
        }
    }
    public boolean buyGene(Species species){
        if(species.hasGene("flexible")){
            if(species.getBP() >= cost - 1 && !species.hasGene(name)){
                species.addGene(this);
                species.changeBP(-(cost-1));
                return true;
            }
            return false;
        } else {
            if(species.getBP() >= cost && !species.hasGene(name)){
                species.addGene(this);
                species.changeBP(-cost);
                return true;
            }
            return false;
        }
        
    }
    public int getCost(){
        return cost;
    }
    public int getMP(){
        return mutPts;
    }
    public String getReq(){
        return "";
    }
    public String getReq2(){
        return "";
    }
    
}