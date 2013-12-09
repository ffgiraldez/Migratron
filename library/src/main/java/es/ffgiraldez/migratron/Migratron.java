package es.ffgiraldez.migratron;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Manage {@link es.ffgiraldez.migratron.Migration} elements to run one per application update.
 * @author Fernando Franco Giráldez
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
public class Migratron {
    // ----------------------------------
    // CONSTANTS
    // ----------------------------------
    public static final String LAST_MIGRATION_VERSION   = "migratron.last_migration_version";
    public static final String LAST_APP_VERSION   = "migratron.last_app_version";

    // ----------------------------------
    // ATTRIBUTES
    // ----------------------------------
    @NonNull private Context context;
    @NonNull private SharedPreferences preferences;
    private SortedMap<Integer,Migration> migrations = new TreeMap<Integer, Migration>();
    private Migration updateMigration = null;

    // ----------------------------------
    // PUBLIC API
    // ----------------------------------

    /**
     * Add a Migration code to execute in the specified version code.
     * @param versionCode the version of the app to run the {@link es.ffgiraldez.migratron.Migration}
     * @param migration the code to execute.
     */
    public void addMigration(int versionCode,Migration migration) {
        migrations.put(versionCode, migration);
    }

    /**
     * Execute the migrations if necesary.
     */
    public void migrate() {
        int lastMigratedVersion = preferences.getInt(LAST_MIGRATION_VERSION, 0);
        int appVersion = appVersion();
        int lastAppVersion = preferences.getInt(LAST_APP_VERSION, 0);
        for(Integer versionCode : migrations.keySet()) {
            if (versionCode > lastMigratedVersion && versionCode <= appVersion) {
                migrations.get(versionCode).migrate(context);
                preferences.edit().putInt(LAST_MIGRATION_VERSION, versionCode).commit();
            }
        }

        if (updateMigration != null && lastAppVersion != appVersion) {
            updateMigration.migrate(context);
            preferences.edit().putInt(LAST_APP_VERSION, appVersion).commit();
        }
    }

    /**
     * Specify a migration code that need to run on every app change.
     * @param update the code to execute.
     */
    public void updateMigration(Migration update) {
        updateMigration = update;
    }

    // ----------------------------------
    // PRIVATE API
    // ----------------------------------
    @SneakyThrows(NameNotFoundException.class)
    private int appVersion() {
        int appVersion = 0;
        PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        appVersion = pinfo.versionCode;

        return appVersion;
    }
}
