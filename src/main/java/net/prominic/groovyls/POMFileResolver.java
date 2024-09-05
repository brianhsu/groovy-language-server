package net.prominic.groovyls;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;

public class POMFileResolver {

    private final Path workspaceRoot;
    private final Optional<String> mavenExecutable;
    private final JarFileListInvokerHandler handler = new JarFileListInvokerHandler();

    public POMFileResolver(Path workspaceRoot, Optional<String> mavenExecutable) {
        this.workspaceRoot = workspaceRoot;
        this.mavenExecutable = mavenExecutable;
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

        this.mavenExecutable.map(File::new).ifPresent(invoker::setMavenExecutable);

        try {

            invoker.execute(request);

            System.out.println("Resolved pom.xml classpath:");
            System.out.println(handler.getClasspathList());

            return handler.getClasspathList();

        } catch (Exception e) {
            throw new RuntimeException("Cannot invoke Maven to get classpath", e);
        }
    }
}
