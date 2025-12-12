import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.net.URL;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Screen extends JPanel implements ActionListener, MouseListener{
    private JButton drift, randomMove, startGame, moveAgain, keepMove, endCellDivision, restartGame, eatMoreLeft, eatMoreRight,driftAgain, doneBuyingGenes, viewGenes;
    private boolean[] visibilities;
    private Game game;
    private boolean firstSound;
    private JTextField pointSetter;
	public Screen(){
        firstSound = true;
        game = new Game();
		setLayout(null);

        addMouseListener(this);
        // textfields
        pointSetter = new JTextField();
        pointSetter.setBounds(300,200,200,30);
        add(pointSetter);
        pointSetter.setVisible(false);


        // buttons
        drift = new JButton("Drift");
        drift.setBounds(40,10,250,30);
        add(drift);
        drift.addActionListener(this);
        drift.setVisible(false);

        randomMove = new JButton("Random Move (1 BP)");
        randomMove.setBounds(290,10,250,30);
        add(randomMove);
        randomMove.addActionListener(this);
        randomMove.setVisible(false);

        startGame = new JButton("Start");
        startGame.setBounds(300,224,200,112);
        add(startGame);
        startGame.addActionListener(this);
        startGame.setVisible(true);

        moveAgain = new JButton("Try to move again?");
        moveAgain.setBounds(290,10,250,30);
        add(moveAgain);
        moveAgain.addActionListener(this);
        moveAgain.setVisible(false);

        driftAgain = new JButton("Try to drift again?");
        driftAgain.setBounds(40,10,250,30);
        add(driftAgain);
        driftAgain.addActionListener(this);
        driftAgain.setVisible(false);

        keepMove = new JButton("Stay?");
        keepMove.setBounds(540,10,250,30);
        add(keepMove);
        keepMove.addActionListener(this);
        keepMove.setVisible(false);

        endCellDivision = new JButton("Stop Cell Division");
        endCellDivision.setBounds(290,10,250,30);
        add(endCellDivision);
        endCellDivision.addActionListener(this);
        endCellDivision.setVisible(false);

        restartGame = new JButton("Restart Game");
        restartGame.setBounds(850,530,120,60);
        add(restartGame);
        restartGame.addActionListener(this);
        restartGame.setVisible(true);

        eatMoreLeft = new JButton("Eat More Left Food");
        eatMoreLeft.setBounds(400,450,150,30);
        add(eatMoreLeft);
        eatMoreLeft.addActionListener(this);
        eatMoreLeft.setVisible(false);

        eatMoreRight = new JButton("Eat More Right Food");
        eatMoreRight.setBounds(575,450,150,30);
        add(eatMoreRight);
        eatMoreRight.addActionListener(this);
        eatMoreRight.setVisible(false);

        doneBuyingGenes = new JButton("Done Buying Genes?");
        doneBuyingGenes.setBounds(200,450,400,60);
        add(doneBuyingGenes);
        doneBuyingGenes.addActionListener(this);
        doneBuyingGenes.setVisible(false);

        viewGenes = new JButton("Show/Hide Genes");
        viewGenes.setBounds(850,170,130,60);
        add(viewGenes);
        viewGenes.addActionListener(this);
        viewGenes.setVisible(true);
            /*  
        sscB = new JButton("");
        sscB.setBounds(40,30,120,60);
        add(sscB);
        sscB.addActionListener(this);
        sscB.setVisible(false);
             */


        
        
	}
	public Dimension getPreferredSize(){
		return new Dimension(1000,600);
	}
	public void paintComponent(Graphics g){
        super.paintComponent(g);
        int soundNum = game.drawScreen(g);
        if(soundNum == 1 && firstSound){
            firstSound = false;
            playSound(1);
        } else if (soundNum == 2){
            playSound(2);
        }
		visibilities = game.getButtonVisiblities();
        drift.setVisible(visibilities[0]);
        randomMove.setVisible(visibilities[1]);
        startGame.setVisible(visibilities[2]);
        moveAgain.setVisible(visibilities[3]);
        keepMove.setVisible(visibilities[4]);
        endCellDivision.setVisible(visibilities[5]);
        restartGame.setVisible(visibilities[6]);
        eatMoreLeft.setVisible(visibilities[7]);
        eatMoreRight.setVisible(visibilities[8]);
        driftAgain.setVisible(visibilities[9]);
        doneBuyingGenes.setVisible(visibilities[10]);
        viewGenes.setVisible(visibilities[11]);
        pointSetter.setVisible(visibilities[12]);

		
	}
	public void actionPerformed(ActionEvent e){
        if(e.getSource() == startGame){
            game.actionPerformed("startGame",pointSetter.getText());
        } else if(e.getSource() == drift){
            game.actionPerformed("drift");
        } else if(e.getSource() == randomMove){
            game.actionPerformed("randomMove");
        } else if(e.getSource() == moveAgain){
            game.actionPerformed("moveAgain");
        } else if(e.getSource() == keepMove){
            game.actionPerformed("keepMove");
        } else if(e.getSource() == endCellDivision){
            game.actionPerformed("endCellDivision");
        } else if(e.getSource() == restartGame){
            game.actionPerformed("restartGame");
            firstSound = true;
        } else if(e.getSource() == eatMoreLeft){
            game.actionPerformed("eatMoreLeft");
        } else if(e.getSource() == eatMoreRight){
            game.actionPerformed("eatMoreRight");
        } else if(e.getSource() == driftAgain){
            game.actionPerformed("driftAgain");
        } else if(e.getSource() == doneBuyingGenes){
            game.actionPerformed("doneBuyingGenes");
        } else if(e.getSource() == viewGenes){
            game.actionPerformed("viewGenes");
        }
	}
    public void playSound(int num) {
        try {
            URL url;
            if(num == 1){
                url = this.getClass().getClassLoader().getResource("Short_triumphal_fanfare-John_Stracke-815794903.wav");
            } else {
                url = this.getClass().getClassLoader().getResource("Power_Up_Ray-Mike_Koenig-800933783.wav");
            }
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
            clip.start();
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }
    public void mousePressed(MouseEvent e  ) {
        game.mousePressed(e);
        repaint();
    }
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void animate(){
        while(true){
            try{
				Thread.sleep(10);
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
            repaint();
        }
    }
}
