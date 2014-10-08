TroveLiquibaseDemo
==================

A project that when run, will create a web app backed by a Trove database.

__NB:__ At the time of writing Trove is only available to NeCTAR users on
request: and it is only accessible within the confines of a Melbourne 
University network address.

To run the project simply use the heat template [launchLiquibaseDemo.yaml](heat/launchLiquibaseDemo.yaml) 
in the heat subdirectory.

Some points of interest
-----------------------
In the resources directory the do_not_version subdirectory is expected to 
contain a properties file named [connection.properties](src/main/resources/do_not_version/connection.properties).
This file is constructed by the heat template using parameters entered by the
user. If you want to run this application locally simply create this file
and add the appropriate values.

The values in this file are used as Maven properties to set the values in
the [liquibase.properties](src/main/resources/connections/liquibase.properties)
and the [load_data.xml](src/main/resources/liquibase/load_data.xml) files
as they are copied to the target directory when the project is built.