package com.alex;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class Completion {

    /**
     * CompletionStage的接口方法可以从多种角度进行分类，从最宏观的横向划分，CompletionStage的接口主要分三类：
     * 一、产出型或者函数型：就是用上一个阶段的结果作为指定函数的参数执行函数产生新的结果。这一类接口方法名中基本都有apply字样，接口的参数是(Bi)Function类型。
     *
     * 二、消耗型或者消费型：就是用上一个阶段的结果作为指定操作的参数执行指定的操作，但不对阶段结果产生影响。这一类接口方法名中基本都有accept字样，接口的参数是(Bi)Consumer类型。
     *
     * 三、不消费也不产出型：就是不依据上一个阶段的执行结果，只要上一个阶段完成（但一般要求正常完成），就执行指定的操作，且不对阶段的结果产生影响。这一类接口方法名中基本都有run字样，接口的参数是Runnable类型。
     *
     * 还有一组特别的方法带有compose字样，它以依赖阶段本身作为参数而不是阶段产生的结果进行产出型（或函数型）操作。
     *
     * 在以上三类横向划分方法的基础上，又可以按照以下的规则对这些接口方法进行纵向的划分：
     * 一、多阶段的依赖：一个阶段的执行可以由一个阶段的完成触发，或者两个阶段的同时完成，或者两个阶段中的任何一个完成。
     *
     * 方法前缀为then的方法安排了对单个阶段的依赖。
     * 那些由完成两个阶段而触发的，可以结合他们的结果或产生的影响，这一类方法带有combine或者both字样。
     * 那些由两个阶段中任意一个完成触发的，不能保证哪个的结果或效果用于相关阶段的计算，这类方法带有either字样。
     * 二、按执行的方式：阶段之间的依赖关系控制计算的触发，但不保证任何特定的顺序。因为一个阶段的执行可以采用以下三种方式之一安排：
     *
     * 默认的执行方式。所有方法名没有以async后缀的方法都按这种默认执行方式执行。
     * 默认的异步执行。所有方法名以async为后缀，但没有Executor参数的方法都属于此类。
     * 自定义执行方式。所有方法名以async为后缀，并且具有Executor参数的方法都属于此类。
     * 默认的执行方式（包括默认的异步执行）的执行属性由CompletionStage的实现类指定例如CompletableFuture，
     * 而自定义的执行方式的执行属性由传入的Executor指定，这可能具有任意的执行属性，甚至可能不支持并发执行，但还是被安排异步执行。
     *
     * 三、按上一个阶段的完成状态：无论触发阶段是正常完成还是异常完成，都有两种形式的方法支持处理。
     *
     * 不论上一个阶段是正常还是异常完成：
     * whenComplete方法可以在上一个阶段不论以何种方式完成的处理，但它是一个消费型接口，即不对整个阶段的结果产生影响。
     * handle前缀的方法也可以在上一个阶段不论以何种方式完成的处理，它是一个产出型（或函数型）接口，既可以由上一个阶段的异常产出新结果，也可以其正常结果产出新结果，使该结果可以由其他相关阶段继续进一步处理。
     * 上一个阶段是异常完成的时候执行：exceptionally方法可以在上一个阶段以异常完成时进行处理，它可以根据上一个阶段的异常产出新的结果，使该结果可以由其他相关阶段继续进一步处理。
     * CompletionStage的异常规则
     * 除了whenComplete不要求其依赖的阶段是正常完成还是异常完成，以及handle前缀的方法只要求其依赖的阶段异常完成之外，其余所有接口方法都要求其依赖的阶段正常完成。
     *
     * 如果一个阶段的执行由于一个(未捕获的)异常或错误而突然终止，那么所有要求其完成的相关阶段也将异常地完成，并通过CompletionException包装其具体异常堆栈。
     * 如果一个阶段同时依赖于两个阶段，并且两个阶段都异常地完成，那么CompletionException可以对应于这两个异常中的任何一个。
     * 如果一个阶段依赖于另外两个阶段中的任何一个，并且其中只有一个异常完成，则不能保证依赖阶段是正常完成还是异常完成。
     * 在使用方法whenComplete的情况下，当提供的操作本身遇到异常时，如果前面的阶段没有异常完成，则阶段将以其异常作为原因异常完成。
     * 所有方法都遵循上述触发、执行和异常完成规范，此外，虽然用于传递一个表示完成结果的参数（也就是说，对于T类型的参数）可以为null，但是如果为其它任何参数传递null都将导致NullPointerException。
     * 此接口不定义用于初始创建、强制正常或异常完成、探测完成状态或结果或等待阶段完成的方法。CompletionStage的实现类可以提供适当的方法来实现这些效果。
     * 方法 toCompletableFuture 通过提供一个公共转换类型，支持该接口的不同实现之间的互操作性。
     */

    public static void main(String[] args) {
        // 根据阶段正常完成结果的产出型（或者叫函数型）
        // 这一类方法都由上一阶段（或者两个阶段，或者两个阶段中的任意一个）的正常完成结果触发，
        // 然后以该结果执行给定的函数，产出新的结果。
        // 这里把异步执行的两者形式也列举出来了。
        System.out.println("---------thenApply---------");
        thenApply();
        System.out.println("---------thenCombine---------");
        thenCombine();
        System.out.println("---------applyToEither---------");
        for (int i = 0; i < 10; i++) {
            applyToEither();
        }

        // 根据阶段正常完成结果的消费型
        // 这一类方法都由上一阶段（或者两个阶段，或者两个阶段中的任意一个）正常完成的结果触发，
        // 然后以该结果执行给定的操作action，但不会对阶段的结果进行影响。
        // 这里把异步执行的两者形式也列举出来了。
        System.out.println("---------thenAccept---------");
        thenAccept();
        System.out.println("---------thenAcceptBoth---------");
        thenAcceptBoth();
        System.out.println("---------acceptEither---------");
        for (int i = 0; i < 10; i++) {
            acceptEither();
        }

        // 只要求依赖的阶段正常完成的不消耗也不产出型
        // 这一类方法只要求上一阶段（或者两个阶段，或者两个阶段中的任意一个）正常完成，
        // 并不关心其具体结果，从而执行指定的操作cation，但不会对阶段的结果进行影响。
        // 这里把异步执行的两者形式也列举出来了。
        System.out.println("---------thenRun---------");
        thenRun();
        System.out.println("---------runAfterBoth---------");
        runAfterBoth();
        System.out.println("---------runAfterEither---------");
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            runAfterEither();
        }

        // 根据正常完成的阶段本身而不是其结果的产出型
        // 产出型的方法都是应用依赖阶段的正常执行结果，CompletionStage提供了一组以阶段本身为依据的产出型接口方法
        System.out.println("---------thenCompose---------");
        thenCompose();

        // 不论阶段正常还是异常完成的消耗型
        // 上面的一、二、三、四种类型的方法都需要依赖的阶段正常完成，如果异常完成将导致上面介绍的四种类型的方法最终也异常完成，不会得出我们希望的结果。
        // 而whenComplete则不论依赖的上一个阶段是正常完成还是异常完成都不会影响它的执行，但它是一个消耗型接口，即不会对阶段的原来结果产生影响，
        // 结合thenCombine综合whenComplete的示例如下
        System.out.println("---------whenComplete---------");
        whenComplete();

        // 不论阶段正常还是异常完成的产出型
        // whenComplete是对不论依赖阶段正常完成还是异常完成时的消耗或者消费，即不会改变阶段的现状，
        // 而handle前缀的方法则是对应的产出型方法，即可以对正常完成的结果进行转换，也可以对异常完成的进行补偿一个结果，即可以改变阶段的现状。
        System.out.println("---------handle---------");
        handle();

        // 异常完成的产出型
        // CompletionStage还提供了一个仅当上一个阶段异常完成时的处理，并且可以修改阶段的结果
        System.out.println("---------exceptionally---------");
        exceptionally();

        // 实现该接口不同实现之间互操作的类型转换方法
        // 返回一个与此阶段保持相同完成属性的CompletableFuture实例。如果此阶段已经是一个CompletableFuture，那么直接返回该阶段本身，
        // 否则此方法的调用可能等效于thenApply(x -> x)，但返回一个类型为CompletableFuture的实例。
        // 不选择实现该互操作性的CompletionStage实现，可能会抛出UnsupportedOperationException异常。
        System.out.println("---------toCompletableFuture---------");

        System.out.println("------Supplier------");
        Supplier<Double> supplier = ()->new Random().nextDouble();
        System.out.println(supplier.get());


    }

    public static void thenApply() {
        CompletableFuture<String> stage = CompletableFuture.supplyAsync(() -> "hello");
        String result = stage.thenApply(s -> s + " world").join();
        System.out.println(result);
    }

    public static void thenCombine() {
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Leehom";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Wang";
        }), (s1, s2) -> s1 + " " + s2).join();
        System.out.println(result);
    }

    public static void applyToEither() {
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Tom";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "John";
        }), s -> "hello " + s).join();
        System.out.println(result);
    }

    public static void thenAccept() {
        CompletableFuture.supplyAsync(() -> "hello").thenAccept(s -> System.out.println(s + " Alex"));
    }

    public static void thenAcceptBoth() {
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Big";
        }).thenAcceptBoth(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Foot";
        }), (s1, s2) -> System.out.println(s1 + " " + s2)).join();

    }

    public static void acceptEither() {
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello Chandler";
        }).acceptEither(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello Joey";
        }), System.out::println).join();
    }

    public static void thenRun(){
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello";
        }).thenRun(() -> System.out.println("happy NewYear"));
    }

    public static void runAfterBoth(){
        //不关心这两个CompletionStage的结果，只关心这两个CompletionStage正常执行完毕，之后在进行操作（Runnable）。
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "s1";
        }).runAfterBoth(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "s2";
        }), () -> System.out.println("hello AAA"));
    }

    public static void runAfterEither() {
        //两个CompletionStage，任何一个正常完成了都会执行下一步的操作（Runnable）。
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "s1";
        }).runAfterEither(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "s2";
        }), () -> System.out.println("hello BBB")).join();
    }

    /**
     * @description 相比thenCombine更简洁
     * @return void
     */
    public static void thenCompose(){
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Breaking";
        }).thenCompose(s -> CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return s + " News";
        })).join();

        System.out.println(result);
    }

    public static void whenComplete(){
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (1 == 1) {
                throw new RuntimeException("测试一下异常情况");
            }

            return "hello ";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("return world...");  //会执行
            return "world";
        }), (s1, s2) -> {
            String s = s1 + " " + s2;   //并不会执行
            System.out.println("combine result :"+s); //并不会执行
            return s;
        }).whenComplete((s, t) -> {
            System.out.println("current result is :" +s);
            if(t != null){
                System.out.println("阶段执行过程中存在异常：");
                t.printStackTrace();
            }
        }).join();

        System.out.println("final result:"+result); //并不会执行
    }

    public static void handle() {
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //出现异常
            if (1 == 3) {
                throw new RuntimeException("测试一下异常情况");
            }
            return "Tom";
        }).handle((s, t) -> {
            if (t != null) { //出现异常了
                return "John";
            }
            return s; //这里也可以对正常结果进行转换
        }).join();
        System.out.println(result);
    }


    public static void exceptionally() {
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (1 == 1) {
                throw new RuntimeException("测试一下异常情况");
            }
            return "s1";
        }).exceptionally(e -> {
            e.printStackTrace(); //e肯定不会null
            return "hello world"; //补偿返回
        }).join();
        System.out.println(result); //打印hello world
    }
}
