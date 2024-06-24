package classes;

import java.util.ArrayList;

import Interfaces.ControlInterface;
import Interfaces.InterSchedulerInterface;

/** Classe para implementar o escalonador de processos de curto prazo. Classe responsável por gerenciar a execução de processos prontos, suspensos e retormar a simulação
 * @author
 */
public class ShortTermScheduler implements ControlInterface, InterSchedulerInterface, Runnable{
    //Constantes
    public static final long QUANTUM = 100;
    public static final short EXECUTION_ERROR = -1;
    public static final short SUSPENDED = 0;
    public static final short SUSPENDING = 1;
    public static final short EXECUTING = 2;
    public static final short FINALIZED = 3;

    private ArrayList<VirtualProcess> readyQueue;           //fila de prontos
    private ArrayList<VirtualProcess> processesCompleted;   //fila de processos finalizados
    private ArrayList<VirtualProcess> iOQueue;              //fila de processos em I/O
    private VirtualProcess currentProcess;                  //processo atual em execução
    private short status;                                   //status do simulador


    /** Construtor da classe ShortTermScheduler
     * 
     */
    public ShortTermScheduler(){
        this.readyQueue = new ArrayList<VirtualProcess>();
        this.currentProcess = null;
    }

    /** Método para adicionar um processo à fila de prontos
     * @param bcp VirtualProcess -  Bloco de Controle do Processo
     * @return
     */
    public void addProcess(VirtualProcess bcp){
        readyQueue.add(bcp);                                //altera o estado do bcp
        return;
    }

    /** Método para obter a quantidade de processos que estão na fila de prontos
     * @return int - Retorna o número de elementos que estão na lista fila de prontos
     */
    public int getProcessLoad(){
        return readyQueue.size();
    }

    /** Método para iniciar a simulação */
    public void startSimulation(){
        this.status = EXECUTING;
    }

    /** Método para suspender a simulação */
    public void suspendSimulation(){
        try {
            status=SUSPENDING;
            while(status!=SUSPENDED);
            this.wait();
        } catch (InterruptedException  e) {
            e.printStackTrace();
        }
    }

    /** Método para retomar a simulação */
    public void resumeSimulation(){
        status = EXECUTING;
        this.notify();
    }

    /** Método para interromper a interromper a simulação */
    public void stopSimulation(){
        this.status = FINALIZED;
    } 

    /** Método para mostrar a fila de processos */
    public void displayProcessQueues(){
         
    }

    /** Método para controlar ativamente o fluxo de execução dos processos
     * 
     */
    public void run(){
        long time;

        time = System.currentTimeMillis();
        while (true) {
            while (status==EXECUTING){
                //Ciclo de quantum
                if((time - System.currentTimeMillis())>=QUANTUM){
                    if(currentProcess != null){
                        interruptProcess();
                    }
                    IOactivities();
                    time = System.currentTimeMillis();
                    continue;
                }
    
                if(currentProcess == null){
                    if(!launchProcess()){
                        return;
                    }
                }

                runCommandsFromCurrentProcess();

                if(status==SUSPENDING){
                    status=SUSPENDED;
                    break;
                }
            }
            
            if (status==FINALIZED) {
                return;
            }
        }
    }
    
    /** Método para obter o status da simulação
     * 
     * @return Short - Retorna o status da simulação
     */
    public short getStatus(){
        return this.status;
    }

    /** Método para obter e remover o próximo processo da fila de prontos
     * 
     * @return VirtualProcess - Retorna um objeto do tipo VirtualProcess depois de remover o primeiro elemento da lista 
     */
    private VirtualProcess getNextProcess(){
        return readyQueue.remove(0);
    }
    
    /** Método para interromper um processo em execução e colocá-lo de volta na fila de prontos */
    private void interruptProcess(){
        readyQueue.add(currentProcess);
        this.currentProcess = null;
    }
    
    /** Método para executar os comandos do processo atual */
    private void runCommandsFromCurrentProcess(){
        if(currentProcess.hasNextCommand()){
            currentProcess.nextCommand();
            if (currentProcess.getStatus() == VirtualProcess.BLOCK) {
                iOQueue.add(currentProcess);
                currentProcess = null;
            }
        }
        else{
            processesCompleted.add(currentProcess);
            currentProcess = null;
        }
    }

    /** Método para iniciar a execução do próximo processo na fila de prontos
     * 
     * @return Boolean - Se um processo foi atribuído com sucesso, retorna true. Caso contrário, retorna false
     */
    private boolean launchProcess(){
        currentProcess = getNextProcess();
        if(currentProcess == null){
            status = EXECUTION_ERROR;
            return false;
        }
        return true;
    }

    /** Método para controlar as atividades de entrada e saída (I/O) dos processos que estão na fila */
    private void IOactivities(){
        for(int i=0; i < iOQueue.size(); i++){
            iOQueue.get(i).launchIO();
            if(iOQueue.get(i).getIOBlock() == 0){
                addProcess(iOQueue.remove(i));
            }
        }
    }

    public int getCompletedProcesses() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCompletedProcesses'");
    }
}
