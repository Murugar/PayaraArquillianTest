package com.iqmsoft.payara.arquillian;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class ParallelSample {
    final static Logger logger = LoggerFactory.getLogger(ParallelSample.class);

    final Random rnd = new Random();

    @Resource
    ManagedExecutorService executor;

    public void execute() {
        logger.info(">>>>> execute() start");

      
        List<Supplier<Void>> taskList = new ArrayList<Supplier<Void>>(){/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
            add(() -> task1());
            add(() -> task2());
        }};

      
        List<CompletableFuture<Void>> futureList =
            taskList.stream()
                .map(task -> CompletableFuture.supplyAsync(task, executor))
                .collect(Collectors.toList());

        CompletableFuture
       
            .allOf(futureList.toArray(new CompletableFuture<?>[futureList.size()]))
           
            .whenComplete((Void result, Throwable e) -> {
                if (e != null) {
                    logger.error(e.getMessage(), e);
                }
                logger.info("call whenComplete");
            }).join();

        logger.info(">>>>> execute() end");
    }

    private Void task1() {
        IntStream.range(0, 10).forEach((i) -> {
            try {
                logger.info("task1 ({})", i);
                Thread.sleep(rnd.nextInt(1000));
            } catch (InterruptedException e) {
                logger.error("task1", e);
            }
        });
        logger.info("task1 completed!");
        return null;
    }

    private Void task2() {
        IntStream.range(0, 10).forEach((i) -> {
            try {
                logger.info("task2 ({})", i);
                Thread.sleep(rnd.nextInt(1000));
            } catch (InterruptedException e) {
                logger.error("task2", e);
            }
        });
        logger.info("task2 completed!");
        return null;
    }
}
