package net.prominic.groovyls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.shared.invoker.InvocationOutputHandler;

public class JarFileListInvokerHandler implements InvocationOutputHandler {

    private List<String> classpathList = new ArrayList<>();
    private boolean nextLineIsResult;

    @Override
    public void consumeLine(String line) throws IOException {
        System.out.println("line:" + line);
        if (line.contains("Dependencies classpath:")) {
            nextLineIsResult = true;
            return;
        } else if (nextLineIsResult) {
            System.out.println("this line is result");
            this.classpathList = Arrays.stream(line.split(":")).toList();
            nextLineIsResult = false;
        }
    }

    public List<String> getClasspathList() {
        return this.classpathList;
    }
}
