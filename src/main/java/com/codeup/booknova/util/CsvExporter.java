package com.codeup.booknova.util;

import com.codeup.booknova.domain.Book;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvExporter {

    private CsvExporter() {
    }

    public static Path exportBooks(List<Book> books, Path out) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(out)) {
            w.write("id,isbn,title,author,stock\n");
            for (Book b : books) {
                w.write(b.getId() + "," + esc(b.getIsbn()) + "," + esc(b.getTitle()) + "," + esc(b.getAuthor()) + "," + b.getStock() + "\n");
            }
        }
        return out;
    }


    /**
     * Recibe filas ya formateadas (por ejemplo, de LoanService.listOverdueLoans).
     */
    public static Path exportLines(List<String> lines, Path out, String header) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(out)) {
            w.write(header + "\n");
            for (String s : lines) w.write(s + "\n");
        }
        return out;
    }

    private static String esc(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n"))
            return "\"" + s.replace("\"", "\"\"") + "\"";
        return s;
    }
}