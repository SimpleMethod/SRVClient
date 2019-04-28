package com.simplemethod.exec;

import com.simplemethod.dockerparser.ConnectionAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExecController {
    private static final Logger logger = LogManager.getLogger(ExecController.class);

    @Autowired
    private ConnectionAPI connectionAPI;


    public ExecController(){}




    public String[] execCommand(String id, String configuration, String argument) throws NullPointerException
    {
        String[] returnArray;
        JSONObject request = new JSONObject();
        JSONArray cmd = new JSONArray();
        cmd.put(configuration);
        cmd.put(argument);
        request.put("Cmd", cmd);
        returnArray =connectionAPI.execCreate(id,request);
        if(returnArray[0].equals(String.valueOf(201)))
        {
            JSONObject requestID = new JSONObject(returnArray[2]);
            returnArray=connectionAPI.execStart(requestID.getString("Id"));
            return returnArray;
        }
        else
        {
            logger.error("["+returnArray[0]+"] "+ returnArray[2]);
        return  returnArray;
        }
    }
}
