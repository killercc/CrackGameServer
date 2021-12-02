package com.zyx.crackgameserver.initrun;



import com.zyx.crackgameserver.modules.netty.nettyserver.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NettyRun implements CommandLineRunner {

    @Autowired
    private NettyServer nettyServer;



    @Async
    @Override
    public void run(String... args) throws Exception {
        //nettyServer.start();
    }
}
