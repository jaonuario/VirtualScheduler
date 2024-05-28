package classes;

import Interfaces.ControlInterface;
import Interfaces.InterSchedulerInterface;

public class ShortTermScheduler implements ControlInterface, InterSchedulerInterface{

    public void addProcess(VirtualProcess bcp){
        return;
    }

    public int getProcessLoad(){
        return 0;
    }

    public void startSimulation(){

    }

    public void suspendSimulation(){

    }

    public void resumeSimulation(){

    }

    public void stopSimulation(){

    }

    public void displayProcessQueues(){

    }
}
