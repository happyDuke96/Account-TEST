package tx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class AccountRunner {
    private final static int ACCOUNT_COUNT = 4;
    private final static int THREAD_COUNT = 4;
    private final static int TRANSACTION_COUNT = 30;
    private final static List<Account> accounts = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(TRANSACTION_COUNT);
        Random RANDOM = new Random();
        for (int i = 0; i < ACCOUNT_COUNT; i++) {
            accounts.add(new Account(10000));
        }

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(THREAD_COUNT);
        int initialDelay = RANDOM.nextInt(1000, 2001);
        executor.scheduleAtFixedRate(new Transaction(accounts, countDownLatch), initialDelay, initialDelay, TimeUnit.MILLISECONDS);
        countDownLatch.await();
        System.out.println("All transactions completed");
        executor.shutdown();
    }
}
