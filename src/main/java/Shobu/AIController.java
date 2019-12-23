package Shobu;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class AIController {
    private List<Process> subprocesses = new ArrayList<>();
    private List<OutputStream> subprocessInput = new ArrayList<>();
    private List<InputStream> subprocessOutput = new ArrayList<>();

    AIController(List<String> programNames, List<String> extensions) {
        // execute the program names according to their extensions, and populate subprocesses;

        ListIterator<String> programNamesIterator = programNames.listIterator();
        ProcessBuilder pb;
        while (programNamesIterator.hasNext()) {
            String ext = extensions.get(programNamesIterator.nextIndex());
            String programName = programNamesIterator.next();
            if (ext == "jar") {
                pb = new ProcessBuilder("java -jar " + programName);
            } else if (ext == "py") {
                pb = new ProcessBuilder("python3 " + programName);
            } else {
                pb = new ProcessBuilder(programName);
            }
            try {
                Process p = pb.start();
                subprocesses.add(p);
                subprocessInput.add(p.getOutputStream());
                subprocessOutput.add(p.getInputStream());
            } catch (Exception e) {
                System.out.println("Couldn't start subprocess " + programName + "! " + e.getMessage());
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



}
