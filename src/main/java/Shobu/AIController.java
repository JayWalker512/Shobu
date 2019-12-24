package Shobu;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class AIController {
    private List<Process> subprocesses = new ArrayList<>();
    private List<OutputStream> subprocessInput = new ArrayList<>();
    private List<InputStream> subprocessOutput = new ArrayList<>();
    private List<String> errors = new ArrayList<>();

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }

    AIController(List<String> programNames, List<String> extensions) {
        // execute the program names according to their extensions, and populate subprocesses;

        ListIterator<String> programNamesIterator = programNames.listIterator();
        ProcessBuilder pb;
        while (programNamesIterator.hasNext()) {
            String ext = extensions.get(programNamesIterator.nextIndex());
            String programName = programNamesIterator.next(); // This string includes the program name and it's arguments
            if (ext == "jar") {
                pb = new ProcessBuilder("java -jar " + programName);
            } else if (ext == "py") {
                pb = new ProcessBuilder("python3 " + programName);
            } else {
                List<String> programAndArgs = Arrays.asList(programName.split("\\s+"));
                pb = new ProcessBuilder(programAndArgs);
            }
            try {
                Process p = pb.start();
                subprocesses.add(p);
                subprocessInput.add(p.getOutputStream());
                subprocessOutput.add(p.getInputStream());
            } catch (Exception e) {
                this.errors.add("Couldn't start subprocess " + programName + "! " + e.getMessage());
            }
        }

        // Experimental code
        /*
        String inputString = "lol"; // getStdin();
        pb = new ProcessBuilder("cat");
        Process p;
        try {
            p = pb.start();
        } catch (Exception e) {
            System.out.println("Couldn't start subprocess.");
            return;
        }
        if (p == null) {
            return;
        }
        OutputStream subprocessInput = p.getOutputStream();
        InputStream subprocessOutput = p.getInputStream();
        int bytesActuallyRead = 0;
        while (bytesActuallyRead < 30) {
            try {
                subprocessInput.write(inputString.getBytes());
                subprocessInput.flush();
            } catch (Exception e) {
                System.out.println("Error writing to subprocess stdin.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            byte b[] = new byte[2];
            try {
                while (subprocessOutput.available() > 0) {
                    subprocessOutput.read(b, 1, 1);
                    bytesActuallyRead += 1;
                    break;
                }
                sb.append(new String(b));
            } catch (Exception e) {
                System.out.println("Error reading subprocess stdout");
                return;
            }
            System.out.print(sb.toString());
        }
        p.destroy();*/

    }

    //Send a JSON message to a subprocess
    public boolean sendStringToSubprocess(int processIndex, String messageToSend) {
        if (this.subprocessInput.get(processIndex) == null) {
            return false;
        }
        try {
            this.subprocessInput.get(processIndex).write(messageToSend.getBytes());
            this.subprocessInput.get(processIndex).flush();
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /** This method reads the output from a subprocess until it determines that it has received a complete
     * JSON object. It does this by matching curly braces { } and with no other method or validation of the JSON
     * structure. That's up to the caller! It then returns the String containing this object.
     * @param processIndex
     * @return
     */
    public String getNextJsonFromSubprocess(int processIndex) {
        if (this.subprocessOutput.get(processIndex) == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        InputStream processOutput = this.subprocessOutput.get(processIndex);
        int byteRead = 0;
        try {
            while (processOutput.available() == 0) {
                byteRead = 0; // just loop and wait until input comes.
            }
            boolean objectStarted = false;
            boolean objectFinished = false;
            Stack<Character> stack = new Stack<>();

            // TODO FIXME I should make this just block on .read() instead of busy waiting.
            while (objectFinished == false) {
                if (processOutput.available() > 0) {
                    byteRead = processOutput.read();
                    char asciiCharRead = (char)byteRead;
                    JSON_DELIMITERS delimReceived = matchCurlyBraces(stack, asciiCharRead);
                    if (delimReceived == JSON_DELIMITERS.STARTED_OBJECT) {
                        objectStarted = true;
                    }
                    if (delimReceived == JSON_DELIMITERS.FINISHED_OBJECT) {
                        objectFinished = true;
                    }
                    if (objectStarted) {
                        sb.append(asciiCharRead);
                    }
                }
            }
        } catch (Exception e) {
            return "";
        }
        return sb.toString();
    }

    enum JSON_DELIMITERS {
        STARTED_OBJECT,
        FINISHED_OBJECT,
        NOT_RECOGNIZED
    }

    // Only making this public so I can write a test for it.
    public static JSON_DELIMITERS matchCurlyBraces(Stack<Character> stack, char nextChar) {
        if (stack.empty()) {
            if (nextChar == '{') {
                stack.push('{');
                return JSON_DELIMITERS.STARTED_OBJECT;
            }
        }
        if (nextChar == '{') {
            stack.push('{');
        } else if (nextChar == '}') {
            stack.pop();
            if (stack.empty()) {
                return JSON_DELIMITERS.FINISHED_OBJECT;
            }
        }
        return JSON_DELIMITERS.NOT_RECOGNIZED;
    }



}
