package net.prominic.groovyls;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;

public class POMFileResolver {

    private final Path workspaceRoot;
    private final JarFileListInvokerHandler handler = new JarFileListInvokerHandler();

    public POMFileResolver(Path workspaceRoot) {
        this.workspaceRoot = workspaceRoot;
    }

    public List<String> getResolvedClasspath() {
        Path pomFile = workspaceRoot.resolve("./pom.xml");

        if (!Files.exists(pomFile)) {
            return Collections.emptyList();
        }

        InvocationRequest request = new DefaultInvocationRequest();

        request.setPomFile(pomFile.toFile())
            .setGoals(Collections.singletonList("dependency:build-classpath"))
            .setOutputHandler(handler);

        Invoker invoker = new DefaultInvoker();

        try {

            invoker.execute(request);

            System.out.println("Resolved pom.xml classpath:");
            System.out.println(handler.getClasspathList());

            return handler.getClasspathList();

        } catch (Exception e) {
            throw new RuntimeException("Cannot invoke Maven to get classpath");
        }
    }
}
