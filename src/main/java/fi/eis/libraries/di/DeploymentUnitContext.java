package fi.eis.libraries.di;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Creation Date: 1.12.2014
 * Creation Time: 21:55
 * <p/>
 * This class borrows heavily from JBoss Weld class FileSystemBeanArchiveHandler, which authors are:
 * Pete Muir, Marko Luksa, Martin Kouba
 * <p/>
 * FileSystemBeanArchiveHandler has been licensed under Apache License,
 * Version 2.0. It permits releasing under different license.
 * <p/>
 * (http://en.wikipedia.org/wiki/Comparison_of_free_and_open-source_software_licenses)
 *
 * @author eis
 */
public class DeploymentUnitContext extends Context {
    private static final String PROCOTOL_JAR = "jar";
    private static final String CLASS_FILE_EXTENSION = ".class";
    

    public DeploymentUnitContext(Class sourceClass) {
        super();
        // a jar or a directory path of whoever initiated us
        initFrom(handle(sourceClass.getProtectionDomain().getCodeSource().getLocation()));
    }

    public DeploymentUnitContext(File sourceJar) {
        super();
        initFrom(handle(sourceJar));
    }

    public <T> T get(Class<T> type) {
        return super.get(type);
    }

    private void initFrom(BeanArchiveBuilder builder) {
        List<Class> classes = builder.getClasses();
        logger.debugPrint("got classes " + classes);
        super.modules.add(DependencyInjection.classes(classes));
    }

    public BeanArchiveBuilder handle(URL url) {
        return handle(new File(url.getPath()));
    }

    public BeanArchiveBuilder handle(File file) {

        if (!file.exists()) {
            throw new IllegalArgumentException(
                    String.format("Doesn't exist: %s", file));
        }

        BeanArchiveBuilder builder = new BeanArchiveBuilder();

        try {
            logger.debugPrint("Handle path: {0}", file.toPath());

            if (file.isDirectory()) {
                handleDirectory(new DirectoryEntry().setFile(file), builder);
            } else {
                handleFile(file, builder);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not handle path: " + file.toPath(), e);
        }
        return builder;
    }

    protected void handleFile(File file, BeanArchiveBuilder builder) throws IOException {

        logger.debugPrint("Handle archive file: {0}", file);

        try {
            ZipFile zip = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zip.entries();
            ZipFileEntry entry = new ZipFileEntry(PROCOTOL_JAR + ":" + file.toURI().toURL().toExternalForm() + "!/");
            while (entries.hasMoreElements()) {
                add(entry.setName(entries.nextElement().getName()), builder);
            }
            zip.close();
        } catch (ZipException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected void handleDirectory(DirectoryEntry entry, BeanArchiveBuilder builder) throws IOException {

        logger.debugPrint("Handle directory: %s", entry.getFile());

        File[] files = entry.getFile().listFiles();

        if (files == null) {
            throw new IllegalArgumentException(
                    String.format("Unable to list directory files: %s", entry.getFile()));
        }
        String parentPath = entry.getName();

        for (File child : files) {

            if (entry.getName() != null) {
                entry.setPath(entry.getName() + "/" + child.getName());
            } else {
                entry.setPath(child.getName());
            }
            entry.setFile(child);

            if (child.isDirectory()) {
                handleDirectory(entry, builder);
            } else {
                add(entry, builder);
            }
            entry.setPath(parentPath);
        }
    }

    protected void add(Entry entry, BeanArchiveBuilder builder) throws MalformedURLException {
        if (isClass(entry.getName())) {
            builder.addClass(filenameToClassname(entry.getName()));
        }
    }

    protected boolean isClass(String name) {
        return name.endsWith(CLASS_FILE_EXTENSION);
    }

    private String filenameToClassname(String filename) {
        return filename.substring(0, filename.lastIndexOf(CLASS_FILE_EXTENSION))
                .replace('/', '.').replace('\\', '.');
    }

    /**
     * An abstraction of a bean archive entry.
     */
    protected interface Entry {

        String getName();

        /**
         * @return the URL, most probably lazily created
         * @throws MalformedURLException
         */
        URL getUrl() throws MalformedURLException;

    }

    static class BeanArchiveBuilder {
        private final List<Class> classes = new ArrayList<>();

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
                System.out.println("Not found: " + className);
            }
        }

        public List<Class> getClasses() {
            return this.classes;
        }
    }

    private static class ZipFileEntry implements Entry {

        private String name;
        private String archiveUrl;

        ZipFileEntry(String archiveUrl) {
            this.archiveUrl = archiveUrl;
        }

        @Override
        public String getName() {
            return name;
        }

        ZipFileEntry setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public URL getUrl() throws MalformedURLException {
            return new URL(archiveUrl + name);
        }

    }

    private static class DirectoryEntry implements Entry {

        private String path;
        private File file;

        @Override
        public String getName() {
            return path;
        }

        @Override
        public URL getUrl() throws MalformedURLException {
            return file.toURI().toURL();
        }

        public DirectoryEntry setPath(String path) {
            this.path = path;
            return this;
        }

        public File getFile() {
            return file;
        }

        public DirectoryEntry setFile(File dir) {
            this.file = dir;
            return this;
        }

    }

}
