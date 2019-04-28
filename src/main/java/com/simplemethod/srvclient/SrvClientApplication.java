package com.simplemethod.srvclient;


import com.simplemethod.containers.ContainersController;
import com.simplemethod.dockerparser.ConnectionAPI;
import com.simplemethod.dockerparser.ConnectionServiceImpl;
import com.simplemethod.exec.ExecController;
import com.simplemethod.images.ImagesController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication

@ComponentScan(basePackages = {"com.simplemethod.containers", "com.simplemethod.dockerparser", "com.simplemethod.srvclient",})
public class SrvClientApplication {

    private  final  static String dockerSocket = "http://127.0.0.1:2137";

    @Bean
    public ConnectionAPI connectionAPI() {
        return new ConnectionServiceImpl().connectionAPI((dockerSocket));
    }

    @Bean
    public ContainersController containersController() {
        return new ContainersController();
    }

    @Bean
    public ExecController execController() {
        return new ExecController();
    }

    @Bean
    public ImagesController imagesController() {
        return new ImagesController();
    }

    @Bean
    public String string() {
        return "";
    }


    public static void main(String[] args) {

        SpringApplication.run(SrvClientApplication.class, args);

    }

}
