package alex.flux;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class FluxDemo {


    public static void main(String[] args) throws InterruptedException {
        //fluxInMainThread();
        //fluxInDifferentThread();
        //fluxOnErrorReturn();
        getPalindromicNumber();
    }

    private static boolean getPalindromicNumber(){

        int x = 121;

        if(x<0) return false;
        int cur = 0;
        int num = x;
        while(num != 0) {
            cur = cur * 10 + num % 10;
            num /= 10;
        }
        return cur == x;
    }

    /**
     * Request 9223372036854775807 number 表示请求数据为long的最大值，表示不管生产多少，订阅者都要消费掉
     * 发现Publish生产者和Subscribe消费者都在主线程执行
     * 执行结束以后Publisher执行Complete逻辑(可以有多个)，Subscriber执行Complete逻辑
     * @throws InterruptedException
     */
    private static void fluxInMainThread() throws InterruptedException {
        Flux.range(1, 6)
                .doOnRequest(n -> log.info("Request {} number", n)) // 注意顺序造成的区别
                .doOnComplete(() -> log.info("Publisher Complete 1"))
                //.publishOn(Schedulers.elastic())
                .map( i -> {
                            log.info("Publish {}, {}", Thread.currentThread(), i);
                            //return 10 / (i - 3);
                            return i;
                        }
                )
                .doOnComplete( () -> log.info("Publisher Complete 2"))
                //.subscribeOn(Schedulers.single())
                //.onErrorResume(e -> {
                //  log.error("Exception {}", e.toString());
                //  return Mono.just(-1);
                //})
                //.onErrorReturn(-1)
                .subscribe( i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
                        e -> log.error("error {}", e.toString()),
                        () -> log.info("Subscriber Complete")
                        //s -> s.request(4)
                );

        Thread.sleep(2000);
    }


    /**
     * 生产者在线程池Schedulers.elastic()执行
     * 消费者在复用线程Schedulers.single()执行
     * [ single-1] Request 256 number 在single申请请求数为256(以前是在主线程申请)
     * [ elastic-2]后面的生产者消费者都从线程池elastic-2中获取线程。
     * @throws InterruptedException
     */
    private static void fluxInDifferentThread() throws InterruptedException {
        Flux.range(1, 6)
                .doOnRequest(n -> log.info("Request {} number", n)) // 注意顺序造成的区别
                .doOnComplete(() -> log.info("Publisher Complete 1"))
                .publishOn(Schedulers.elastic())
                .map( i -> {
                            log.info("Publish {}, {}", Thread.currentThread(), i);
                            //return 10 / (i - 3);
                            return i;
                        }
                )
                .doOnComplete( () -> log.info("Publisher Complete 2"))
                .subscribeOn(Schedulers.single())
                //.onErrorResume(e -> {
                //  log.error("Exception {}", e.toString());
                //  return Mono.just(-1);
                //})
                //.onErrorReturn(-1)
                .subscribe( i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
                        e -> log.error("error {}", e.toString()),
                        () -> log.info("Subscriber Complete")
                        //s -> s.request(4)
                );

        Thread.sleep(2000);
    }


    /**
     * 当有异常发生时候，返回-1， .onErrorReturn(-1)
     * 当执行到3的时候，除数为0，发生异常，返回-1.
     * 因为是异常退出，所以没有执行完成，Publisher Complete 2 没有执行。
     * 流程已经结束，订阅者执行完成Subscriber Complete
     * @throws InterruptedException
     */
    private static void fluxOnErrorReturn() throws InterruptedException {
        Flux.range(1, 6)
                .doOnRequest(n -> log.info("Request {} number", n)) // 注意顺序造成的区别
                .doOnComplete(() -> log.info("Publisher Complete 1"))
                .publishOn(Schedulers.elastic())
                .map( i -> {
                            log.info("Publish {}, {}", Thread.currentThread(), i);
                            return 10 / (i - 3);
                            //return i;
                        }
                )
                .doOnComplete( () -> log.info("Publisher Complete 2"))
                .subscribeOn(Schedulers.single())
                //.onErrorResume(e -> {
                //  log.error("Exception {}", e.toString());
                //  return Mono.just(-1);
                //})
                .onErrorReturn(-1)
                .subscribe( i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
                        e -> log.error("error {}", e.toString()),
                        () -> log.info("Subscriber Complete")
                        //s -> s.request(4)
                );

        Thread.sleep(2000);
    }

    /**
     * 打印异常信息为除数为0， Exception java.lang.ArithmeticException: / by zero
     * @throws InterruptedException
     */
    private static void fluxOnErrorResume() throws InterruptedException {
        Flux.range(1, 6)
                .doOnRequest(n -> log.info("Request {} number", n)) // 注意顺序造成的区别
                .doOnComplete(() -> log.info("Publisher Complete 1"))
                .publishOn(Schedulers.elastic())
                .map( i -> {
                            log.info("Publish {}, {}", Thread.currentThread(), i);
                            return 10 / (i - 3);
                            //return i;
                        }
                )
                .doOnComplete( () -> log.info("Publisher Complete 2"))
                .subscribeOn(Schedulers.single())
                .onErrorResume(e -> {
                    log.error("Exception {}", e.toString());
                    return Mono.just(-1);
                })
                //.onErrorReturn(-1)
                .subscribe( i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
                        e -> log.error("error {}", e.toString()),
                        () -> log.info("Subscriber Complete")
                        //s -> s.request(4)
                );

        Thread.sleep(2000);
    }

    /**
     * Request 4 number 生产者只会请求4个线程。
     * 这里Publisher Complete 1，Publish Complete 2，Subscriber Complete 都没有打印， 因为上面逻辑都在线程[elastic-2,5,main]完毕的时候处理，目前都没有结束。
     * @throws InterruptedException
     */
    private void fluxWithSubscribe() throws InterruptedException {
        Flux.range(1, 6)
                .publishOn(Schedulers.elastic())
                .doOnRequest(n -> log.info("Request {} number", n)) // 注意顺序造成的区别
                .doOnComplete(() -> log.info("Publisher Complete 1"))
                .map( i -> {
                            log.info("Publish {}, {}", Thread.currentThread(), i);
                            //return 10 / (i - 3);
                            return i;
                        }
                )
                .doOnComplete( () -> log.info("Publisher Complete 2"))
                .subscribeOn(Schedulers.single())
                .onErrorResume(e -> {
                    log.error("Exception {}", e.toString());
                    return Mono.just(-1);
                })
                //.onErrorReturn(-1)
                .subscribe( i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
                        e -> log.error("error {}", e.toString()),
                        () -> log.info("Subscriber Complete"),
                        s -> s.request(4)
                );

        Thread.sleep(2000);
    }


    /**
     * Request 256 number 订阅者线程池被生产者线程池覆盖掉了，所以显示还是256.
     * @throws InterruptedException
     */
    private static void fluxWithSubscribeAndChangePublishOnThreadOrder() throws InterruptedException {
        Flux.range(1, 6)
                .doOnRequest(n -> log.info("Request {} number", n)) // 注意顺序造成的区别
                .publishOn(Schedulers.elastic())
                .doOnComplete(() -> log.info("Publisher Complete 1"))
                .map( i -> {
                            log.info("Publish {}, {}", Thread.currentThread(), i);
                            //return 10 / (i - 3);
                            return i;
                        }
                )
                .doOnComplete( () -> log.info("Publisher Complete 2"))
                .subscribeOn(Schedulers.single())
                .onErrorResume(e -> {
                    log.error("Exception {}", e.toString());
                    return Mono.just(-1);
                })
                //.onErrorReturn(-1)
                .subscribe( i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
                        e -> log.error("error {}", e.toString()),
                        () -> log.info("Subscriber Complete"),
                        s -> s.request(4)
                );

        Thread.sleep(2000);
    }

}
