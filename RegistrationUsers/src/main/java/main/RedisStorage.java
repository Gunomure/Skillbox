package main;

import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;
import org.redisson.client.protocol.ScoredEntry;
import org.redisson.config.Config;

import java.util.Collection;

import static java.lang.System.out;

public class RedisStorage {

    // Объект для работы с Redis
    private RedissonClient redisson;

    // Объект для работы с ключами
    private RKeys rKeys;

    // Объект для работы с Sorted Set'ом
    private RScoredSortedSet<String> onlineUsers;

    private final static String KEY = "ONLINE_USERS";
    private final static int REDIS_DEFAULT_PORT = 6379;

    void init(int redisConnectPort) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:%d".formatted(redisConnectPort));
        try {
            redisson = Redisson.create(config);
        } catch (RedisConnectionException Exc) {
            out.println("Не удалось подключиться к Redis");
            out.println(Exc.getMessage());
        }
        rKeys = redisson.getKeys();
        onlineUsers = redisson.getScoredSortedSet(KEY);
        rKeys.delete(KEY);
    }

    void init() {
        init(REDIS_DEFAULT_PORT);
    }

    // Фиксирует посещение пользователем страницы
    void registerUser(int user_id) {
        //ZADD ONLINE_USERS
        onlineUsers.add(System.nanoTime(), String.valueOf(user_id));
        onlineUsers = redisson.getScoredSortedSet(KEY);
    }

    public void moveUserToEnd(String user) {
        if (onlineUsers.contains(user)) {
            onlineUsers.addScore(user, - onlineUsers.getScore(user) + System.nanoTime());
        } else {
            out.println("User %s not found".formatted(user));
        }
    }

    public void moveUserToBegin(String user) {
        if (onlineUsers.contains(user)) {
            onlineUsers.addScore(user, -onlineUsers.getScore(user));
        } else {
            out.println("User %s not found".formatted(user));
        }
    }

    public String showUser() {
        String user = onlineUsers.first();
        out.println("— На главной странице показываем пользователя %s".formatted(user));
        moveUserToEnd(user);
        return user;
    }

    public String showUser(String user) {
        if (onlineUsers.contains(user)) {
            out.println("> Пользователь %s оплатил платную услугу".formatted(user));
            moveUserToBegin(user);
            user = onlineUsers.first();
            out.println("— На главной странице показываем пользователя %s".formatted(user));
            moveUserToEnd(user);
            return user;
        } else {
            return "";
        }
    }

    private Double getMaxUserScore() {
        Double score = onlineUsers.lastScore();
        return score != null ? score : 0;
    }

    public void printAll() {
        Collection<ScoredEntry<String>> scoredEntries = onlineUsers.entryRange(Double.NEGATIVE_INFINITY, true, Double.POSITIVE_INFINITY, true);
        for (ScoredEntry<String> item : scoredEntries) {
            out.println(item.getValue() + " - " + item.getScore() + " rank: " + onlineUsers.rank(item.getValue()));
        }
    }

    void shutdown() {
        redisson.shutdown();
    }

}
