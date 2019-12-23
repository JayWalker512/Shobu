package Shobu;

import org.junit.Test;

import java.util.Arrays;
import java.util.Stack;

import static org.junit.Assert.*;

public class AIControllerTest {

    @Test
    public void testSpawningSubprocesses() {
        String[] programs = {"cat", "cat"};
        String[] extensions = {"", ""};
        AIController aic = new AIController(Arrays.asList(programs), Arrays.asList(extensions));
        assertEquals(0, aic.getErrors().size());
    }

    @Test
    public void testSendingAndReceivingMessages() {
        String[] programs = {"cat", "cat"};
        String[] extensions = {"", ""};
        AIController aic = new AIController(Arrays.asList(programs), Arrays.asList(extensions));

        // Send message 1
        aic.sendStringToSubprocess(0, "hellox");

        // Assert that we receive the same message back
        String receivedMessage = "";
        while (receivedMessage == "") {
            receivedMessage = aic.getNextJSONFromSubprocess(0);
        }
        assertEquals("hellox", receivedMessage);

        // Send message 2
        aic.sendStringToSubprocess(0, "worldx");

        // Assert that we receive the same message back
        receivedMessage = "";
        while (receivedMessage == "") {
            receivedMessage = aic.getNextJSONFromSubprocess(0);
        }
        assertEquals("worldx", receivedMessage);

        // Send message to second subprocess
        aic.sendStringToSubprocess(1, "worldx");

        // Assert that we receive the same message back
        receivedMessage = "";
        while (receivedMessage == "") {
            receivedMessage = aic.getNextJSONFromSubprocess(1);
        }
        assertEquals("worldx", receivedMessage);

    }

    @Test
    public void testCurlyBraceMatching() {
        String inputString = "xxyy { this is { the } object }";
        char[] charArray = inputString.toCharArray();
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < charArray.length; i++) {
            AIController.JSON_DELIMITERS del = AIController.matchCurlyBraces(stack, charArray[i]);
            if (i == 5) {
                assertEquals(AIController.JSON_DELIMITERS.STARTED_OBJECT, del);
            }
            if (i == inputString.length() - 1) {
                assertEquals(AIController.JSON_DELIMITERS.FINISHED_OBJECT, del);
            }
        }
    }

}
