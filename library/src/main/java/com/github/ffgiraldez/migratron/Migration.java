package com.github.ffgiraldez.migratron;

import android.content.Context;

/**
 * Defines the behavior of a migration.
 * @author Fernando Franco Giráldez
 */
public interface Migration {
    /**
     * Run the migration progress.
     * @param context Interface to application's global information.
     */
    void migrate(Context context);
}
