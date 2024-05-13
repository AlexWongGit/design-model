package alex.mono;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class MonoDemo {

    public static void main(String[] args) {

        //Mono创建
        createMono();

        //操作mono
        operateMono();
    }

    private static void createMono() {

        Mono.just(1)
                .mergeWith(Flux.just(1, 2, 3))
                .subscribe(System.out::println);

        System.out.println();

        Mono.fromSupplier(() -> "Hello")
                .subscribe(System.out::println);

        Mono.justOrEmpty(Optional.of("Hello"))
                .subscribe(System.out::println);

        Mono.create(monoSink -> monoSink.success("Hello"))
                .subscribe(System.out::println);
    }

    private static void operateMono() {

        // zip 将多个Mono合并成一个Mono，类似flatmap
        Mono.zip(string -> string.length, Mono.just(1), Mono.just(2))
                .subscribe(System.out::println);
        System.out.println("---------- 分割线2 ----------");

        // then 当订阅成功或失败后 返回另外一个publisher
        Mono.zip(string -> string.length, Mono.just(1))
                .map(getIntegerIntegerFunction())
                .doOnSuccess(ignore -> System.out.println("成功了"))
                .doOnTerminate(() -> System.out.println("结束了"))
                .then(Mono.defer(() -> Mono.just(888)))
                .subscribe(System.out::println);
        System.out.println("---------- 分割线3 ----------");

        //冷/懒加载 当deferMono被订阅时，才会触发1的feeService()
/*        Mono.defer(() -> Mono.just(feeService())) // 1
                .map(integer -> integer + 1)
                .map(sleep())
                .subscribe(System.out::println);*/
        Mono.defer(() -> Mono.just(feeService()))
                .map(integer -> integer + 1)
                .subscribe(System.out::println);

        //热加载 Mono.just时，直接触发了1的feeService()
/*        Mono.just(feeService()) //1
                .map(integer -> integer + 1)
                .map(sleep())
                .subscribe(System.out::println);*/
        Mono.just(feeService())
                .map(integer -> integer + 1)
                .subscribe(System.out::println);
        System.out.println("---------- 分割线4 ----------");


        Mono.delay(Duration.ofMillis(3)).subscribe(System.out::println);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("---------- 分割线5 ----------");

        // using在订阅者取消或者异常时 能执行3将资源清理
        // true&false暂时没看到区别
        // 1 数据源
        /*
        这段代码使用了Mono.using()来创建一个Mono流，其中包括了资源的初始化、结果的处理、资源的清理等步骤。
        在这段代码中，资源的初始化方法 (() -> 1) 返回的是1，结果处理方法 (integer -> Mono.just(2 + integer))
         返回的是2加上传入的参数值，资源清理方法 (integerSource -> System.out.println(“清理结果是：” + integerSource))
         在订阅完成后会执行，而完成前调用参数设为false。
         接着通过flatMap()方法对结果进行处理，对流中的元素进行加3的操作，
         最后通过subscribe()方法订阅并打印最终结果。这段代码中主要展示了使用Mono.using()来管理资源的流程，
         以及对结果进行操作的过程。
        * */
        Mono.using(() -> 1,
                        // 2 最终返回
                        integer -> Mono.just(2 + integer),
                        //3 根据4执行3
                        integerSource -> System.out.println("清理结果是：" + integerSource),
                        // 4 完成前调用 还是 完成后调用 true 完成前调用 false 完成后调用
                        false)
                .flatMap(integer -> Mono.just(integer + 3))
                .subscribe(integer -> System.out.println("最终结果是：" + integer));
        System.out.println("---------- 分割线6 ----------");

        // 1 数据源
        Mono.using(() -> 1,
                        // 2 最终返回
                        integer -> Mono.just(2 + integer),
                        //3 根据4执行3
                        integerSource -> System.out.println("清理结果是：" + integerSource),
                        // 4 完成前调用 还是 完成后调用 true 完成前调用 false 完成后调用
                        true)
                .flatMap(integer -> Mono.just(integer + 3))
                .subscribe(integer -> System.out.println("最终结果是：" + integer));

        Mono<Void> when = Mono.when();
        Flux.zip(Mono.just(1), Mono.just(2), (m1, m2) -> m1 + m2).subscribe(System.out::println);
        System.out.println("---------- 分割线7 ----------");
        //Mono.when(Mono.just(1), Mono.just(2), (m1, m2) -> m1 + m2).subscribe(System.out::println);
        Mono.just(1).concatWith(Mono.just(2)).subscribe(System.out::println);


    }

    private static Integer feeService() {
        //doSomething
        //call db
        //calculate fee
        return 1;
    }

    private static Function<Integer, Integer> getIntegerIntegerFunction() {
        return integer -> {
            integer = integer + 1;
            System.out.println("----->" + integer);
            return integer;
        };
    }

    private static Function<Integer, Integer> sleep() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return integer -> {
            integer = integer + 1;
            System.out.println("----->" + integer);
            return integer;
        };
    }
}
