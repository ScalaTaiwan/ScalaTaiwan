import java.util.concurrent.*;

public class JavaFuture {
    private void java7() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<Integer> futureOne = executorService.submit(() -> {
            Thread.sleep(1000);
            return 1;
        });

        Future<Integer> futureTwo = executorService.submit(() -> {
            Thread.sleep(2000);
            return 2;
        });

        System.out.println(futureOne.get(5000, TimeUnit.MILLISECONDS) + futureTwo.get(5000, TimeUnit.MILLISECONDS));
    }

    private void java8() throws InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture<Integer> futureOne = CompletableFuture
                .runAsync(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .thenApplyAsync(v -> 1);

        CompletableFuture<Integer> futureTwo = CompletableFuture
                .runAsync(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .thenApplyAsync(v -> 2);

        CompletableFuture<Integer> result = futureOne.thenCombineAsync(futureTwo, (i1, i2) -> i1 + i2);
        System.out.println(result.get(5000, TimeUnit.MILLISECONDS));
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        JavaFuture simpleFuture = new JavaFuture();
        simpleFuture.java7();
        simpleFuture.java8();
    }
}

