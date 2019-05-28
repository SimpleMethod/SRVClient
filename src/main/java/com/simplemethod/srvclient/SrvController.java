package com.simplemethod.srvclient;


import com.simplemethod.containers.ContainersController;
import com.simplemethod.dockerparser.ConnectionAPI;
import com.simplemethod.dockerparser.ConnectionService;
import com.simplemethod.dockerparser.ConnectionServiceImpl;
import com.simplemethod.exec.ExecController;
import com.simplemethod.images.ImagesController;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@RestController
@Validated
@RequestMapping("v1.0")
public class SrvController {




    @Autowired
    ContainersController containersController;

    @Autowired
    ExecController execController;

    @Autowired
    ImagesController imagesController;




/*  @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)

    ResponseEntity<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        JSONObject request = new JSONObject();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        request.put("message", "Incorrect parameters have been entered: "+e.getMessage());
        return new ResponseEntity<>(request.toString(),headers, HttpStatus.BAD_REQUEST);
    }*/

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>("{\"message\":\"Incorrect parameters have been entered:\"}",headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        JSONObject request = new JSONObject();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        request.put("message", "Incorrect parameters have been entered: "+e.getMessage());
        return new ResponseEntity<>(request.toString(),headers, HttpStatus.BAD_REQUEST);
    }




    @GetMapping(path = "/info")
    public @ResponseBody
    ResponseEntity<String> systemInformation() {

        String[] returnArray = new String[3];
        try {

            returnArray = containersController.systemInformation();

        } catch (NullPointerException e) {

        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(returnArray[2], headers, HttpStatus.valueOf(Integer.valueOf(returnArray[0])));
    }

    @GetMapping(path = "/_ping")
    public @ResponseBody
    ResponseEntity<String> containersPing() {

        String[] returnArray = new String[3];
        try {

            returnArray = containersController.containersPing();

        } catch (NullPointerException e) {

        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(returnArray[2], headers, HttpStatus.valueOf(Integer.valueOf(returnArray[0])));
    }


    @GetMapping(path = "/containers/json")
    public @ResponseBody
    ResponseEntity<String> containersJson(@NotNull @RequestParam("all")Boolean all,  @NotNull @Min(1) @RequestParam("limit") Integer limit, @RequestParam("filters") String filters) {
        String[] returnArray = new String[3];
        try {
            returnArray = containersController.containersJson(all, limit, filters);
        } catch (NullPointerException e) {

        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(returnArray[2], headers, HttpStatus.valueOf(Integer.valueOf(returnArray[0])));
    }


    @GetMapping(path = "/containers/{id}/json")
    public @ResponseBody
    ResponseEntity<String> containersIDJson(@PathVariable String id) {
        String[] returnArray = new String[3];
        HttpHeaders headers = new HttpHeaders();
        try {
            returnArray = containersController.containersIDJson(id);
        } catch (NullPointerException e) {

        }
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(returnArray[2], headers, HttpStatus.valueOf(Integer.valueOf(returnArray[0])));
    }

    @GetMapping(path = "/containers/{id}/logs")
    public @ResponseBody
    ResponseEntity<String> containersIDLogs(@PathVariable String id) {
        String[] returnArray = new String[3];
        HttpHeaders headers = new HttpHeaders();
        try {
            returnArray = containersController.containersLogs(id);
        } catch (NullPointerException e) {

        }
        headers.add("Content-Type", "text/plain");
        return new ResponseEntity<>(returnArray[2], headers, HttpStatus.valueOf(Integer.valueOf(returnArray[0])));
    }

    @GetMapping(path = "/containers/{id}/top")
    public @ResponseBody
    ResponseEntity<String> containersIDTop(@PathVariable String id, @RequestParam("ps_args") String ps_args) {
        String[] returnArray = new String[3];
        try {
            returnArray = containersController.containersTop(id, ps_args);
        } catch (NullPointerException e) {

        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(returnArray[2], headers, HttpStatus.valueOf(Integer.valueOf(returnArray[0])));
    }

    @PostMapping(path = "/containers/{id}/exec")
    public @ResponseBody
    ResponseEntity<String> containersIDExec(@PathVariable String id, @NotNull @RequestParam("path") String path, @NotNull @RequestParam("argument") String argument) {
        String[] returnArray = new String[3];
        try {
            returnArray = execController.execCommand(id, path, argument);
        } catch (NullPointerException e) {

        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(returnArray[2], headers, HttpStatus.valueOf(Integer.valueOf(returnArray[0])));
    }

    @PostMapping(path = "/containers/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<String> containersCreate(HttpEntity<String> httpEntity, @NotNull @RequestParam("name") String name) {
        String[] returnArray = new String[3];
        String config = httpEntity.getBody();
        if(config == null || config.isEmpty())
        {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            return new ResponseEntity<>("{\"message\":\"Incorrect parameters have been entered:\"}",headers, HttpStatus.valueOf(415));
        }

        try {
            returnArray = containersController.containersCreate(name, config);
        } catch (NullPointerException e) {

        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(returnArray[2], headers, HttpStatus.valueOf(Integer.valueOf(returnArray[0])));
    }

    @DeleteMapping("/containers/{id}")
    public @ResponseBody
    ResponseEntity<String> containersIDRemove(@PathVariable String id) {
        String[] returnArray = new String[3];
        try {
            returnArray = containersController.containersIDRemove(id);
        } catch (NullPointerException e) {

        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(returnArray[2], headers, HttpStatus.valueOf(Integer.valueOf(returnArray[0])));
    }

    @PostMapping("/containers/{id}/pause")
    public @ResponseBody
    ResponseEntity<String> containersIDPause(@PathVariable String id) {
        String[] returnArray = new String[3];
        try {
            returnArray = containersController.containersIDPause(id);
        } catch (NullPointerException e) {

        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(returnArray[2], headers, HttpStatus.valueOf(Integer.valueOf(returnArray[0])));
    }

    @PostMapping("/containers/{id}/unpause")
    public @ResponseBody
    ResponseEntity<String> containersIDUnpause(@PathVariable String id) {
        String[] returnArray = new String[3];
        try {
            returnArray = containersController.containersIDUnpause(id);
        } catch (NullPointerException e) {

        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(returnArray[2], headers, HttpStatus.valueOf(Integer.valueOf(returnArray[0])));
    }


    @PostMapping("/containers/{id}/restart")
    public @ResponseBody
    ResponseEntity<String> containersIDRestart(@PathVariable String id) {
        String[] returnArray = new String[3];
        try {
            returnArray = containersController.containersIDRestart(id);
        } catch (NullPointerException e) {

        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(returnArray[2], headers, HttpStatus.valueOf(Integer.valueOf(returnArray[0])));
    }

    @PostMapping("/containers/{id}/kill")
    public @ResponseBody
    ResponseEntity<String> containersIDKill(@PathVariable String id) {
        String[] returnArray = new String[3];
        try {
            returnArray = containersController.containersIDKill(id);
        } catch (NullPointerException e) {

        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(returnArray[2], headers, HttpStatus.valueOf(Integer.valueOf(returnArray[0])));
    }


    @PostMapping("/containers/{id}/start")
    public @ResponseBody
    ResponseEntity<String> containersIDStart(@PathVariable String id) {
        String[] returnArray = new String[3];
        HttpStatus status;
        HttpHeaders headers = new HttpHeaders();
        try {
            returnArray = containersController.containersIDStart(id);
        } catch (NullPointerException e) {
        }

        if (Integer.valueOf(returnArray[0]) == 304) {
            //304->405
            status = HttpStatus.METHOD_NOT_ALLOWED;
        } else {
            status = HttpStatus.valueOf(Integer.valueOf(returnArray[0]));
        }

        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(returnArray[2], headers, status);
    }

    @PostMapping("/containers/{id}/stop")
    public @ResponseBody
    ResponseEntity<String> containersIDStop(@PathVariable String id) {
        String[] returnArray = new String[3];
        HttpStatus status;
        HttpHeaders headers = new HttpHeaders();
        try {
            returnArray = containersController.containersIDStop(id);
        } catch (NullPointerException e) {
        }

        if (Integer.valueOf(returnArray[0]) == 304) {
            //304->405
            status = HttpStatus.METHOD_NOT_ALLOWED;
        } else {
            status = HttpStatus.valueOf(Integer.valueOf(returnArray[0]));
        }

        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(returnArray[2], headers, status);
    }


    @GetMapping("/containers/{id}/stats")
    public @ResponseBody
    ResponseEntity<String> containersIDStats(@PathVariable String id) {
        String[] returnArray = new String[3];
        try {
            returnArray = containersController.containersStats(id);
        } catch (NullPointerException e) {
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(returnArray[2], headers, HttpStatus.valueOf(Integer.valueOf(returnArray[0])));
    }


    @PostMapping("/containers/prune")
    ResponseEntity<String> containersPrune() {
        String[] returnArray = new String[3];
        try {
            returnArray = containersController.containersPrune();
        } catch (NullPointerException e) {
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(returnArray[2], headers, HttpStatus.valueOf(Integer.valueOf(returnArray[0])));
    }


    @GetMapping("/images/json")
    ResponseEntity<String> imagesJson() {
        String[] returnArray = new String[3];
        try {
            returnArray = imagesController.imageList();
        } catch (NullPointerException e) {
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(returnArray[2], headers, HttpStatus.valueOf(Integer.valueOf(returnArray[0])));
    }
}
