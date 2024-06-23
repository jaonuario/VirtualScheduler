package classes;

import java.util.ArrayList;

import Interfaces.ControlInterface;
import Interfaces.InterSchedulerInterface;

public class ShortTermScheduler implements ControlInterface, InterSchedulerInterface, Runnable{
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

    public ShortTermScheduler(){
        this.readyQueue = new ArrayList<VirtualProcess>();
        this.currentProcess = null;
    }

    public void addProcess(VirtualProcess bcp){
        readyQueue.add(bcp);
        return;
    }

    public int getProcessLoad(){
        return readyQueue.size();
    }

    public void startSimulation(){
        this.status = EXECUTING;
    }

    public void suspendSimulation(){
        try {
            status=SUSPENDING;
            while(status!=SUSPENDED);
            this.wait();
        } catch (InterruptedException  e) {
            e.printStackTrace();
        }
    }

    public void resumeSimulation(){
        status = EXECUTING;
        this.notify();
    }

    public void stopSimulation(){
        this.status = FINALIZED;
    } 

    public void displayProcessQueues(){
         
    }

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
    
    public short getStatus(){
        return this.status;
    }

    private VirtualProcess getNextProcess(){
        return readyQueue.remove(0);
    }
    
    private void interruptProcess(){
        readyQueue.add(currentProcess);
        this.currentProcess = null;
    }
    
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

    private boolean launchProcess(){
        currentProcess = getNextProcess();
        if(currentProcess == null){
            status = EXECUTION_ERROR;
            return false;
        }
        return true;
    }

    private void IOactivities(){
        for(int i=0; i < iOQueue.size(); i++){
            iOQueue.get(i).launchIO();
            if(iOQueue.get(i).getIOBlock() == 0){
                addProcess(iOQueue.remove(i));
            }
        }
    }
}
