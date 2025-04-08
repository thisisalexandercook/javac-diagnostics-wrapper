package io.github.eisopux.diagnostics.builtin;

import java.io.IOException;

import io.github.eisopux.diagnostics.collectors.DiagnosticCollector;
import io.github.eisopux.diagnostics.core.CompilerRunner;
import io.github.eisopux.diagnostics.reporter.JsonReporter;

/** A prebuilt JSON diagnostics output to call from the command line */
public class JsonDiagnostics {
    public static void main(String[] args) throws IOException {
        CompilerRunner runner =
                new CompilerRunner()
                        .addCollector(new DiagnosticCollector())
                        .setReporter(new JsonReporter());

        runner.run(args);
    }
}
