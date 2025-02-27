package io.github.wmdietl.diagnostics.json.sarif;

import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import io.github.wmdietl.diagnostics.JavacDiagnosticsWrapper;

public class Main extends JavacDiagnosticsWrapper {
    public static void main(String[] args) {
        new Main().run(args);
    }

    @Override
    protected void processDiagnostics(
            boolean result, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        diagnostics.forEach(d -> System.out.println(d.getMessage(null)));
        System.out.println("\n--- Captured Processor Info ---");
        System.out.println(capturedProcessorInfo);
    }
}
