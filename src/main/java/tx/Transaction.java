package tx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Transaction implements Runnable {

    private final List<Account> accounts;
    private final static Random RANDOM = new Random();
    private final static Logger log = LogManager.getLogger(Transaction.class);
    private final CountDownLatch countDownLatch;

    public Transaction(List<Account> accounts,CountDownLatch countDownLatch) {
        this.accounts = accounts;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        lockAccounts();
        try {
            Account accountFrom = accounts.get(RANDOM.nextInt(accounts.size()));
            Account accountTo;
            do {
                accountTo = accounts.get(RANDOM.nextInt(accounts.size()));
            } while (accountTo == accountFrom);

            int transferMoney = RANDOM.nextInt(1, 9999);
            if (accountFrom.withDraw(transferMoney)) {
                log.info("Adding money : {} to Account : {}", transferMoney, accountTo);
                accountTo.add(transferMoney);
                countDownLatch.countDown();
            }
        } finally {
            for (Account account : accounts) {
                account.getLock().unlock();
            }
        }
        try {
            Thread.sleep(RANDOM.nextInt(1000, 2001));
        } catch (InterruptedException e) {
            log.error("Transaction suspended...");
            Thread.currentThread().interrupt();
        }
    }

    private void lockAccounts() {
        while (true) {
            boolean allLocked = true;
            for (Account account : accounts) {
                if (!account.getLock().tryLock()) {
                    allLocked = false;
                    break;
                }
            }
            if (allLocked) {
                break;
            } else {
                for (Account account : accounts) {
                    account.getLock().unlock();
                }
            }
        }
    }
}
