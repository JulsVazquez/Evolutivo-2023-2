package fciencias.tarea1.complements;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class FileManager {

    private Map<String,FileWriter> filesList;
    private Map<String,BufferedInputStream> filesBuffer;
    private Map<Long,String> filesIndex;
    private long currentKey = 0;

    public FileManager()
    {
        filesList = new HashMap<>();
        filesIndex = new HashMap<>();
        filesBuffer = new HashMap<>();
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
                filesBuffer.put(file,new BufferedInputStream(new FileInputStream(file)));
                filesIndex.put(currentKey, file);
                currentKey++;
                return currentKey - 1;

            } catch (IOException e) {
                
                e.printStackTrace();
            }
           
        }
        return -1L;
    }

    public void writeFile(long index, String newText,boolean appendFile)
    {

        BufferedWriter bufferedWriter;
        try {
            FileWriter fileWriter = filesList.get(filesIndex.get(index));
            bufferedWriter = new BufferedWriter(fileWriter,newText.length());
            PrintWriter printWriter = new PrintWriter(bufferedWriter); 
            if(!appendFile)
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

    public String readFile(long index)
    {
         BufferedInputStream bufferedInputStream = filesBuffer.get(filesIndex.get(index));
         byte[] newLine = new byte[1024];
        
         int lineLength = 0;
        try {
            lineLength = bufferedInputStream.read(newLine);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         byte[] readedLine = Arrays.copyOf(newLine, lineLength);
         return new String(readedLine);

    }

    public String readFileLine(long index, int line)
    {
        String[] lines =  readFile(index).split("\n");
        return lines[line];
    }
    
}
