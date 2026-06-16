package fi.eis.libraries.di.context.deployment;

import fi.eis.libraries.di.logger.SimpleLogger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * <p>
 * This class borrows heavily from JBoss Weld class FileSystemBeanArchiveHandler, which authors are:
 * Pete Muir, Marko Luksa, Martin Kouba
 * </p>
 * <p>
 * FileSystemBeanArchiveHandler has been licensed under Apache License,
 * Version 2.0. It permits releasing under different license.
 * (http://en.wikipedia.org/wiki/Comparison_of_free_and_open-source_software_licenses)
 * </p>
 *
 * @author eis
 */
class BeanArchive {
    private final List<Class> classes = new ArrayList<>();
    private final SimpleLogger logger = new SimpleLogger(this.getClass());

    public BeanArchive(URL url) {
        initArchive(url);
    }
    public BeanArchive(URL url, SimpleLogger.LogLevel logLevel) {
        setLogLevel(logLevel);
        initArchive(url);
    }
    public BeanArchive(File file) {
        initArchive(file);
    }
    public BeanArchive(File file, SimpleLogger.LogLevel logLevel) {
        setLogLevel(logLevel);
        initArchive(file);
    }

    public void addClass(String className) {
        try {
            Class targetClass = Class.forName(className);
            // we want instantiable classes, so don't add interfaces or
            // abstract classes
            if (targetClass.isInterface() || Modifier.isAbstract(targetClass.getModifiers())) {
                return;
            }
            classes.add(targetClass);
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            throw new IllegalArgumentException("Not found: " + className);
        }
    }

    public List<Class> getClasses() {
        return this.classes;
    }

    private static final String PROCOTOL_JAR = "jar";
    private static final String CLASS_FILE_EXTENSION = ".class";

    private void initArchive(URL url) {
        initArchive(new File(url.getPath()));
    }

    private void initArchive(File file) {

        if (!file.exists()) {
            throw new IllegalArgumentException(
                    String.format("Doesn't exist: %s", file));
        }

        try {
            logger.debug("Handle path: {0}", file.toPath());

            if (file.isDirectory()) {
                handleDirectory(new DirectoryEntry().setFile(file));
            } else {
                handleFile(file);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not handle path: " + file.toPath(), e);
        }
    }

    private void handleFile(File file) throws IOException {

        logger.debug("Handle archive file: {0}", file);

        try {
            ZipFile zip = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zip.entries();
            ZipFileEntry entry = new ZipFileEntry(PROCOTOL_JAR + ":" + file.toURI().toURL().toExternalForm() + "!/");
            while (entries.hasMoreElements()) {
                addIfClass(entry.setName(entries.nextElement().getName()));
            }
            zip.close();
        } catch (ZipException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Recursive method to handle a directory tree.
     *
     * @param entry an entry in a directory. A file instance that might or might not have a path.
     * @throws IOException
     */
    private void handleDirectory(DirectoryEntry entry) throws IOException {

        String directoryPath = entry.getName();

        logger.debug("Handle file {0} with a path: {1}", entry.getFile(), directoryPath);

        boolean hasPath = directoryPath != null;

        File[] files = entry.getFile().listFiles();

        if (files == null) {
            throw new IllegalArgumentException(
                    String.format("Unable to list directory files: %s", entry.getFile()));
        }

        for (File childFile : files) {

            if (hasPath) {
                entry.setPath(directoryPath + "/" + childFile.getName());
            } else {
                entry.setPath(childFile.getName());
            }
            entry.setFile(childFile);

            if (childFile.isDirectory()) {
                handleDirectory(entry);
            } else {
                addIfClass(entry);
            }
            entry.setPath(directoryPath);
        }
    }

    private void addIfClass(Entry entry) {
        if (isClass(entry.getName())) {
            addClass(filenameToClassname(entry.getName()));
        }
    }

    private boolean isClass(String name) {
        return name.endsWith(CLASS_FILE_EXTENSION);
    }

    private String filenameToClassname(String filename) {
        return filename.substring(0, filename.lastIndexOf(CLASS_FILE_EXTENSION))
                .replace('/', '.').replace('\\', '.');
    }

    private void setLogLevel(SimpleLogger.LogLevel logLevel) {
        this.logger.setLogLevel(logLevel);
    }

}
