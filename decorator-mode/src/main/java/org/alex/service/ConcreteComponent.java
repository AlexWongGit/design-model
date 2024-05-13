package org.alex.service;

import org.springframework.context.annotation.Configuration;

/**
 * 具体构件
 * 抽象构件类的子类，用于定义具体的构件对象，实现了在抽象构件中声明的方法，装饰器可以给它增加额外的职责（方法）
 */
@Configuration
public class ConcreteComponent extends Component {
    @Override
    public void operation() {
        System.out.println("操作一下");
    }
}
