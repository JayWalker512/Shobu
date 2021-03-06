package Shobu;

import java.util.*;

public class Options {
    private List<String> programNames;
    private List<String> extensions;
    private boolean jsonPassThrough = false;
    private boolean isValid = false;

    public boolean jsonPassThrough() {
        return jsonPassThrough;
    }

    public boolean isValid() {
        return isValid;
    }

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
        if (args.length < 1) {
            return; // isValid = false;
        }

        if (args.length == 1) {
            if (args[0].equals("--json-pass-through")) {
                jsonPassThrough = true;
                isValid = true;
                return;
            }
        }

        if (args.length != 2) { return; }

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
