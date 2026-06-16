package fi.eis.libraries.di.context.deployment;

import java.net.MalformedURLException;
import java.net.URL;

class ZipFileEntry implements Entry {

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
