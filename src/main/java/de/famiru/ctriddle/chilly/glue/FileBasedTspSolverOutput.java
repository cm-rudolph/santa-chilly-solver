package de.famiru.ctriddle.chilly.glue;

import de.famiru.ctriddle.chilly.Matrix;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;

abstract class FileBasedTspSolverOutput implements TspSolverOutput {
    private final String fileName;

    FileBasedTspSolverOutput(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void output(Matrix matrix) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writeData(writer, matrix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        instructUser();
    }

    protected abstract void writeData(PrintWriter writer, Matrix matrix);

    protected void instructUser() {
    }
}
