package net.prominic.groovyls;

import java.io.IOException;
import java.util.ArrayList;
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
        } else if (nextLineIsResult && line.contains(".jar")) {
            System.out.println("this line is result");
            for (String jarFile: line.split(":")) {
                this.classpathList.add(jarFile);
            }
            nextLineIsResult = false;
        }
    }

    public List<String> getClasspathList() {
        return this.classpathList;
    }
}
