package com.gmail.murmeldjur.adressbok;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Created by Ola on 2016-12-03.
 */
public class ThreadPool implements Runnable {
    private static final Logger log = Logger.getLogger(ThreadPool.class.getName());
    private static Map<String, Integer> listOfServers;
    private Adressbok adressbok;

    private static final int FIXED_POOL_SIZE = 5;

    public ThreadPool(Adressbok adressbok, Map<String, Integer> listOfServers) {
        this.adressbok = adressbok;
        this.listOfServers = listOfServers;
    }

    @Override
    public void run() {
        log.info("Starting Thread Pool");
        ExecutorService executor = Executors.newFixedThreadPool(FIXED_POOL_SIZE);

        for (Map.Entry<String, Integer> entry : listOfServers.entrySet()) {
            Runnable worker = new LoadCentralCatalogue(adressbok, entry.getKey(), entry.getValue());
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }
}



