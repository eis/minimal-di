package fi.eis.libraries.di;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class SimpleLoggerTest {
    @Test
    public void testLogging() throws UnsupportedEncodingException {
        SimpleLogger logger = new SimpleLogger(this.getClass());
        logger.setDebug(true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        logger.setPrintOut(ps);
        logger.debugPrint("Hello");
        logger.debugPrint("How're you doing, %s?", "John");
        String result = baos.toString("UTF-8");
        Assert.assertThat(result, Matchers.containsString("DEBUG"));
        Assert.assertThat(result, Matchers.containsString("How're you doing, John?"));
    }
}
