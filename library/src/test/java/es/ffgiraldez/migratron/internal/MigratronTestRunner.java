package es.ffgiraldez.migratron.internal;

import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.RobolectricTestRunner;
import java.io.File;
import org.robolectric.annotation.Config;
import org.robolectric.res.Fs;
import org.robolectric.res.FsFile;

/**
 * @author Fernando Franco Giráldez
 */
public class MigratronTestRunner extends RobolectricTestRunner {

    // ----------------------------------
    // CONSTANTS
    // ----------------------------------

    // ----------------------------------
    // ATTRIBUTES
    // ----------------------------------

    // ----------------------------------
    // CONSTRUCTORS
    // ----------------------------------
    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your AndroidManifest.xml file
     * and res directory by default. Use the {@link org.robolectric.annotation.Config} annotation to configure.
     *
     * @param testClass the test class to be run
     * @throws org.junit.runners.model.InitializationError if junit says so
     */
    public MigratronTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }
    // ----------------------------------
    // LIFE CYCLE
    // ----------------------------------

    // ----------------------------------
    // OVERRIDES
    // ----------------------------------

    @Override protected AndroidManifest getAppManifest(Config config) {
        FsFile baseDir = Fs.newFile(new File(getClass().getResource("/").getPath()));
        return new AndroidManifest(baseDir.join("AndroidManifest.xml"), baseDir.join("/res"),baseDir.join("asserts"));
    }

    // ----------------------------------
    // INTERFACES IMPL
    // ----------------------------------

    // ----------------------------------
    // PUBLIC API
    // ----------------------------------

    // ----------------------------------
    // PRIVATE API
    // ----------------------------------

    // ----------------------------------
    // NESTED CLASSES AND INTERFACES
    // ----------------------------------
}
