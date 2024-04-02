package tx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class AccountRunner {

    public static void main(String[] args) {
        final int ACCOUNT_COUNT = 4;
        final int THREAD_COUNT = 4;
        int txCount = 0;

        List<Account> accounts = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < ACCOUNT_COUNT; i++) {
            accounts.add(new Account(10000));
        }

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(THREAD_COUNT);
        do {
            int initialDelay = random.nextInt(1000, 2001);
            executor.scheduleWithFixedDelay(new Transaction(accounts), initialDelay, initialDelay, TimeUnit.MILLISECONDS);
            txCount++;
        } while (txCount < 30);
        executor.schedule(() -> {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }, 1, TimeUnit.MINUTES);
    }
}
