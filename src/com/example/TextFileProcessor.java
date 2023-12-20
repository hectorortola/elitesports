package com.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class TextFileProcessor {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\hecto\\Desktop\\prueba elite sports\\elitesports\\Alice.txt";

        try {
            List<String> lines = readLinesFromFile(filePath);
            System.out.println("Total number of lines: " + lines.size());

            String longestWord = findLongestWord(lines);
            System.out.println("Longest word: " + longestWord + " (" + longestWord.length() + " characters)");

            String htmlContent = parseToHtml(lines);
            saveHtmlToFile(htmlContent, "Alice.html");

        } catch (NoSuchFileException nsfe) {
            System.err.println("File not found: " + nsfe.getFile());
        } catch (IOException e) {
            System.err.println("An error occurred while processing the file.");
        }
    }

    private static List<String> readLinesFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllLines(path);
    }

    private static void saveHtmlToFile(String content, String fileName) throws IOException {
        Path path = Paths.get(fileName);
        Files.write(path, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static String parseToHtml(List<String> lines) {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html>\n<body>\n");

        String indexContent = generateIndex(lines);
        htmlContent.append(indexContent);

        int totalChapters = 0;
        for (String line : lines) {
            totalChapters = processLine(htmlContent, line, totalChapters);
        }

        htmlContent.append("</body>\n</html>\n");
        return htmlContent.toString();
    }

    private static int processLine(StringBuilder htmlContent, String line, int totalChapters) {

        if (line.startsWith("CHAPTER")) {
            totalChapters += 1;
            htmlContent.append("</br>");
            htmlContent.append("<h1 id=\"chapter-").append(totalChapters).append("\">").append(line).append("</h1>\n");
        } else {
            htmlContent.append("<p>").append(line).append("</p>\n");
        }
        return totalChapters;
    }

    private static String findLongestWord(List<String> lines) {
        String longestWord = "";

        for (String line : lines) {
            String[] words = line.split("\\s+");

            for (String word : words) {
                String cleanedWord = word.replaceAll("[^a-zA-Z]", "");
                if (cleanedWord.length() > longestWord.length()) {
                    longestWord = cleanedWord;
                }
            }
        }
        return longestWord;
    }

    private static String generateIndex(List<String> lines) {
        StringBuilder htmlIndex = new StringBuilder();
        htmlIndex.append("<h1>Index</h1>\n<ul>\n");

        int totalChapters = 0;
        for (String line : lines) {
            totalChapters = processIndexLine(htmlIndex, line, totalChapters);
        }

        htmlIndex.append("</ul>\n");
        return htmlIndex.toString();
    }

    private static int processIndexLine(StringBuilder htmlIndex, String line, int totalChapters) {
        if (line.startsWith("CHAPTER")) {
            totalChapters += 1;
            htmlIndex.append("<li><a href=\"#chapter-").append(totalChapters).append("\">").append(line).append("</a></li>\n");
        }
        return totalChapters;
    }
}