# stanbol-isbn

Hands on tutorial material for learning to customize a Stanbol
server.


## Exercise 1: Loading an database of ISBN codes in the Entity Hub

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


## Exercise 3: Unknown ISBN detection with a custom Enhancement Engine

Install maven and then build the current project with:

    mvn install

Inside the folder `stanbol-isbn-engine-skeleton` type:

    mvn eclipse:eclipse

This will generate the project file to make it possible to develop
with the Eclipse IDE (Import... > General > Existing Projects into
Workspace).

At any moment you can deploy this project to a running Stanbol
server using:

    mvn install -DskipTests -PinstallBundle \
        -Dsling.url=http://localhost:8080/system/console

You can check the deployment in the logs in `STANBOL_HOME/logs/error.log`
and checking that a new engine named "Stanbol ISBN Enhancement
Engine" is available in the felix console.

You can add this engine to the previous chain or configure a new
chain to test this engine indepentently, either using the Stanbol
web interface or using the REST API.


### Exercise sub-parts

- Update the regular expression `isbnPattern` and use it to output
  ISBN matches in the text content of the passed `ContentItem`
  instance  using `System.out.println()` to check that the engine
  is called as expected once redeployed with a valid patten.

  To test, pass a paragraph of text that mentions one or several
  ISBN code found in the `data/isbn.nt` file for instance.

- Replace the STDOUT raw output by a RDF description in to be added
  to the `ContentItem` metadata: make it hold the basic match
  information for each ISBN occurrence: selected text, start and
  end character positions.

- Filter the regex match by computing the checksum documented in
  the Wikipedia article for ISBN-13. Try that the filtering works
  as expected by changing the last digit of a valid ISBN from the
  file `data/isbn.nt`.

- Write a method called `makeSelectionContext` to extract a window
  of text around the location of each match and add it to the RDF
  description so as to provide the service callers with context
  information that is useful even if the text document is a text
  extraction from a formatted source such as an HTML page or a PDF
  file. Using text selection context is also useful to render the
  ISBN detection enhancement more robust against document editing
  events that would corrupt annotations based on character offsets.


### Solution

The solution for this exercise is available in the `stanbol-isbn-engine`
folder.


## Licenses

- Code examples are available for reuse under the
  [Apache Software License](http://www.apache.org/licenses/LICENSE-2.0)

- This short documentation is available under Creative Commons
  Attribution 3.0.

- The DBpedia data extract is available under the original license
  (Creative Commons Attribution-ShareAlike 3.0 License and the GNU Free
  Documentation License).
