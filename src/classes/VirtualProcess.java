package classes;

public class VirtualProcess {
    //Constantes
    public static final long BURST_VALUE = 4000;
    public static final short READY = 0;
    public static final short EXEC = 1;
    public static final short BLOCK = 2;
    public static final short INTERRUPTED = 3;

    private String name;    // nome do processo
    private Integer[] seqc; // sequencia de commandos
    private long birth;     // instante que o processo nasceu (milisegundos)
    private int status;     // status do processo
    private int counter;    // contador de programa
    private int iOblock;    // contador de IOs
    
    public VirtualProcess(String name, Integer[] seqc){
        this.name = name;
        this.seqc = seqc;
        this.birth = System.currentTimeMillis();
        this.counter = 0;
        this.status = READY;
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
    
    public int getIOBlock(){
        return this.iOblock;
    }

    private void execute(){
        burst();
    }

    private void block(int value){
        burst();
        iOblock = value;
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

    // consome um IO
    public void launchIO(){
        this.iOblock--;
    }

}
