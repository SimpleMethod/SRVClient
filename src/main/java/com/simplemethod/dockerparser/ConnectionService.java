package com.simplemethod.dockerparser;

public interface ConnectionService {
    ConnectionAPI connectionAPI(String dockerSocket);
}
