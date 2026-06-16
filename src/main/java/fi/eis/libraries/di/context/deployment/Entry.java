package fi.eis.libraries.di.context.deployment;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * An abstraction of a bean archive entry.
 */
interface Entry {

    String getName();

    /**
     * @return the URL, most probably lazily created
     * @throws MalformedURLException missing protocol or URL parsing error
     */
    URL getUrl() throws MalformedURLException;

}
