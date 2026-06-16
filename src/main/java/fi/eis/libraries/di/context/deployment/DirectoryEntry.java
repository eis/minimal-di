package fi.eis.libraries.di.context.deployment;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

class DirectoryEntry implements Entry {

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
