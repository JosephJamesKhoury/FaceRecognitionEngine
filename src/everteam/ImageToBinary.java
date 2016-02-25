package everteam;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageToBinary {
    
    public String ConvertImageToBinary(String path) throws FileNotFoundException, IOException
    {
        File input= new File(path);

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(input));
        int read;
        String text = "";
        
        while ((read=bis.read()) != -1)
        {
            text = Integer.toString(read,2);
            while (text.length() < 8)
            {
                text = "0"+text;
            }
        }
        
        return text;
    }
}

    