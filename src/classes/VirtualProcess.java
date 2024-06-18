package classes;

public class VirtualProcess {
    private static int burstValue = 0;
    private Integer[] seqc;
    private int counter;

    public VirtualProcess(String name, Integer[] seqc){
        this.counter = 0;
        this.seqc = seqc;
    }

    public void run(){

    }
    
    private void execute(){
        
    }

    private void block(int value){
        
    }

    private void nextCommand(){
        if(!hasNextCommand()){
            return;
        }

        if(seqc[counter] == 0){
            execute();
        }
        else{
            block(seqc[counter]);
        }

        counter++;
    }   

    private boolean hasNextCommand(){
        if(counter < seqc.length)
            return true;
        return false;
    }
}
