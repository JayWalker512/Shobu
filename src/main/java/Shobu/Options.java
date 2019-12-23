package Shobu;

import java.util.*;

public class Options {
    private List<String> programNames;
    private List<String> extensions;

    public boolean isValid() {
        return isValid;
    }

    private boolean isValid = false;

    public List<String> getProgramNames() {
        return Collections.unmodifiableList(this.programNames);
    }

    public List<String> getExtensions() {
        return Collections.unmodifiableList(this.extensions);
    }

    private Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    Options(String[] args) {
        if (args.length < 2) {
            return; // isValid = false;
        }

        List<String> programNames = Arrays.asList(args);
        this.programNames = Collections.unmodifiableList(programNames);

        List<String> extList = new ArrayList<>();
        ListIterator<String> li = programNames.listIterator();
        while (li.hasNext()) {
            Optional<String> ext = getExtensionByStringHandling(li.next());
            if (ext.isPresent()) {
                extList.add(ext.get());
            } else {
                extList.add("");
            }
        }
        this.extensions = Collections.unmodifiableList(extList);

        if (this.programNames.size() == this.extensions.size()) {
            this.isValid = true;
        }

    }
}
