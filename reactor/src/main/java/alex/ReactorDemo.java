package alex;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactorDemo {

    public static void main(String[] args) {
        // from工厂创建序列
        Flux.from((Publisher<String>) subscriber -> {
            for (int i = 0; i < 10; i++) {
                subscriber.onNext("hello" + i);
            }
        }).subscribe(
                System.out::println,
                System.err::println,
                () -> System.out.println("done")
        );


        requestData("big").subscribe(System.out::println);

        // defer创建序列
        // 不订阅不会走判断的逻辑，延迟
        requestDeferData(null);
                //.subscribe(System.out::println);

    }

    static boolean isValid(String s) {
        System.out.println("调用isValid方法");
        return s  != null;
    }

    static String getData(String s) {
        System.out.println("调用getData方法");
        return s + s;
    }

    static Mono<String> requestData(String s) {
        System.out.println("调用requestData方法");
        return isValid(s) ? Mono.fromCallable(() -> getData(s)) : Mono.just("echo");
    }

    static Mono<String> requestDeferData(String s) {
        System.out.println("调用requestDeferData方法");
        return Mono.defer(() -> isValid(s) ? Mono.fromCallable(() -> getData(s)) : Mono.error(new RuntimeException()));
    }
}
