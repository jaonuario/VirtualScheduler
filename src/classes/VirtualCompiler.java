package classes;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files

public class VirtualCompiler {
    public VirtualCompiler(){
        
    }

    public Integer[] createVirtualProcess(String filename){
        File file;
        Scanner reader;
        
        try{    
            file = new File(filename);
            reader = new Scanner(file);

        } catch(FileNotFoundException e){
            System.out.println("Error");
            e.printStackTrace();
            return null;
        }

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
