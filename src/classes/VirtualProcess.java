package classes;


/** Classe para modelar um processo virtual para funcionar dentro de um sistema simulado
 * @author
 */
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
    
    /** Construtor da classe VirtualProcess
     * 
     * @param name String - Nome do processo
     * @param seqc Integer - Sequência de comandos
     */
    public VirtualProcess(String name, Integer[] seqc){
        this.name = name;
        this.seqc = seqc;
        this.birth = System.currentTimeMillis();
        this.counter = 0;
        this.status = READY;
    }
    
    /** Método para obter a idade do processo
     * 
     * @return Long - Retorna a idade do processo em milissegundos 
     */
    public long getAge(){
        return System.currentTimeMillis() - birth;
    }
    
    /** Método para obter o nome do processo
     * 
     * @return String - Retorna o nome do processo
     */
    public String getName(){
        return name;
    }
    
    /** Método para obter o status do processo
     * 
     * @return int - Retorna o status do processo
     */
    public int getStatus(){
        return this.status;
    }
    
    /** Método para obter as operações de I/O que o processo está aguardando para serem concluídas
     * 
     * @return int - Retorna o número atual de operações de entrada e saída
     */
    public int getIOBlock(){
        return this.iOblock;
    }

    /** Método para simular a execução de um comando de processo */
    private void execute(){
        burst();
    }

    /** Método para simular o bloqueio de um processo, dado um tempo de burst
     * 
     * @param value int - Tempo que o processo ficará bloqueado em milissegundos
     */
    private void block(int value){
        burst();
        iOblock = value;
        this.status = BLOCK;
    }

    /** Método para obter o próximo comando do processo */
    public void nextCommand(){
        if(seqc[counter] == 0){
            execute();
        }
        else{
            block(seqc[counter]);
        }
        counter++;
    }   

    /** Método para verificar se há ainda comandos para serem executados na sequência de comandos do processo
     * 
     * @return Boolean - Retorna true se ainda haver comandos para serem executados, caso contrário retorna false
     */
    public boolean hasNextCommand(){
        if(counter < seqc.length)
            return true;
        return false;
    }

    /** Método para simular um tempo de Burst de CPU */
    private void burst(){
        long start = System.currentTimeMillis();
        long current;
        do {
            current = System.currentTimeMillis();
        } while ((current - start) < BURST_VALUE);
    }

    /** Método responsável por simular uma conclusão de uma operação de entrada e sáida (I/O). Consome uma I/O */
    public void launchIO(){
        this.iOblock--;
    }

}
