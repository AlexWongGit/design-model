package org.alex;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.alex.service.Component;
import org.alex.service.ConcreteComponent;
import org.alex.service.ConcreteDecorator;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * Unit test for simple App.
 */
@SpringBootTest(classes = App.class)
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    @org.junit.Test
    public void mfs()
    {
        Component component = new ConcreteDecorator(new ConcreteComponent());
        component.operation();
    }
}
