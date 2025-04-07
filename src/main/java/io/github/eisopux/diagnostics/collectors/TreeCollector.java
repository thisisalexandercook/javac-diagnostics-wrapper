package io.github.eisopux.diagnostics.collectors;

import javax.tools.JavaFileObject;

import io.github.eisopux.diagnostics.core.Collector;
import io.github.eisopux.diagnostics.core.CompilationReportData;
import io.github.eisopux.diagnostics.core.CompilationTaskBuilder;

public class TreeCollector implements Collector {

    private javax.tools.DiagnosticCollector<JavaFileObject> diagCollector;

    @Override
    public void onBeforeCompile(CompilationTaskBuilder builder) {
        diagCollector = builder.getOrCreateDiagnosticListener();

        if (diagCollector == null) {
            System.out.println("oops");
        }
    }

    @Override
    public void onAfterCompile(CompilationReportData reportData) {}
}
