package com.simplemethod.dockerparser;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ConnectionAPI {
    private static final Logger logger = LogManager.getLogger(ConnectionAPI.class);

    private final String DOCKER_SOCKET;

    public ConnectionAPI(String dockerSocket)
    {
        this.DOCKER_SOCKET=dockerSocket;
    }


    private static String[] ReturnErrors() {
        String[] returnArray = new String[3];
        returnArray[0] = String.valueOf(510);
        returnArray[1] = null;
        returnArray[2] = "{\"message\": \"Cannot connect to the server.\"}";
        return returnArray;
    }


    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/SystemPing
     */
    public String[] containersPing() {
        String[] returnArray = new String[3];
        try {
            HttpResponse<String> containersResponse = Unirest.get(DOCKER_SOCKET+"/_ping").asString();
            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            returnArray[2] = containersResponse.getBody();
            return returnArray;
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ContainerList
     */
    public String[] containersJson(boolean all, int limit, String filters) {
        String[] returnArray = new String[3];
        try {
            HttpResponse<JsonNode> containersResponse = Unirest.get(DOCKER_SOCKET + "/containers/json")
                    .header("accept", "application/json").header("Content-Type", "application/json")
                    .queryString("all", all)
                    .queryString("limit", limit)
                    .queryString("filters", filters)
                    .queryString("size",true)
                    .asJson();
            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            returnArray[2] = containersResponse.getBody().toString();
            return returnArray;
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/SystemInfo
     */
    public String[] getSystemInformation() {
        String[] returnArray = new String[3];
        JSONParser parser = new JSONParser();
        org.json.JSONObject body = new org.json.JSONObject();
        Object obj = null;
        try {
            HttpResponse<JsonNode> systemInfo = Unirest.get(DOCKER_SOCKET+"/info").asJson();
            obj = parser.parse(systemInfo.getBody().toString());
            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
            body.put("Containers",jsonObject.get("Containers") );
            body.put("ContainersRunning",jsonObject.get("ContainersRunning"));
            body.put("ContainersPaused",jsonObject.get("ContainersPaused"));
            body.put("ContainersStopped",jsonObject.get("ContainersStopped"));
            body.put("Images",jsonObject.get("Images"));
            body.put("MemTotal",jsonObject.get("MemTotal"));
            returnArray[0] = String.valueOf(systemInfo.getStatus());
            returnArray[1] = systemInfo.getHeaders().toString();
            returnArray[2] = body.toString();
            return returnArray;
        } catch ( NullPointerException | ParseException | org.json.JSONException |  UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }
    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ContainerList
     */
    public String[] containersJson(boolean all, int limit) {
        return containersJson(all, limit, "{}");
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ContainerInspect
     */
    public String[] containersIDJson(String id) {
        String[] returnArray = new String[3];
        try {
            HttpResponse<JsonNode> containersResponse = Unirest.get(DOCKER_SOCKET + "/containers/" + id + "/json")
                    .header("accept", "application/json").queryString("size",true).header("Content-Type", "application/json").asJson();
            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            returnArray[2] = containersResponse.getBody().toString();
            return returnArray;
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ContainerLogs
     */
    public String[] containersLogs(String id) {
        String[] returnArray = new String[3];
        try {
            HttpResponse<String> containersResponse = Unirest.get(DOCKER_SOCKET + "/containers/" + id + "/logs")
                    .header("Content-Type", "text/plain").queryString("stdout", true)
                    .asString();
            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            returnArray[2] = containersResponse.getBody();
            return returnArray;
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ContainerTop
     */
    public String[] containersTop(String id, String ps_args) {
        String[] returnArray = new String[3];
        try {
            HttpResponse<JsonNode> containersResponse = Unirest.get(DOCKER_SOCKET + "/containers/" + id + "/top")
                    .header("accept", "application/json").header("Content-Type", "application/json").queryString("ps_args", ps_args)
                    .asJson();
            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            returnArray[2] = containersResponse.getBody().toString();
            return returnArray;
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ContainerTop
     */
    public String[] containersTop(String id) {
        return containersTop(id, "-ef");
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ContainerCreate
     */
    public String[] containersCreate(String name, JSONObject configuration) {
        String[] returnArray = new String[3];
        try {
            HttpResponse<JsonNode> containersResponse = Unirest.post(DOCKER_SOCKET + "/containers/create")
                    .header("accept", "application/json").header("Content-Type", "application/json").queryString("name", name).body(configuration).asJson();
            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            returnArray[2] = containersResponse.getBody().toString();
            return returnArray;
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ContainerDelete
     */
    public String[] containersIDRemove(String id) {
        String[] returnArray = new String[3];
        try {
            HttpResponse<JsonNode> containersResponse = Unirest.delete(DOCKER_SOCKET + "/containers/" + id)
                   .header("Content-Type", "application/json").queryString("v", false).queryString("force", true).queryString("link", false).asJson();
            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            if (containersResponse.getBody() != null) {
                returnArray[2] = containersResponse.getBody().toString();
            } else {
                returnArray[2] = "{\"message\": \"Something went wrong.\"}";
            }
            return returnArray;
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ContainerPause
     */
    public String[] containersIDPause(String id) {
        String[] returnArray = new String[3];
        try {
            HttpResponse<JsonNode> containersResponse = Unirest.post(DOCKER_SOCKET + "/containers/" + id + "/pause")
                    .header("accept", "application/json").header("Content-Type", "application/json").asJson();
            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            if (containersResponse.getBody() != null) {
                returnArray[2] = containersResponse.getBody().toString();
            } else {
                returnArray[2] = "{\"message\": \"Something went wrong.\"}";
            }
            return returnArray;
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ContainerUnpause
     */
    public String[] containersIDUnpause(String id) {
        String[] returnArray = new String[3];
        try {
            HttpResponse<JsonNode> containersResponse = Unirest.post(DOCKER_SOCKET + "/containers/" + id + "/unpause")
                    .header("accept", "application/json").header("Content-Type", "application/json").asJson();
            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            if (containersResponse.getBody() != null) {
                returnArray[2] = containersResponse.getBody().toString();
            } else {
                returnArray[2] = "{\"message\": \"Something went wrong.\"}";
            }
            return returnArray;
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ContainerRestart
     */
    public String[] containersIDRestart(String id) {
        String[] returnArray = new String[3];
        try {
            HttpResponse<JsonNode> containersResponse = Unirest.post(DOCKER_SOCKET + "/containers/" + id + "/restart")
                   .header("Content-Type", "application/json").asJson();


            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            if (containersResponse.getBody() != null) {
                returnArray[2] = containersResponse.getBody().toString();
            } else {
                returnArray[2] = "{\"message\": \"Something went wrong.\"}";
            }

            return returnArray;
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ContainerKill
     */
    public String[] containersIDKill(String id) {
        String[] returnArray = new String[3];
        try {
            HttpResponse<JsonNode> containersResponse = Unirest.post(DOCKER_SOCKET + "/containers/" + id + "/kill")
                    .header("accept", "application/json").header("Content-Type", "application/json").asJson();
            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            if (containersResponse.getBody() != null) {
                returnArray[2] = containersResponse.getBody().toString();
            } else {
                returnArray[2] = "{\"message\": \"Something went wrong.\"}";
            }
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
        return returnArray;
    }


    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ContainerStart
     */
    public String[] containersIDStop(String id) {
        String[] returnArray = new String[3];
        try {
            HttpResponse<JsonNode> containersResponse = Unirest.post(DOCKER_SOCKET + "/containers/" + id + "/stop")
                    .header("accept", "application/json").header("Content-Type", "application/json").asJson();
            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            if (containersResponse.getBody() != null) {
                returnArray[2] = containersResponse.getBody().toString();
                return returnArray;
            } else {
                returnArray[2] = "{\"message\": \"Something went wrong.\"}";
            }
            return returnArray;
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ContainerStart
     */
    public String[] containersIDStart(String id) {
        String[] returnArray = new String[3];
        try {
            HttpResponse<JsonNode> containersResponse = Unirest.post(DOCKER_SOCKET + "/containers/" + id + "/start")
                    .header("accept", "application/json").header("Content-Type", "application/json").asJson();
            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            if (containersResponse.getBody() != null) {
                returnArray[2] = containersResponse.getBody().toString();
                return returnArray;
            } else {
                returnArray[2] = "{\"message\": \"Something went wrong.\"}";
            }
            return returnArray;
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ContainerStats
     */
    public String[] containersStats(String id) {
        String[] returnArray = new String[3];
        try {
            HttpResponse<JsonNode> containersResponse = Unirest.get(DOCKER_SOCKET + "/containers/" + id + "/stats")
                    .header("accept", "application/json").header("Content-Type", "application/json").queryString("stream", false)
                    .asJson();
            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            returnArray[2] = containersResponse.getBody().toString();
            return returnArray;
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ContainerPrune
     */
    public String[] containersPrune() {
        String[] returnArray = new String[3];
        try {
            HttpResponse<JsonNode> containersResponse = Unirest.post(DOCKER_SOCKET + "/containers/prune")
                    .header("accept", "application/json").header("Content-Type", "application/json").asJson();
            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            returnArray[2] = containersResponse.getBody().toString();
            return returnArray;
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ImageList
     */
    public String[] imagesList() {
        String[] returnArray = new String[3];
        try {
            HttpResponse<JsonNode> containersResponse = Unirest.get(DOCKER_SOCKET + "/images/json")
                    .header("accept", "application/json").header("Content-Type", "application/json").queryString("all", true).asJson();
            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            returnArray[2] = containersResponse.getBody().toString();
            return returnArray;
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ContainerExec
     */
    public String[] execCreate(String id, JSONObject configuration) {
        String[] returnArray = new String[3];
        try {
            HttpResponse<JsonNode> containersResponse = Unirest.post(DOCKER_SOCKET + "/containers/" + id + "/exec")
                    .header("accept", "application/json").header("Content-Type", "application/json").body(configuration)
                    .asJson();
            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            returnArray[2] = containersResponse.getBody().toString();
            return returnArray;
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }

    /**
     * https://docs.docker.com/engine/api/v1.39/#operation/ExecStart
     */
    public String[] execStart(String id) {
        String[] returnArray = new String[3];
        try {
            HttpResponse<JsonNode> containersResponse = Unirest.post(DOCKER_SOCKET + "/exec/" + id + "/start")
                    .header("accept", "application/json").header("Content-Type", "application/json").body("{\"Detach\": false,\"Tty\": false}")
                    .asJson();
            returnArray[0] = String.valueOf(containersResponse.getStatus());
            returnArray[1] = containersResponse.getHeaders().toString();
            returnArray[2] = containersResponse.getBody().toString();
            return returnArray;
        } catch (UnirestException e) {
            logger.error("Cannot connect to the server.");
            return ReturnErrors();
        }
    }

}
