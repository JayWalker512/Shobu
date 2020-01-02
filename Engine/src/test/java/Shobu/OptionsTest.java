package Shobu;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testOptionsCreation() {
        String[] args = {"mybot.jar", "otherbot.py"};
        Options opts = new Options(args);
        assertTrue(opts.isValid());
        assertTrue(opts.getProgramNames().size() == 2);
        assertTrue(opts.getExtensions().size() == 2);

        String[] extensions = {"jar", "py"};
        for (String e : Arrays.asList(extensions)) {
            assertTrue(opts.getExtensions().indexOf(e) != -1);
        }

        // Test json pass through option
        String[] args2 = {"--json-pass-through"};
        opts = new Options(args2);
        assertTrue(opts.isValid());
        assertTrue(opts.jsonPassThrough());
    }
}
