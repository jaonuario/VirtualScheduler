package classes; 


/**Importação bibliotecas Java */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**Importa a interface "SubmissionInterface" do pacote de interfaces (Interfaces) */
import Interfaces.SubmissionInterface;

/**Classe para implementação do LongTermScheduler. Recebe processos novos para simulação para assim estruturá-los em uma fila de prontos
 * @author 
 */
public class LongTermScheduler implements SubmissionInterface{

    //Define uma variável "queueProcess" do tipo "ArrayList<VirtualProcess>" que conterá elementos do tipo "VirtualProcess". É uma lista dinâmica
    private ArrayList<VirtualProcess> queueProcess;

    /**Construtor da classe LongTermScheduler
     * 
     */
    public LongTermScheduler(){
        queueProcess = new ArrayList<VirtualProcess>();     //Instância "queueProcess" do tipo "ArrayList<VirtualProcess>" que armazenará objetos do tipo "VirtualProcess"
    }

    /** Método para submeter processo na fila
     * 
     * @param fileName String - Nome do Arquivo
     * @return Boolean - Processo submetido com sucesso (true) ou não (false) 
     */
    public boolean submitJob(String fileName){
        VirtualProcess process = createProcess(fileName);   
        queueProcess.add(process);                      
        
        //add process in queue

        return false;
    }


    /** Método para mostrar a fila de submissão de processos
     * 
     */
    public void displaySubmissionQueue(){
        return;
    }

    /** Método para criar processos
     * 
     * @param fileName String - Nome do arquivo
     * @return VirtualProcess - Cria e retorna um novo processo virtual com o nome do arquivo e a sequência de instruções
     */
    private VirtualProcess createProcess(String fileName){
        Scanner reader;
        Integer[] instructionsSequence; 
        
        reader = loadFile(fileName);
        if(reader == null){
            return null;
        }

        String processName = reader.next();
        if(!processName.endsWith(".txt")){
            reader.close();
            return null;
        } 

        instructionsSequence = getInstructionsSequence(reader);
        if(instructionsSequence == null){
            return null;
        }

        return new VirtualProcess(fileName, instructionsSequence);
    }

    /** Método para carregar um arquivo
     * 
     * @param filename
     * @return Scanner - Retorna um Scanner para ler o arquivo
     */
    private Scanner loadFile(String filename){
        File file;
        Scanner reader;
        
        try{    
            file = new File(filename);
            reader = new Scanner(file);
            return reader;

        } catch(FileNotFoundException e){
            System.out.println("Error");
            e.printStackTrace();
            return null;
        }
    }

    /** Método para extrair a sequência de instruções de um arquivo
     * 
     * @param reader Scanner -  Lê as instruções
     * @return Integer - Retorna a conversão de uma lista de inteiros em um array de inteiros, criado e preenchido com elementos da lista "sequence" 
     */
    private Integer[] getInstructionsSequence(Scanner reader){
        ArrayList<Integer> sequence = new ArrayList<Integer>();
        boolean init = false;
        
        if(!reader.hasNext()){
            reader.close();
            return null;
        }

        String token = reader.next();
        if(!token.endsWith(".txt")){
            reader.close();
            return null;
        }
        

        reader.next();
        if(!token.equals("begin")){
            reader.close();
            return null;
        }
        init = true;
        while (reader.hasNext()) {
            token = reader.next();

            switch (token) {
                case "begin":
                    return null;

                case "execute":
                    sequence.add(0);
                    break;

                case "block":
                    if(reader.hasNext()){
                        try {
                            sequence.add(Integer.parseInt(reader.next()));
                        } catch (NumberFormatException e) {
                            reader.close();
                            return null;
                        }
                    }
                    break;

                case "end":
                    reader.close();
                    if (!init)
                        return null;
                    return sequence.toArray(new Integer[sequence.size()]);

                default:
                    return null;
            }
        }
        
        reader.close();
        return null;
    }
}
