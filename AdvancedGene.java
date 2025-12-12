public class AdvancedGene extends Gene{
    String prerequisite;
    String prerequisite2;
    public AdvancedGene(String name, int cost, int bioNum, String prerequisite, String desc){
        super(name,cost,bioNum,desc);
        this.prerequisite = prerequisite;
        prerequisite2 = "";
    }
    public AdvancedGene(String name, int cost, int bioNum, String prerequisite, String prerequisite2, String desc){
        super(name,cost,bioNum,desc);
        this.prerequisite = prerequisite;
        this.prerequisite2 = prerequisite2;
    }
    public boolean isAdvanced(){
        return true;
    }
    public boolean buyGene(Species species){
        if(species.hasGene("flexible")){
            if(species.getBP() >= super.getCost()-1 && (species.hasGene(prerequisite) || species.hasGene(prerequisite2)) && !species.hasGene(super.getName())){
                species.addGene(this);
                if(!species.removeGene(prerequisite)){
                    species.removeGene(prerequisite2);
                }
                species.changeBP(-(super.getCost()-1));
                return true;
            }
            return false;
        } else {
            if(species.getBP() >= super.getCost() && (species.hasGene(prerequisite) || species.hasGene(prerequisite2)) && !species.hasGene(super.getName())){
                species.addGene(this);
                if(!species.removeGene(prerequisite)){
                    species.removeGene(prerequisite2);
                }
                species.changeBP(-super.getCost());
                return true;
            }
            return false;
        }
        
    }
    public String getReq(){
        return prerequisite;
    }
    public String getReq2(){
        return prerequisite2;
    }
    

}