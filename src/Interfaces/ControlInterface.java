package Interfaces;

/** Define um conjunto de métodos para encapsular operações relacionadas ao controle de processos ou simulações */
public interface ControlInterface {
    public void startSimulation();
    public void suspendSimulation();
    public void resumeSimulation();
    public void stopSimulation();
    public void displayProcessQueues();
}
