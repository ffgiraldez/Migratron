package com.github.ffgiraldez.migratron;

import android.content.Context;
import android.content.SharedPreferences;
import com.github.ffgiraldez.migratron.internal.MigratronTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.robolectric.Robolectric;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Fernando Franco Giráldez
 */
@RunWith(MigratronTestRunner.class)
public class MigratronTest {
    // ----------------------------------
    // ATTRIBUTES
    // ----------------------------------
    private Migratron migratron;
    private SharedPreferences prefs;

    // ----------------------------------
    // SETUPS
    // ----------------------------------
    @Before
    public void setup() {
        prefs = Robolectric.application.getSharedPreferences("test", Context.MODE_PRIVATE);
        migratron = new Migratron(Robolectric.application, prefs);
    }

    // ----------------------------------
    // TESTS
    // ----------------------------------
    @Test
    public void testCalledMigration() {
        //given
        Migration migration = mock(Migration.class);

        //when
        migratron.addMigration(8000, migration);
        migratron.migrate();


        //then
        verify(migration).migrate(eq(Robolectric.application));

    }

    @Test
    public void testMigrationOrder() {
        //given
        Migration first = mock(Migration.class);
        Migration second = mock(Migration.class);
        Migration never = mock(Migration.class);

        //when
        migratron.addMigration(7002, never);
        migratron.addMigration(7003, second);
        migratron.addMigration(7002, first);
        migratron.migrate();

        //then
        InOrder order = inOrder(first, second, never);
        order.verify(first).migrate(eq(Robolectric.application));
        order.verify(second).migrate(eq(Robolectric.application));
        order.verifyNoMoreInteractions();

        verify(never, never()).migrate(eq(Robolectric.application));
    }

    @Test
    public void testSkipMigrationVersion() {
        //given
        prefs.edit().putInt(Migratron.LAST_MIGRATION_VERSION,7000).commit();
        Migration first = mock(Migration.class);
        Migration never = mock(Migration.class);

        //when
        migratron.addMigration(6000, never);
        migratron.addMigration(8000, first);
        migratron.migrate();

        //then
        verify(first).migrate(eq(Robolectric.application));
        verify(never, never()).migrate(eq(Robolectric.application));
    }

    @Test
    public void testSkipOldMigrationVersion() {
        //given
        Migration first = mock(Migration.class);
        migratron.addMigration(6000, first);

        //when
        migratron.migrate();
        //just simulate other run
        migratron.migrate();

        //then
        verify(first,times(1)).migrate(eq(Robolectric.application));
    }

    @Test
    public void testUpdateMigration() {
        //given
        Migration update = mock(Migration.class);
        migratron.updateMigration(update);

        //when
        migratron.migrate();

        //then
        verify(update,times(1)).migrate(eq(Robolectric.application));
    }

    @Test
    public void testSkipUpdateMigration() {
        //given
        Migration update = mock(Migration.class);
        migratron.updateMigration(update);

        //when
        migratron.migrate();
        //simulate other run
        migratron.migrate();

        //then
        verify(update,times(1)).migrate(eq(Robolectric.application));
    }

    @Test
    public void testSkipFutureMigration() {
        //given
        prefs.edit().putInt(Migratron.LAST_MIGRATION_VERSION,7000).commit();
        Migration first = mock(Migration.class);
        migratron.addMigration(9000, first);

        //when
        migratron.migrate();

        //then
        verify(first,never()).migrate(eq(Robolectric.application));
    }

    @Test(expected = NullPointerException.class)
    public void testNullContext() {
        migratron = new Migratron(null, prefs);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPreferences() {
        migratron = new Migratron(Robolectric.application,null);
    }
}
