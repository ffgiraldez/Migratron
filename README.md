Migratron
=========
[![Build Status](https://travis-ci.org/ffgiraldez/Migratron.png?branch=master)](https://travis-ci.org/ffgiraldez/Migratron)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ffgiraldez.migratron/migratron/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ffgiraldez.migratron/migratron/badge.svg) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Migratron-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/2840)

Android port of [MTMigration](https://github.com/mysterioustrousers/MTMigration) for Android to execute pieces of code that only need to run once on version updates. This could be anything from data
normalization routines, "What's New In This Version" screens, or bug fixes.

## Add it to yout project

Include the library in your ``build.gradle``

```groovy
dependencies{
    compile 'com.github.ffgiraldez.migratron:migratron:1.0'
}
```

or to your ``pom.xml`` if you are using Maven

```xml
<dependency>
    <groupId>com.github.ffgiraldez.migratron</groupId>
    <artifactId>migratron</artifactId>
    <version>1.0</version>
</dependency>
```

## Usage

First of all you need create the `Migratron` object, with a `Context` recomended the application context and a `SharedPreference` to store version information needed to handle correctly the diferents `Migration`

```java
SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Mode.Private);
Migratron migratron = new Migration(context.getApplicationContext(), sharedPrefenences)
```

Migratron provides two kind of migrations, one to use when you need some code that run every time you update your application.
Use `updateMigration`method

When you need a piece of code that runs every time you update your application.
Use the `applicationUpdateBlock:` method.

```java
migratron.updateMigration(new Migration() {
    @Override
    public void migrate(Context context) {
        rater.reset();
    }
})
```
the other migratrion is specific to a version, use `addMigration(int versionCode,Migration migration)` and Migratron will ensure that the `Migration` passed it's only ever run once for that version

```java
migratron.addMigration(8, new Migration() {
    @Override
    public void migrate(Context context) {
        showVersionNotesController.enable();
    }
})
```
Once all your migrations are configured in a `Migratron` object execute it calling

```java
migratron.migrate();
```

This allow programers configure the `Migratron` and execute the migrations in a lazy way only when needed, usualy in the onCreate method of the Application.

Migratron inspect your AndroidManifest.xml for your actual versionCode and keeps track of last migration.

It will migrate all un-migrated Migration inbetween. For example, let's say you had the following migrations:

```java
migratron.addMigration(9, new Migration() {
    @Override
    public void migrate(Context context) {
        // Some 9 stuff
    }
})
migratron.addMigration(10, new Migration() {
    @Override
    public void migrate(Context context) {
        // Some 10 stuff
    }
})
```

If a user was at version `8`, skipped `9`, and upgraded to `10`, then both the `9` *and* `10` migrations would run.
## Notes

Migratron assumes version numbers are incremented in a logical way, i.e. `101` -> `102`, `110` -> `120`, etc.

Version numbers that are past the version specified in your app will not be run. For example, if your AndroidManifest.xml file
specifies `120` as versionCode, and you attempt to migrate to `130`, the migration will not run.

Migrations are executed on the thread the migration is run on. Background/foreground situations should be considered accordingly.

## Contributing

This library does not handle some more intricate migration situations, if you come across intricate use cases from your own
app, please add it and submit a pull request. Be sure to add test cases.

## Libraries used in this project

- [Lombok](https://projectlombok.org/) 
- [Robolectric](https://github.com/robolectric/robolectric)
- [Mockito](http://mockito.org/)
- [JUnit](http://junit.org/)
- [AssertJ-Android](https://github.com/square/assertj-android)