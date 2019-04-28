package com.simplemethod.dockerparser;

import org.springframework.stereotype.Service;

@Service
public class ConnectionServiceImpl implements ConnectionService {

    @Override
    public ConnectionAPI connectionAPI(String dockerSocket)
    {
        return new ConnectionAPI(dockerSocket);
    }
}
