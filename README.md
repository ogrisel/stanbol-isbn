# stanbol-isbn

Hands on tutorial material for learning to customize a Stanbol server.


## Exercise 1: Indexing an RDF database of ISBN codes with the Entity Hub

Build and package a bundle to ship the content of the `data` folder
(a small RDF databse of ISBN for some famous books) as a "Referenced
Site" for the Stanbol "Entity Hub" component.

This can be done by following the first section of the instructions
online documentation on [Working with Custom Vocabularies](
http://stanbol.apache.org/docs/trunk/customvocabulary.html).

Examples of working configuration files can be found in the
`indexing-config` subfolder.

Deploy the content of the resulting `indexing/dist` folder on your
own Stanbol server as explained in the instructions.

Check that you can use the web interface and the REST API of the
referenced site to query the database to find some of the books by
name or by ISBN.


## Exercise 2: Linking known ISBN with the Keyword Linking engine

Configure a new Keyword Linking enhancement engine on Stanbol and
make it use the `dbp-ont:isbn` property and the newly registered
referenced site of "Exercise 1".

This can be done by following the intructions of the second part
of the online documentation on [Working with Custom Vocabularies](
http://stanbol.apache.org/docs/trunk/customvocabulary.html).

Test it by configuring a new `List Chain` component configuration
in the system console of your Stanbol server.


## Exercise 3: Unknow ISBN detection with a custom Enhancement Engine

Install maven and then build the current project with:

    mvn install

In the folder `stanbol-isbn-engine-skeleton` type:

    mvn eclipse:eclipse

This will generate the project file to make it possible to develop
with the Eclipse IDE.

At any moment you can deploy this project to a running Stanbol
server using:

    mvn install -DskipTests -PinstallBundle \
        -Dsling.url=http://localhost:8080/system/console

You can check the deployment in the logs in `STANBOL_HOME/logs/error.log`
and checking that a new engine named "Stanbol ISBN Enhancement
Engine" is available in the felix console.

You can add this engine to the previous chain or configure a new
one to test it alone, either using the Stanbol web interface or
using the REST API.

The solution for this exercise is available in the `stanbol-isbn-engine`
folder.
