import javax.swing.JPanel;
import java.awt.Dimension;
import java.net.URL;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Game{
    private Species[] players;
    private Food[][] foodGrid;
    private Color choosingFoodColor;
    private boolean isFirstRound, isChoosingMove, viewingGenes;
    private Environment env;
    private int targetRow, targetCol, gameStage, pointBarrier, speciesSelected, amoebaIndexSelected, choosingFoodX, choosingFoodY, speedChoiceCounter, diffMP, tick;
    private boolean drift, randomMove, startGame, moveAgain, keepMove, endCellDivision, restartGame, eatMoreLeft, eatMoreRight,driftAgain, doneBuyingGenes, viewGenes,pointSetter;
    private Font font;
    private ArrayList<Gene> availableGenes;
	public Game(){
        availableGenes = new ArrayList<Gene>();
        addAllGenes(availableGenes);
        isFirstRound = true;
        foodGrid = new Food[5][5];
        for(int i = 0; i < foodGrid.length; i++){
            for(int k = 0; k < foodGrid[i].length; k++){
                foodGrid[i][k] = new Food();
            }
        }

        players = new Species[3];
        players[0] = new Species(Color.RED,1);
        players[1] = new Species(Color.GREEN,2);
        players[2] = new Species(Color.YELLOW,3);
        
        
        
        /*
            will be reordered at scoring based on score
        */

        env = new Environment();



        gameStage = -1;
        



        /*
        0: start menu
        1: 1st round of amoeba placing (ascending score)
        2: 2nd round of amoeba placing
        3: movement and feeding (ascending score)
        4: genetic defects (no order), then set isFirstRound to false
        5: new genes (descending order)
        6: cell division (descending order), everyone gets 10 BP
        7: deaths
        8: scoring (descending order), leapfrogging other scores
        9: game end screen

        */

        // buttons
        drift = false;
        randomMove = false;
        startGame = false;
        moveAgain = false;
        driftAgain = false;
        keepMove = false;
        endCellDivision = false;
        restartGame = false;
        eatMoreLeft = false;
        eatMoreRight = false;
        doneBuyingGenes = false;
        viewGenes = false;
        


        
        
	}
	public static Species[] sortSpecies(Species[] thePlayers){
        Species[] newSpecies = new Species[3];
        int biggestScore = thePlayers[0].getScore();
        int indexOfBiggest = 0;
        for(int i = 1; i < thePlayers.length; i++){
            if(biggestScore < thePlayers[i].getScore()){
                indexOfBiggest = i;
                biggestScore = thePlayers[i].getScore();
            }
        }
        newSpecies[2] = thePlayers[indexOfBiggest];
        int smallestScore = thePlayers[0].getScore();
        int indexOfSmallest = 0;
        for(int i = 1; i < thePlayers.length; i++){
            if(smallestScore > thePlayers[i].getScore()){
                indexOfSmallest = i;
                smallestScore = thePlayers[i].getScore();
            }
        }
        newSpecies[0] = thePlayers[indexOfSmallest];
        for(int i = 0; i < thePlayers.length; i++){
            if(i != indexOfSmallest && i != indexOfBiggest){
                newSpecies[1] = thePlayers[i];
            }
        }
        return newSpecies;
    }
	public int drawScreen(Graphics g){
        g.setColor(Color.GRAY);
        g.fillRect(0,0,1000,600);
        g.setColor(Color.WHITE);
        g.drawRect(250,390,100,85);
        for(int i = 0; i < 5; i++){
            g.drawLine(150,50 + 85 * i, 650, 50+85*i);
        }
        for(int i = 0; i < 6; i++){
            g.drawLine(150+ 100*i,50, 150+100*i,390);
        }
        g.setColor(Color.WHITE);
        g.fillRect(350,220,100,85);
        int counter;
        Amoeba tempAmoeba;
        for(int x = 0; x < 5; x++){
            for(int y = 0; y < 5; y++){
                if(y == 4 && x != 1){
                    break;
                }
                counter = 0;
                for(int i = 0; i < players.length; i++){
                    for(int k = 0; k < players[i].size(); k++){
                        tempAmoeba = players[i].get(k);
                        if(tempAmoeba.getX() == x && tempAmoeba.getY() == y){
                            if(gameStage == 3 && i == speciesSelected && k == amoebaIndexSelected){
                                tempAmoeba.drawMe(g,counter,true);
                            } else if (gameStage == 6 && i == speciesSelected){
                                tempAmoeba.drawMe(g,counter,true);
                            } else {
                                tempAmoeba.drawMe(g,counter);
                            }
                            

                            counter++;
                        }
                    }
                }
                if(counter > 9){
                    g.setColor(Color.WHITE);
                    font = new Font("Arial", Font.PLAIN, 12);
                    g.setFont(font);
                    g.drawString(counter-9 + " more not shown",x*100+151,y*85+70);
                }
                foodGrid[y][x].drawMe(g,x,y);
            }
        } // draws the amoebas
        
        g.setColor(Color.BLUE);
        font = new Font("Arial", Font.PLAIN, 10);
        g.setFont(font);
        g.drawString("Ozone thickness: " + env.getOzoneThickness(),355,260);
        g.drawString("Drift direction: " + env.getDirectionString(),355,240);
        switch(gameStage){
            case 0:
                font = new Font("Arial", Font.PLAIN, 50);
                g.setFont(font);
                g.setColor(Color.RED);
                g.drawString("Primordial Soup",250,100);

                g.setColor(Color.BLACK);
                font = new Font("Arial", Font.PLAIN, 20);
                g.setFont(font);
                g.drawString("Set points needed for a win",300,180);
                
                pointSetter = true;
                startGame = true;
                break;
            case 1:
                if(players[speciesSelected].size() == 1){
                    speciesSelected += 1;
                }
                if(speciesSelected == 3){
                    speciesSelected = 2;
                    gameStage = 2;
                } else {
                    g.setColor(players[speciesSelected].getColor());
                    font = new Font("Arial", Font.PLAIN, 25);
                    g.setFont(font);
                    g.drawString("Place an amoeba on the board!",25,510);
                }
                break;
                
            case 2:
                if(players[speciesSelected].size() == 2){
                    speciesSelected -= 1;
                }
                if(speciesSelected == -1){
                    gameStage = 3;
                    if(env.getDirection() == 5){
                        isChoosingMove = true;
                    }
                    speciesSelected = 0;
                    amoebaIndexSelected = 0;
                    drift = true;
                    randomMove = true;
                } else {
                    g.setColor(players[speciesSelected].getColor());
                    font = new Font("Arial", Font.PLAIN, 25);
                    g.setFont(font);
                    g.drawString("Place an amoeba on the board!",25,510);
                    
                }
                break;
            case 3:
                if(env.getDirection() == 5){
                    drift = false;
                    randomMove = false;
                }
                if((amoebaIndexSelected) >= players[speciesSelected].size()){
                    amoebaIndexSelected = 0;
                    speciesSelected += 1;
                }
                if(speciesSelected >= 3){
                    speciesSelected = 0;
                    isChoosingMove = false;
                    gameStage = 4;
                    drift = false;
                    randomMove = false;
                    env.randomize();
                    diffMP = players[speciesSelected].getMPs();
                } else {
                    g.setColor(players[speciesSelected].getColor());
                    font = new Font("Arial", Font.PLAIN, 25);
                    g.setFont(font);
                    if(eatMoreLeft){
                        g.drawString("Choose between eating more:",25,510);
                        g.drawString("left food",25,535);
                        g.drawString("right food",25,560);
                        if(players[speciesSelected].getColor() == Color.RED){
                            g.setColor(Color.GREEN);
                            g.drawString("2",135,535);
                            g.drawString("1",135,560);
                            g.setColor(Color.YELLOW);
                            g.drawString("1",160,535);
                            g.drawString("2",160,560);
                        } else if (players[speciesSelected].getColor() == Color.GREEN){
                            g.setColor(Color.RED);
                            g.drawString("2",135,535);
                            g.drawString("1",135,560);
                            g.setColor(Color.YELLOW);
                            g.drawString("1",160,535);
                            g.drawString("2",160,560);
                        } else if (players[speciesSelected].getColor() == Color.YELLOW){
                            g.setColor(Color.RED);
                            g.drawString("2",135,535);
                            g.drawString("1",135,560);
                            g.setColor(Color.GREEN);
                            g.drawString("1",160,535);
                            g.drawString("2",160,560);
                        }
                    } else if (isChoosingMove){
                        g.drawString("You can choose where your amoeba moves. Click a valid square",25,510);
                        g.drawString("that you want it to move to.",25,535);
                    } else {
                        g.drawString("Move your amoebas either in the drift direction",25,510);
                        g.drawString("or in a random direction. If there is insufficient food",25,535);
                        g.drawString("on the space it lands, it will take 1 damage.",25,560);
                    }
                    
                }
                break;
            case 4:
                if(isFirstRound){
                    gameStage = 5;
                    speciesSelected = 2;
                    doneBuyingGenes = true;
                    isFirstRound = false;
                } else {
                    viewingGenes = true;
                    int ozone = env.getOzoneThickness();
                    if(diffMP <= ozone){
                        speciesSelected += 1;
                        if(speciesSelected == 3){
                            gameStage = 5;
                            speciesSelected = 2;
                            doneBuyingGenes = true;
                            viewingGenes = false;
                        } else {
                            diffMP = players[speciesSelected].getMPs();
                        }
                    }
                }
                break;
            case 5:
                g.setColor(Color.BLACK);
                g.fillRect(0,0,1000,600);
                if(speciesSelected == -1){
                    gameStage = 6;
                    for(Species each: players){
                        each.changeBP(10);
                    }
                    doneBuyingGenes = false;
                    speciesSelected = 2;
                } else {
                    g.setColor(players[speciesSelected].getColor());
                    font = new Font("Arial", Font.PLAIN, 25);
                    g.setFont(font);
                    g.drawString("Buy genes for BP for your species by clicking on them!",25,545);
                    g.drawString("If your total mutation points exceeds the ozone layer thickness,",25,570);
                    g.drawString("you must remove genes or BP to balance the difference.",25,595);
                    for(int i = 0; i < availableGenes.size(); i++){
                        availableGenes.get(i).drawMe(g,120 + 85*(i%8),105 * (i/8));
                    }
                }
                
                
                

                break;
            case 6:
                endCellDivision = true;
                g.setColor(players[speciesSelected].getColor());
                font = new Font("Arial", Font.PLAIN, 25);
                g.setFont(font);
                g.drawString("Place an amoeba on the board! Max of 7,",25,510);
                g.drawString("and if you have > 2 amoebas they must be placed",25,535);
                g.drawString("next to alive amoebas",25,560);
                break;
            case 7:
                for(int i = 0; i < players.length; i++){
                    players[i].killAmoebas(foodGrid);
                }
                gameStage = 8;
                return 2;
            case 8:
                int newScore;
                int oldScore;
                boolean someoneWins = false;
                for(int i = players.length-1; i >= 0; i--){
                    oldScore = players[i].getScore();
                    newScore = oldScore + players[i].updateScore();
                    //System.out.println(oldScore + " to " + newScore + " index " + i);
                    for(int k = 0; k < players.length; k++){
                        if((i != k) && players[k].getScore() <= newScore && players[k].getScore() > oldScore){ // if isn't comparing to itself and a score is in between old and new score
                            players[i].addScore(1);
                            newScore += 1;
                            //System.out.println("LEAP! over index " + k + " w/ score " + players[k].getScore());
                            k = 0;
                            oldScore = newScore-1;
                        } else {
                            //System.out.println("NO LEAP! over index " + k + " w/ score " + players[k].getScore());
                            
                        }
                    }
                    if(players[i].getScore() >= pointBarrier){
                        someoneWins = true;
                    }
                }
                if(someoneWins){
                    gameStage = 9;
                    players = sortSpecies(players);
                } else {
                    gameStage = 3;
                    if(env.getDirection() == 5){
                        isChoosingMove = true;
                    }
                    speciesSelected = 0;
                    amoebaIndexSelected = 0;
                    players = sortSpecies(players);
                    drift = true;
                    randomMove = true;
                }
                break;
            case 9:
                if(tick < 700){
                    tick += 5;
                }
                g.setColor(Color.BLACK);
                g.fillRect(0,0,1000,600);
                font = new Font("Arial", Font.PLAIN, 40);
                g.setFont(font);
                int bestScore = players[2].getScore();
                Color winColor = players[2].getColor();
                g.setColor(winColor);
                int winX = tick - 500;
                if(winColor.equals(Color.RED)){
                    g.drawString("Red wins with " + bestScore + " points!",winX,320);
                } else if(winColor.equals(Color.GREEN)){
                    g.drawString("Green wins with " + bestScore + " points!",winX,320);
                } else if(winColor.equals(Color.YELLOW)){
                    g.drawString("Yellow wins with " + bestScore + " points!",winX,320);
                }
                font = new Font("Arial", Font.PLAIN, 20);
                g.setFont(font);
                if(tick == 700){
                    for(int i = 1; i >= 0; i--){
                        players[i].drawMe(g,200,380-30*i,i);
                    }
                    return 1;
                }
                break;
        }
        if(viewingGenes && gameStage != 9){
            g.setColor(Color.BLACK);
            g.fillRect(0,0,1000,600);
            for(int k = 0; k < players.length; k++){
                for(int i = 0; i < players[k].geneListLength(); i++){
                    players[k].getGene(i).drawMe(g,120 + 85*i,175 * k + 75);
                }
                font = new Font("Arial", Font.PLAIN, 20);
                g.setFont(font);
                g.setColor(players[k].getColor());
                g.drawString("Genes:",120,50+175*k);
                
            }
            if(gameStage == 4){
                int ozone = env.getOzoneThickness();
                g.setColor(players[speciesSelected].getColor());
                font = new Font("Arial", Font.PLAIN, 25);
                g.setFont(font);
                if(diffMP > ozone){
                    g.drawString("Click to remove genes or BPs to balance the difference of " + (diffMP-ozone),25,560);
                    g.setColor(Color.WHITE);
                    g.fillRect(850,400,120,100);
                    g.setColor(Color.BLACK);
                    font = new Font("Arial", Font.PLAIN, 12);
                    g.setFont(font);
                    g.drawString("Click to remove 1 BP",855,460);
                }
            }
            

        }
        
        if(gameStage > 0 && gameStage < 9){
            font = new Font("Arial", Font.PLAIN, 16);
            g.setFont(font);
            for(int i = 0; i < players.length; i++){
                g.setColor(players[i].getColor());
                g.drawString(players[i].toString(),10,70+40*i);
            }
        }
        if(gameStage == -1){
            font = new Font("Arial", Font.PLAIN, 15);
            g.setFont(font);
            g.setColor(Color.WHITE);
            g.fillRect(0,0,1000,800);
            g.setColor(Color.BLACK);
            String startText = "Primordial Soup is a game in which you control a species of amoebas. At first, you get 2 BP (Biological Points), and place two amoebas on the board. Additionally, 2 food items of each color, represented by the numbers with color on each tile, are placed on each tile. Then, the game starts its first round. In each round, there are 6 phases; in the first, you move amoebas either in the drift direction, or move randomly by spending 1 BP. These amoebas move in ascending score order, meaning the person with least score moves, then the 2nd to least score, and then finally the highest score. Amoebas move individually, and are highlighted when they are going to move. Amoebas need to feed by eating 2 food items of a color not their own, and 1 more from the remaining color that is not their own. If an amoeba cannot eat, it takes 1 DP (Damage Point). On the first round, the second phase, genetic defects, is skipped. Next, the third phase starts: gene buying. In this phase, you can spend BP to buy genes in the gene pool, in descending score order (opposite of ascending). Genes grant bonuses to your entire species, like adding the ability to move twice or the ability to eat amoebas instead of food items. However, beware of stacking up too many mutation points, which will have potentially negative impacts during genetic defects. After all players are done buying genes, the fourth round, cell division, starts. Everyone gets 10 BP to spend on placing more amoebas in descending order, which cost 6 BP each and have some rules specified in that phase. Then, in the fifth phase, all amoebas with 2 DP die, getting removed from the board. Finally, in the 6th phase, scoring happens in descending order. Each player gets score based on amoebas on the board after deaths (1 score per amoeba above 2 amoebas, and 1 extra point if you have at least 5 amoebas), and genes (1 score per gene you have above 2 genes but below 7). Since no two players can share a score, if you pass someone on score, you 'leapfrog' them, essentially getting a bonus score. On rounds other than the first, genetic defection is the second round. If your total mutation points stated on your genes total more than the ozone layer thickness level, you must pay BPs or remove genes to balance the difference. Additionally, a new environment is picked, giving a new drift direction and a new ozone layer thickness.";
            int startTextWidth = 148;
            for(int i = 0; i <= startText.length()/startTextWidth; i++){
                if(i == startText.length()/startTextWidth){
                    g.drawString(startText.substring(startTextWidth*i),5,200+16*i);
                } else {
                    g.drawString(startText.substring(startTextWidth*i,startTextWidth*i+startTextWidth),5,200+16*i);
                }
                
            }
            font = new Font("Arial", Font.PLAIN, 50);
            g.setFont(font);
            g.drawString("CLICK ANYWHERE TO START!",100,500);
            g.setColor(Color.RED);
            g.drawString("Primordial Soup",250,100);
        }

        return 0;
		
		
	}
	public void actionPerformed(String e){
        Species tempSpecies = players[speciesSelected];
		if(e.equals("drift")){
            tempSpecies.driftAmoeba(env,amoebaIndexSelected);
            if(tempSpecies.hasGene("Energy Conservation")){
                tempSpecies.changeBP(1);
            }
            if(tempSpecies.hasGene("Speed")){ 
                drift = false;
                randomMove = false;
                keepMove = true;
                moveAgain = true;
                driftAgain = true;
            } else if(eatFood(tempSpecies,amoebaIndexSelected,foodGrid)){
                amoebaIndexSelected += 1;
                if(env.getDirection() == 5){
                    isChoosingMove = true;
                }
            }
        } else if (e.equals("randomMove") && (tempSpecies.getBP() > 0 || tempSpecies.hasGene("Streamline"))){
            int directionMoved = 0;
            if(tempSpecies.hasGene("Movement 2")){
                isChoosingMove = true;
                drift = false;
                randomMove = false;
                directionMoved = 5;
            } else {
                directionMoved = tempSpecies.moveAmoeba(amoebaIndexSelected);
            }
            if(!tempSpecies.hasGene("Streamline")){
                tempSpecies.changeBP(-1);
            }
            if(directionMoved == 5){
                isChoosingMove = true;
                drift = false;
                randomMove = false;
            } else if(tempSpecies.hasGene("Speed")){
                drift = false;
                randomMove = false;
                keepMove = true;
                moveAgain = true;
                driftAgain = true;
            } else if (tempSpecies.hasGene("Movement 1") && !canEat()){
                int newDirection = 4;
                switch(directionMoved){
                    case 0:
                        newDirection = 2;
                        break;
                    case 1:
                        newDirection = 3;
                        break;
                    case 2:
                        newDirection = 0;
                        break;
                    case 3:
                        newDirection = 1;
                        break;
                    case 4:
                        newDirection = 4;
                        break;
                }
                tempSpecies.moveAmoeba(amoebaIndexSelected,newDirection);
                tempSpecies.moveAmoeba(amoebaIndexSelected,(int)(Math.random() * 5));
                if(eatFood(tempSpecies,amoebaIndexSelected,foodGrid)){
                    amoebaIndexSelected += 1;
                    if(env.getDirection() == 5){
                        isChoosingMove = true;
                    }
                }
            } else if(eatFood(tempSpecies,amoebaIndexSelected,foodGrid)){
                amoebaIndexSelected += 1;
                if(env.getDirection() == 5){
                    isChoosingMove = true;
                }
            }
        } else if (e.equals("moveAgain") && (tempSpecies.getBP() > 0 || tempSpecies.hasGene("Streamline"))){
            int directionMoved = tempSpecies.moveAmoeba(amoebaIndexSelected);
            if(!tempSpecies.hasGene("Streamline")){
                tempSpecies.changeBP(-1);
            }
            if(directionMoved == 5){
                isChoosingMove = true;
                drift = false;
                randomMove = false;
            }
            keepMove = false;
            driftAgain = false;
            moveAgain = false;
            drift = true;
            randomMove = true;
            if(eatFood(tempSpecies,amoebaIndexSelected,foodGrid)){
                speedChoiceCounter = 0;
                amoebaIndexSelected += 1;
                if(env.getDirection() == 5){
                    isChoosingMove = true;
                }
            }
        } else if (e.equals("driftAgain")){
            tempSpecies.driftAmoeba(env,amoebaIndexSelected);
            if(tempSpecies.hasGene("Energy Conservation")){
                tempSpecies.changeBP(1);
            }
            keepMove = false;
            driftAgain = false;
            moveAgain = false;
            drift = true;
            randomMove = true;
            if(eatFood(tempSpecies,amoebaIndexSelected,foodGrid)){
                speedChoiceCounter = 0;
                amoebaIndexSelected += 1;
                if(env.getDirection() == 5){
                    isChoosingMove = true;
                }
            }
        } else if (e.equals("keepMove")){
            keepMove = false;
            driftAgain = false;
            moveAgain = false;
            drift = true;
            randomMove = true;
            if(eatFood(tempSpecies,amoebaIndexSelected,foodGrid)){
                speedChoiceCounter = 0;
                amoebaIndexSelected += 1;
                if(env.getDirection() == 5){
                    isChoosingMove = true;
                }
            }
        } else if (e.equals("endCellDivision")){
            if(speciesSelected > 0){
                speciesSelected -= 1;
            } else {
                gameStage = 7;
                endCellDivision = false;
            }
        } else if (e.equals("restartGame")){
            availableGenes = new ArrayList<Gene>();
            addAllGenes(availableGenes);
            isChoosingMove = false;
            isFirstRound = true;
            foodGrid = new Food[5][5];
            for(int i = 0; i < foodGrid.length; i++){
                for(int k = 0; k < foodGrid[i].length; k++){
                    foodGrid[i][k] = new Food();
                }
            }
            players = new Species[3];
            players[0] = new Species(Color.RED,1);
            players[1] = new Species(Color.GREEN,2);
            players[2] = new Species(Color.YELLOW,3);
            env = new Environment();
            gameStage = 0;
            tick = 0;
            drift = false;
            randomMove = false;
            startGame = true;
            moveAgain = false;
            driftAgain = false;
            keepMove = false;
            endCellDivision = false;
            restartGame = true;
            eatMoreLeft = false;
            eatMoreRight = false;
            doneBuyingGenes = false;
            viewGenes = false;
        } else if (e.equals("eatMoreLeft")){
            foodGrid[choosingFoodY][choosingFoodX].eat(choosingFoodColor,true);
            foodGrid[choosingFoodY][choosingFoodX].excrete(choosingFoodColor);
            speedChoiceCounter = 0;
            amoebaIndexSelected += 1;
            if(env.getDirection() == 5){
                isChoosingMove = true;
            }
            eatMoreLeft = false;
            eatMoreRight = false;
            drift = true;
            randomMove = true;
        } else if (e.equals("eatMoreRight")){
            foodGrid[choosingFoodY][choosingFoodX].eat(choosingFoodColor,false);
            foodGrid[choosingFoodY][choosingFoodX].excrete(choosingFoodColor);
            speedChoiceCounter = 0;
            amoebaIndexSelected += 1;
            if(env.getDirection() == 5){
                isChoosingMove = true;
            }
            eatMoreLeft = false;
            
            eatMoreRight = false;
            drift = true;
            randomMove = true;
        } else if (e.equals("doneBuyingGenes")){
            speciesSelected -= 1;
        } else if (e.equals("viewGenes")){
            if(viewingGenes){
                viewingGenes = false;
            } else {
                viewingGenes = true;
            }
        }
	}
    public void actionPerformed(String e, String pointSetterText){
        if(e.equals("startGame")){
            try{
                if(Integer.parseInt(pointSetterText) > 3){
                    gameStage = 1;
                    speciesSelected = 0;
                    pointBarrier = Integer.parseInt(pointSetterText);
                    startGame = false;
                    pointSetter = false;
                    viewGenes = true;
                }
            } catch (NumberFormatException except){
                
            }
        }
    }
    public void addAllGenes(ArrayList<Gene> geneList){
        geneList.add(new Gene("Movement 1",3,2,"Increased chance to move to food on random move")); //
        geneList.add(new AdvancedGene("Movement 2",4,5,"Movement 1","Choose where you move on random move, requires Movement 1")); // done
        geneList.add(new Gene("Speed",4,3,"Move twice in a round")); // done
        geneList.add(new AdvancedGene("Persistence",4,4,"Speed","Speed + 2nd chance on defense, aggression, and struggle for survival, requires Speed")); //
        geneList.add(new Gene("Struggle for Survival",5,5,"Eat amoebas instead of starving")); //done
        geneList.add(new Gene("Struggle for Survival",5,5,"Eat amoebas instead of starving")); //
        geneList.add(new AdvancedGene("Aggression",5,5,"Struggle for Survival","If struggle for survival fails, spend 1 BP to try harder; requires SfS")); //
        geneList.add(new Gene("Defense",4,4,"50% chance to negate attacks")); //
        geneList.add(new Gene("Defense",4,4,"50% chance to negate attacks")); //done
        geneList.add(new Gene("Escape",4,4,"Move randomly upon attempted attack, doesn't work with aggression")); //done
        geneList.add(new AdvancedGene("Armor",8,6,"Defense","Escape","Always negate normal attacks, aggressions deal 1 DP; requires defense OR escape")); //
        geneList.add(new Gene("Intelligence",2,3,"Does nothing")); //
        geneList.add(new Gene("Intelligence",2,3,"Does nothing")); // done
        geneList.add(new AdvancedGene("PhD",3,4,"Intelligence","Does nothing better")); // done
        geneList.add(new Gene("Camouflage 1",4,4,"50% chance to ignore defense, more than a mouthful, and escape")); //
        geneList.add(new AdvancedGene("Camouflage 2",4,4,"Camouflage 1","Always ignore defense, more than a mouthful, and escape; requires Camoflage 1")); //
        geneList.add(new Gene("Ray Protection",5,-2,"Guards against genetic defect by having -2 MP; doesn't count for scoring")); //
        geneList.add(new Gene("More than a Mouthful",4,3,"Attacks turn into 1 DP")); //
        geneList.add(new Gene("Life Expectancy",5,5,"Die at 3 DP instead of 2")); //
        geneList.add(new Gene("Life Expectancy",5,5,"Die at 3 DP instead of 2")); //
        geneList.add(new Gene("Energy Conservation",6,6,"Drifting grants 1 BP")); //done
        geneList.add(new Gene("Division Rate",5,5,"Cell division costs 4 BP instead of 6")); //
        geneList.add(new Gene("Flexible",5,4,"Genes cost 1 less BP")); //
        geneList.add(new Gene("Rebound",5,4,"Hitting a barrier causes you to bounce")); // Done
        geneList.add(new Gene("Spores",3,3,"Divide anywhere instead of only on adjacent spaces to amoebas")); //
        geneList.add(new Gene("Spores",3,3,"Divide anywhere instead of only on adjacent spaces to amoebas")); //
        geneList.add(new Gene("Streamline",5,5,"Random movements are free")); // done
    } 
    public void mousePressed(MouseEvent e  ) {
        /*
        Bounds for board

        for(int i = 0; i < 5; i++){
            g.drawLine(150,50 + 85 * i, 650, 50+85*i);
        }
        for(int i = 0; i < 6; i++){
            g.drawLine(150+ 100*i,50, 150+100*i,390);
        }
        g.drawRect(250,390,100,85);


        Bounds for genes
        for(int i = 0; i < availableGenes.size(); i++){
            availableGenes.get(i).drawMe(g,120 + 85*(i%8),105 * (i/8)); 80 x 100
        }
        */
        if(gameStage == -1){
            gameStage = 0;
            drift = false;
            randomMove = false;
            startGame = true;
            moveAgain = false;
            driftAgain = false;
            keepMove = false;
            endCellDivision = false;
            restartGame = true;
            eatMoreLeft = false;
            eatMoreRight = false;
            doneBuyingGenes = false;
            viewGenes = false;
        }
        if (gameStage == 5 && e.getX() > 120 && e.getX() < 795 && (e.getX()-120)%85 < 80 && e.getY() > 0 && e.getY() < 415 && (e.getY())%105 < 100){
            int geneIndex = ((e.getX() - 120)/85) + 8 * (e.getY()/105);
            if(geneIndex < availableGenes.size() && availableGenes.get(geneIndex).buyGene(players[speciesSelected])){ //valid index & can buy the gene (the function does it if possible)
                availableGenes.remove(geneIndex);
            }
        } else if (gameStage == 4 && viewingGenes && diffMP > env.getOzoneThickness()){
            // for(int k = 0; k < players.length; k++){
            //     for(int i = 0; i < players[k].geneListLength(); i++){
            //         players[k].getGene(i).drawMe(g,120 + 85*i,175 * k + 75);
            //     }
            //     font = new Font("Arial", Font.PLAIN, 20);
            //     g.setFont(font);
            //     g.setColor(players[k].getColor());
            //     g.drawString("Genes:",120,50+175*k);
                
            // }
            if(e.getX() > 850 && e.getX() < 970 && e.getY() > 400 && e.getY() < 500 && players[speciesSelected].getBP() > 0){
                diffMP--;
                players[speciesSelected].changeBP(-1);
            } else if ((e.getX()-120)%85 < 80 && e.getX() > 120 && e.getY() > 75 && (e.getY()-75)%175 < 100 && (e.getY() < 525)){
                int speciesIndex = (e.getY()-75)/175;
                int geneIndex = (e.getX() - 120)/85;
                if(players[speciesIndex].geneListLength() > geneIndex){
                    diffMP -= players[speciesIndex].getGene(geneIndex).getMP();
                    availableGenes.add(players[speciesIndex].removeGene(geneIndex));
                }
            }
        } else if(150 < e.getX() && e.getX() < 650 && 50 < e.getY() && e.getY() < 390 || (250 < e.getX() && e.getX() < 350 && 390 < e.getY() && e.getY() < 475)){
            int xIndex = (e.getX()-150)/100;
            int yIndex = (e.getY()-50)/85;
            Species tempSpecies = players[speciesSelected];
            if(gameStage == 1 || gameStage == 2){
                tempSpecies.addAmoeba(new Amoeba(tempSpecies.getColor(), xIndex, yIndex, 0));
            } else if(gameStage == 6 && tempSpecies.size() < 7){
                if(tempSpecies.size() == 0){
                    tempSpecies.addAmoeba(new Amoeba(tempSpecies.getColor(), xIndex, yIndex, 0));
                } else if (((tempSpecies.hasGene("Spores") || tempSpecies.size() == 1) && tempSpecies.getBP() >= 4 && tempSpecies.hasGene("Division Rate"))){
                    tempSpecies.addAmoeba(new Amoeba(tempSpecies.getColor(), xIndex, yIndex, 0));
                    tempSpecies.changeBP(-4);
                } else if (((tempSpecies.hasGene("Spores") || tempSpecies.size() == 1) && tempSpecies.getBP() >= 6)){
                    tempSpecies.addAmoeba(new Amoeba(tempSpecies.getColor(), xIndex, yIndex, 0));
                    tempSpecies.changeBP(-6);
                } else if (tempSpecies.getBP() >= 4 && tempSpecies.hasGene("Division Rate")){
                    boolean canPlace = false;
                    Amoeba tempAmoeba;
                    for(int i = 0; i < tempSpecies.size(); i++){
                        tempAmoeba = tempSpecies.get(i);
                        if(Math.abs(tempAmoeba.getX()-xIndex) + Math.abs(tempAmoeba.getY()-yIndex) <= 1){
                            canPlace = true;
                        }
                    }
                    if(canPlace){
                        tempSpecies.addAmoeba(new Amoeba(tempSpecies.getColor(), xIndex, yIndex, 0));
                        tempSpecies.changeBP(-4);
                    }
                    
                } else if (tempSpecies.getBP() >= 6){
                    boolean canPlace = false;
                    Amoeba tempAmoeba;
                    for(int i = 0; i < tempSpecies.size(); i++){
                        tempAmoeba = tempSpecies.get(i);
                        if(Math.abs(tempAmoeba.getX()-xIndex) + Math.abs(tempAmoeba.getY()-yIndex) <= 1){
                            canPlace = true;
                        }
                    }
                    if(canPlace){
                        tempSpecies.addAmoeba(new Amoeba(tempSpecies.getColor(), xIndex, yIndex, 0));
                        tempSpecies.changeBP(-6);
                    }
                    
                }
                
            } else if (gameStage == 3 && isChoosingMove){
                Amoeba tempAmoeba = tempSpecies.get(amoebaIndexSelected);
                if(Math.abs(tempAmoeba.getX()-xIndex) + Math.abs(tempAmoeba.getY()-yIndex) <= 1){
                    isChoosingMove = false;
                    tempAmoeba.setLocation(xIndex,yIndex);
                    drift = true;
                    randomMove = true;
                    if(tempSpecies.hasGene("Speed") && env.getDirection() != 5){
                        drift = false;
                        randomMove = false;
                        keepMove = true;
                        moveAgain = true;
                        driftAgain = true;
                    } else if(tempSpecies.hasGene("Speed") && speedChoiceCounter == 0 && env.getDirection() == 5){
                        speedChoiceCounter += 1;
                        drift = false;
                        randomMove = false;
                        isChoosingMove = true;
                    } else if(eatFood(tempSpecies,amoebaIndexSelected,foodGrid)){
                        amoebaIndexSelected += 1;
                        if(env.getDirection() == 5){
                            isChoosingMove = true;
                        }
                        speedChoiceCounter = 0;
                    }
                } 
            }

        }
    }
    public boolean eatFood(Species species, int index, Food[][] foods){
        isChoosingMove = false;
        Amoeba tempAmoeba = species.get(index);
        Color speciesColor = species.getColor();
        int x = tempAmoeba.getX();
        int y = tempAmoeba.getY();
        int waysToEat = foods[y][x].waysToEat(speciesColor);
        if(waysToEat == 1){
            foods[y][x].eat(speciesColor);
            foods[y][x].excrete(speciesColor);
        } else if (waysToEat == 2){
            choosingFoodColor = tempAmoeba.getSpecies();
            choosingFoodX = x;
            choosingFoodY = y;
            eatMoreLeft = true;
            eatMoreRight = true;
            drift = false;
            randomMove = false;
            return false;
        } else {
            if((species.hasGene("Aggression"))){
                boolean firstAttempt = true;
                if(!tryEatAmoeba(species,x,y,true)){
                    if(species.hasGene("Persistence")){
                        if(!tryEatAmoeba(species,x,y,true)){
                            firstAttempt = false;
                        }
                    } else {
                        firstAttempt = false;
                    }
                }
                if(firstAttempt == false && species.getBP() > 0){
                    species.changeBP(-1);
                    if(!tryAggEatAmoeba(species,x,y)){
                        if(species.hasGene("Persistence")){
                            if(!tryAggEatAmoeba(species,x,y)){
                                tempAmoeba.damage(1);
                            }
                        } else {
                            tempAmoeba.damage(1);
                        }
                    }
                }
            } else if((species.hasGene("Struggle for Survival"))){
                if(!tryEatAmoeba(species,x,y,false)){
                    if(species.hasGene("Persistence")){
                        if(!tryEatAmoeba(species,x,y,false)){
                            tempAmoeba.damage(1);
                        }
                    } else {
                        tempAmoeba.damage(1);
                    }
                }
            } else {
                tempAmoeba.damage(1);
            }
        }
        return true;

    }
    public boolean canEat(){
        Species tempSpecies = players[speciesSelected];
        Amoeba tempAmoeba = tempSpecies.get(amoebaIndexSelected);
        int x = tempAmoeba.getX();
        int y = tempAmoeba.getY();
        int waysToEat = foodGrid[y][x].waysToEat(tempSpecies.getColor());
        if(waysToEat == 0){
            return false;
        }
        return true;
        
    }
    public boolean tryEatAmoeba(Species species, int x, int y, boolean hasAggression){
        Amoeba tempAmoeba;
        for(int i = 0; i < players.length; i++){
            for(int k = 0; k < players[i].size(); k++){
                tempAmoeba = players[i].get(k);
                if(tempAmoeba.getX() == x && tempAmoeba.getY() == y && tempAmoeba.getSpecies() != species.getColor()){
                    if(species.hasGene("Camouflage 2") || (species.hasGene("Camouflage 1") && Math.random() < 0.5)){
                        return players[i].camoEatAmoeba(k);
                    }
                    if(hasAggression){
                        return players[i].aggressiveEatAmoeba1(k);
                    }
                    return players[i].eatAmoeba(k);
                }
            }
        }
        return false;
    }
    public boolean tryAggEatAmoeba(Species species, int x, int y){
        Amoeba tempAmoeba;
        for(int i = 0; i < players.length; i++){
            for(int k = 0; k < players[i].size(); k++){
                tempAmoeba = players[i].get(k);
                if(tempAmoeba.getX() == x && tempAmoeba.getY() == y && tempAmoeba.getSpecies() != species.getColor()){
                    if(species.hasGene("Camouflage 2") || (species.hasGene("Camouflage 1") && Math.random() < 0.5)){
                        return players[i].camoAggressiveEatAmoeba(k);
                    }
                    return players[i].aggressiveEatAmoeba(k);
                }
            }
        }
        return false;
    }
    public boolean[] getButtonVisiblities(){
        boolean[] visibilities = {drift,randomMove,startGame,moveAgain,keepMove,endCellDivision,restartGame,eatMoreLeft,eatMoreRight,driftAgain,doneBuyingGenes,viewGenes,pointSetter};
        return visibilities;
    }
}