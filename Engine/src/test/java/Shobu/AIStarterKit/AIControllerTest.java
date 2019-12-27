package Shobu.AIStarterKit;

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
        String messageToSend = "garbage {\"name\":\"value\", \"bla\":{}} garbage {\"other\":1}";
        String jsonToGet = "{\"name\":\"value\", \"bla\":{}}";

        aic.sendStringToSubprocess(0, messageToSend);

        // Assert that we receive the first json object back
        String receivedMessage = "";
        while (receivedMessage == "") {
            receivedMessage = aic.getNextJsonFromSubprocess(0);
        }
        assertEquals(jsonToGet, receivedMessage);

        //Assert that we get the second one afterwards
        receivedMessage = "";
        jsonToGet = "{\"other\":1}";
        while (receivedMessage == "") {
            receivedMessage = aic.getNextJsonFromSubprocess(0);
        }
        assertEquals(jsonToGet, receivedMessage);

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
