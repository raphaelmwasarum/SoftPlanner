package com.raphjava.softplanner.data.services;


import com.raphjava.softplanner.data.interfaces.IOService;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class DefaultIOService implements IOService
{

    public String read(String path)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(path)))
        {
            StringBuilder sb = new StringBuilder();
            String data = br.readLine();
            String line = (data != null ? (data + "\n") : null);
            while (line != null)
            {
                sb.append(line);
                data = br.readLine();
                line = (data != null ? (data + "\n") : null);
            }
            return sb.toString();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean ensureExistence(String directory)
    {
        try
        {
            File f = new File(directory);
            if (!f.exists()) return f.mkdirs();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Overwrites to file. By default, this method creates the file if it doesn't exist.
     *
     * @param filePath     file's path.
     * @param data         data.
     * @param failIfAbsent doesn't create the file if it doesn't exist.
     * @return true if writing was successful, false otherwise.
     */
    @Override
    public boolean writeToFile(String filePath, String data, boolean... failIfAbsent)
    {
        if (failIfAbsent.length != 0 && failIfAbsent[0])
        {
            if (new File(filePath).exists()) return postValidationWrite(filePath, data);
            else return false;
        }
        else
        {
            return postValidationWrite(filePath, data);

        }
    }

    private boolean postValidationWrite(String filePath, String data)
    {
        try (FileWriter fs = new FileWriter(new File(filePath)))
        {
            fs.write(data);
            fs.flush();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

}
