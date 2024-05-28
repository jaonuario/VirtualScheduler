package Interfaces;
import classes.VirtualProcess;

public interface InterSchedulerInterface {
    void addProcess(VirtualProcess bcp);
    int getProcessLoad();
}
