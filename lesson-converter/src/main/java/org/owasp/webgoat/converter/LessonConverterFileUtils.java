package org.owasp.webgoat.converter;


import com.google.common.base.Predicate;
import org.owasp.webgoat.plugins.PluginFileUtils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LessonConverterFileUtils extends PluginFileUtils {

    public static List<Path> findFiles(Path root, final Predicate<Path> predicate) throws IOException {
        final List<Path> files = new ArrayList<>();

        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (predicate.apply(file)) {
                    files.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return files;
    }

    public static List<Path> findFile(Path root, final String name) throws IOException {
        final List<Path> files = new ArrayList<>();

        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.getFileName().toString().equals(name)) {
                    files.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return files;
    }

    public static Path findSubDirectory(Path root, final String name) throws IOException {
        Objects.requireNonNull(root);
        Objects.requireNonNull(name);

        final Path[] directory = new Path[1];
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (directory[0] == null && name.equals(dir.getFileName().toString())) {
                    directory[0] = dir;
                    return FileVisitResult.TERMINATE;
                }
                return FileVisitResult.CONTINUE;
            }

        });
        return directory[0];
    }

    public static boolean hasParentDirectoryWithName(Path currentFile, String nameOfParentDirectory,
                                                     boolean onlyOneLevel) {
        if (onlyOneLevel) {
            return currentFile.getParent().getFileName().toString().equals(nameOfParentDirectory);
        } else {
            return hasParentDirectoryWithName(currentFile, nameOfParentDirectory);
        }
    }

    public static boolean hasParentDirectoryWithName(Path currentFile, Path nameOfParentDirectory,
                                                     boolean onlyOneLevel) {
        return hasParentDirectoryWithName(currentFile, nameOfParentDirectory.getFileName().toString(), onlyOneLevel);
    }

    public static void copyTo(Path source, Path targetDirectory, StandardCopyOption... options) throws IOException {
        Path target = targetDirectory.resolve(source.getFileName());
        Logger.log("Copying '%s' to '%s'", source, target);
        Files.copy(source, target, options);
    }

}