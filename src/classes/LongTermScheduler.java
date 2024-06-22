package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import Interfaces.SubmissionInterface;

public class LongTermScheduler implements SubmissionInterface{
    private ArrayList<VirtualProcess> queuProcess;

    public LongTermScheduler(){
        queuProcess = new ArrayList<VirtualProcess>();
    }

    public boolean submitJob(String fileName){
        VirtualProcess process = createProcess(fileName);
        
        
        //add process in queue

        return false;
    }

    public void displaySubmissionQueue(){
        return;
    }

    private VirtualProcess createProcess(String fileName){
        Scanner reader;
        Integer[] instructionsSequence; 
        
        reader = loadFile(fileName);
        if(reader == null){
            return null;
        }

        instructionsSequence = getInstructionsSequence(reader);
        if(instructionsSequence == null){
            return null;
        }

        return new VirtualProcess(fileName, instructionsSequence);
    }

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

    private Integer[] getInstructionsSequence(Scanner reader){
        ArrayList<Integer> sequence = new ArrayList<Integer>();
        boolean init = false;
        
        if(!reader.hasNext()){
            reader.close();
            return null;
        }

        String token = reader.next();
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
