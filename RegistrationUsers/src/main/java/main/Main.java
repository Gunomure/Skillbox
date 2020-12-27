package main;

public class Main {

    private final static int USERS_COUNT = 20;

    public static void main(String[] args) {
        RedisStorage redis = new RedisStorage();
        redis.init();

        for (int i = 1; i <= USERS_COUNT; i++) {
            redis.registerUser(i);
        }

        for (int j = 0; j < 2; j++) {
            int indexRnd = rnd(1, USERS_COUNT - 1);
            int userRnd = rnd(indexRnd + 1, USERS_COUNT - 1);
            System.out.println("indexRnd: %d".formatted(indexRnd));
            System.out.println("userRnd: %d".formatted(userRnd));

            for (int i = 1; i <= USERS_COUNT; i++) {
                if (indexRnd == i) {
                    redis.showUser(String.valueOf(userRnd));
                } else {
                    redis.showUser();
                }
            }
        }

        redis.printAll();
        redis.shutdown();
    }

    public static int rnd(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
