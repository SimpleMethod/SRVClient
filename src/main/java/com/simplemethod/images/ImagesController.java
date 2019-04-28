package com.simplemethod.images;

import com.simplemethod.dockerparser.ConnectionAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImagesController {

    private static final Logger logger = LogManager.getLogger(ImagesController.class);

    @Autowired
    private ConnectionAPI connectionAPI;


    public ImagesController(){}

    public String[] imageList() throws NullPointerException
    {
        String[] returnArray;
        returnArray =connectionAPI.imagesList();
        if(returnArray[0].equals(String.valueOf(200)))
        {
            return  returnArray;
        }
        else
        {
            logger.error("["+returnArray[0]+"] "+ returnArray[2]);
            return  returnArray;
        }
    }
}
