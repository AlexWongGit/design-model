package alex;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAspectJAutoProxy
@MapperScan("alex.mapper")
@EnableAsync
@SpringBootApplication(scanBasePackages = {"alex"})
public class Application {


    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

}
