package tx;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private final String id;
    private final AtomicInteger money;
    private final static Logger log = LogManager.getLogger(Account.class);

    private final Lock lock = new ReentrantLock();

    public Account(int money) {
        this.money = new AtomicInteger(money);
        this.id = RandomStringUtils.randomAlphabetic(25);
    }

    public Lock getLock() {
        return lock;
    }

    public void add(int money) {
        this.money.addAndGet(money);
    }

    public boolean withDraw(int money) {
        if (this.money.get() >= money){
            this.money.compareAndSet(this.money.intValue(),money);
            log.info("Withdraw money : {} from Account : {}", money, this);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", money=" + money +
                '}';
    }
}
