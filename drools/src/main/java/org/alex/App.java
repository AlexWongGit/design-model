package org.alex;

import org.alex.config.DroolsConfig;
import org.alex.service.SPIDemoInterface;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.util.ServiceLoader;


/**
 * Hello world!
 */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        ServiceLoader<SPIDemoInterface> load = ServiceLoader.load(SPIDemoInterface.class);
        for (SPIDemoInterface spiDemoInterface : load) {
            spiDemoInterface.upload("filepath");
        }
    }



}
