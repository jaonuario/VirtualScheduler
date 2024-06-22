package classes;

public class VirtualProcess {
    //Constantes
    public static final long BURST_VALUE = 4000;
    public static final short READY = 0;
    public static final short EXEC = 1;
    public static final short BLOCK = 2;

    private String name;
    private Integer[] seqc;
    private long birth;
    private int status;
    private int counter;

    public VirtualProcess(String name, Integer[] seqc){
        this.name = name;
        this.seqc = seqc;
        this.birth = System.currentTimeMillis();
        this.counter = 0;
        this.status = READY;
    }

    public int run(){
        while (hasNextCommand()) {
            nextCommand();
        }
        return status;
    }

    public long getAge(){
        return System.currentTimeMillis() - birth;
    }

    public String getName(){
        return name;
    }

    public int getStatus(){
        return this.status;
    }
    
    private void execute(){
        burst();
    }

    private void block(int value){
        burst();
        this.status = BLOCK;
    }

    public void nextCommand(){
        if(seqc[counter] == 0){
            execute();
        }
        else{
            block(seqc[counter]);
        }
        counter++;
    }   

    public boolean hasNextCommand(){
        if(counter < seqc.length)
            return true;
        return false;
    }

    private void burst(){
        long start = System.currentTimeMillis();
        long current;
        do {
            current = System.currentTimeMillis();
        } while ((current - start) < BURST_VALUE);
    }
}
