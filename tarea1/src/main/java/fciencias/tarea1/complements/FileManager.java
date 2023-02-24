package fciencias.tarea1.complements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


public class FileManager {

    private Map<String,FileWriter> filesList;
    private Map<Long,String> filesIndex;
    private long currentKey = 0;

    public FileManager()
    {
        filesList = new HashMap<>();
        filesIndex = new HashMap<>();
    }

    public Long openFile(String file, boolean append)
    {
        if(filesList.containsKey(file) && filesList.get(file) != null)
        {
            for(Map.Entry<Long,String> fileTuple : filesIndex.entrySet())
            {
                if(fileTuple.getValue().equals(file))
                    return fileTuple.getKey();
            }
        }
        else
        {
            try 
            {
                FileWriter newFuiFileWriter = new FileWriter(file,append);
                filesList.put(file, newFuiFileWriter);
                filesIndex.put(currentKey, file);
                currentKey++;
                return currentKey - 1;

            } catch (IOException e) {
                
                e.printStackTrace();
            }
           
        }
        return -1L;
    }

    public void writeFile(long index, String newText,boolean newFile)
    {

        BufferedWriter bufferedWriter;
        try {
            FileWriter fileWriter = filesList.get(filesIndex.get(index));
            bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter); 
            if(newFile)
                printWriter.write(newText);
            else
                printWriter.append(newText);
            printWriter.close();

            
            bufferedWriter.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
       
    }
    
}
