package com.simplemethod.containers;

import com.simplemethod.dockerparser.ConnectionAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ContainersController {

    @Autowired
   ConnectionAPI connectionAPI;

   // ConnectionAPI connectionAPI;
    private static final Logger logger = LogManager.getLogger(ContainersController.class);


    public ContainersController() {
    }


    public String[] systemInformation() {
        String[] returnArray;
        returnArray = connectionAPI.getSystemInformation();
        if (returnArray[0].equals(String.valueOf(200))) {
            return returnArray;
        } else {
            logger.error("[" + returnArray[0] + "] " + returnArray[2]);
            return returnArray;
        }
    }

    public String[] containersPing() {
        String[] returnArray;
        returnArray = connectionAPI.containersPing();
        if (returnArray[0].equals(String.valueOf(200))) {
            return returnArray;
        } else {
            logger.error("[" + returnArray[0] + "] " + returnArray[2]);
            return returnArray;
        }
    }


    public String[] containersJson(boolean all, int limit, String filters) throws NullPointerException {
        String[] returnArray;
        returnArray = connectionAPI.containersJson(all, limit, filters);
        if (returnArray[0].equals(String.valueOf(200))) {
            return returnArray;
        } else {
            logger.error("[" + returnArray[0] + "] " + returnArray[2]);
            return returnArray;
        }
    }

    public String[] containersIDJson(String id) throws NullPointerException {
        String[] returnArray;
        returnArray = connectionAPI.containersIDJson(id);

        if (returnArray[0].equals(String.valueOf(200))) {
            return returnArray;
        } else {
            logger.error("[" + returnArray[0] + "] " + returnArray[2]);
            return returnArray;
        }
    }

    public String[] containersLogs(String id) throws NullPointerException {
        String[] returnArray;
        returnArray = connectionAPI.containersLogs(id);

        if (returnArray[0].equals(String.valueOf(200))) {
            return returnArray;
        } else {
            logger.error("[" + returnArray[0] + "] " + returnArray[2]);
            return returnArray;
        }
    }

    public String[] containersTop(String id, String ps_args) throws NullPointerException {
        String[] returnArray;
        returnArray = connectionAPI.containersTop(id, ps_args);

        if (returnArray[0].equals(String.valueOf(200))) {
            return returnArray;
        } else {
            logger.error("[" + returnArray[0] + "] " + returnArray[2]);
            return returnArray;
        }
    }

    public String[] containersCreate(String name, String configuration) throws NullPointerException {

        String[] returnArray;
        JSONObject json = null;
        try {
            json = new JSONObject(configuration);
        } catch (JSONException e) {
            json = new JSONObject("{}");
        }
        returnArray = connectionAPI.containersCreate(name, json);

        if (returnArray[0].equals(String.valueOf(201))) {
            return returnArray;
        } else {
            logger.error("[" + returnArray[0] + "] " + returnArray[2]);
            return returnArray;
        }
    }

    public String[] containersIDRemove(String id) throws NullPointerException {
        String[] returnArray;

        returnArray = connectionAPI.containersIDRemove(id);
        if (returnArray[0].equals(String.valueOf(204))) {
            return returnArray;
        } else {
            logger.error("[" + returnArray[0] + "] " + returnArray[2]);
            return returnArray;
        }
    }

    public String[] containersIDPause(String id) throws NullPointerException {
        String[] returnArray;

        returnArray = connectionAPI.containersIDPause(id);
        if (returnArray[0].equals(String.valueOf(204))) {
            return returnArray;
        } else {
            logger.error("[" + returnArray[0] + "] " + returnArray[2]);
            return returnArray;
        }
    }

    public String[] containersIDUnpause(String id) throws NullPointerException {
        String[] returnArray;

        returnArray = connectionAPI.containersIDUnpause(id);
        if (returnArray[0].equals(String.valueOf(204))) {
            return returnArray;
        } else {
            logger.error("[" + returnArray[0] + "] " + returnArray[2]);
            return returnArray;
        }
    }



    public String[] containersIDRestart(String id) throws NullPointerException {
        String[] returnArray;

        returnArray = connectionAPI.containersIDRestart(id);

        if (returnArray[0].equals(String.valueOf(204))) {
            return returnArray;
        } else {
            logger.error("[" + returnArray[0] + "] " + returnArray[2]);
            return returnArray;
        }
    }

    public String[] containersIDKill(String id) throws NullPointerException {
        String[] returnArray;

        returnArray = connectionAPI.containersIDKill(id);
        if (returnArray[0].equals(String.valueOf(204))) {
            return returnArray;
        } else {
            logger.error("[" + returnArray[0] + "] " + returnArray[2]);
            return returnArray;
        }
    }


    public String[] containersIDStop(String id) throws NullPointerException {
        String[] returnArray;

        returnArray = connectionAPI.containersIDStop(id);
        if (returnArray[0].equals(String.valueOf(204))) {
            return returnArray;
        } else {
            logger.error("[" + returnArray[0] + "] " + returnArray[2]);
            return returnArray;
        }
    }

    public String[] containersIDStart(String id) throws NullPointerException {
        String[] returnArray;

        returnArray = connectionAPI.containersIDStart(id);
        if (returnArray[0].equals(String.valueOf(204))) {
            return returnArray;
        } else {
            logger.error("[" + returnArray[0] + "] " + returnArray[2]);
            return returnArray;
        }
    }


    public String[] containersPrune() throws NullPointerException {
        String[] returnArray;
        returnArray = connectionAPI.containersPrune();
        if (returnArray[0].equals(String.valueOf(200))) {
            return returnArray;
        } else {
            logger.error("[" + returnArray[0] + "] " + returnArray[2]);
            return returnArray;
        }
    }

    public String[] containersStats(String id) throws NullPointerException {
        String[] returnArray;
        returnArray = connectionAPI.containersStats(id);
        if (returnArray[0].equals(String.valueOf(200))) {
            return returnArray;
        } else {
            logger.error("[" + returnArray[0] + "] " + returnArray[2]);
            return returnArray;
        }
    }
}
