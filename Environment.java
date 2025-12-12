public class Environment{
    /*
        0: up
        1: right
        2: down
        3: left
        4: nothing
        5: choice
    */
    int direction;
    int ozoneThickness;
    int nextDirection;
    int nextOzoneThickness;
    public Environment(){
        direction = (int)(Math.random() * 6);
        ozoneThickness = (int)(Math.random() * 6 + 6);
        nextDirection = (int)(Math.random() * 6);
        nextOzoneThickness = (int)(Math.random() * 6 + 6);
    }
    public void randomize(){
        direction = nextDirection;
        ozoneThickness = nextOzoneThickness;
        nextDirection = (int)(Math.random() * 6);
        nextOzoneThickness = (int)(Math.random() * 6 + 6);
    }
    public int getDirection(){
        return direction;
    }
    public String getDirectionString(){
        switch(direction){
            case 0:
                return "up";
            case 1:
                return "right";
            case 2:
                return "down";
            case 3:
                return "left";
            case 4:
                return "no drift";
            case 5:
                return "choice";
            default:
                return null;
        }
    }
    public int getOzoneThickness(){
        return ozoneThickness;
    }
}