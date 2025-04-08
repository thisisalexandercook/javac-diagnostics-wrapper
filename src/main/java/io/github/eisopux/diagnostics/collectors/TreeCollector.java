package io.github.eisopux.diagnostics.collectors;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreeScanner;

import java.util.*;
import java.util.stream.Collectors;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import io.github.eisopux.diagnostics.core.Collector;
import io.github.eisopux.diagnostics.core.CompilationReportData;
import io.github.eisopux.diagnostics.core.CompilationTaskBuilder;

public class TreeCollector implements Collector {

    private javax.tools.DiagnosticCollector<JavaFileObject> diagCollector;
    private final List<Map<String, Object>> treeEntries = new ArrayList<>();

    @Override
    public void onBeforeCompile(CompilationTaskBuilder builder) {
        diagCollector = builder.getOrCreateDiagnosticListener();

        if (diagCollector == null) {
            System.out.println("oops");
        }
    }

    @Override
    public void onAfterCompile(
            CompilationReportData reportData, Iterable<? extends CompilationUnitTree> asts) {

        Map<String, CompilationUnitTree> astMap = new HashMap<>();

        for (CompilationUnitTree ast : asts) {
            String uri = ast.getSourceFile().toUri().toString();
            astMap.put(uri, ast);
        }

        List<Diagnostic<? extends JavaFileObject>> diagnostics = diagCollector.getDiagnostics();

        Set<String> diagnosticSources =
                diagnostics.stream()
                        .filter(diag -> diag.getSource() != null)
                        .map(diag -> diag.getSource().toUri().toString())
                        .collect(Collectors.toSet());

        for (String sourceUri : diagnosticSources) {
            CompilationUnitTree ast = astMap.get(sourceUri);
            if (ast != null) {
                new DiagnosticTreeVisitor(sourceUri).scan(ast, null);
            }
        }

        reportData.putSection("tree", treeEntries);
    }

    private class DiagnosticTreeVisitor extends TreeScanner<Void, Void> {
        private final String sourceUri;

        public DiagnosticTreeVisitor(String sourceUri) {
            this.sourceUri = sourceUri;
        }

        @Override
        public Void visitMethod(MethodTree methodTree, Void v) {

            String methodName = methodTree.getName().toString();

            Map<String, Object> entry = new HashMap<>();
            entry.put("nodeType", "Method");
            entry.put("name", methodName);
            entry.put("source", sourceUri);

            treeEntries.add(entry);

            return super.visitMethod(methodTree, null);
        }
    }
}
