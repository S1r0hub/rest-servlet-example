# REST-API Servlet Example

A basic example of a REST-API using HTTP-Servlets.

Created with [Eclipse IDE for Java EE Developers](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/oxygen3a).


<br/>

## Usage

If you have a running Webserver like `Tomcat`,  
just put the `web application archive (war)` in the according directory.  
E.g. for Tomcat: `tomcat-root/webapps/`.  
Tomcat will automatically extract the archive and the servlet will be available at:
`http://localhost:<port>/REST/`  
(Default port for Tomcat is `8080`)

The servlet is called `people` so that the address to the collection is:  
`http://localhost:<port>/REST/people`

Everything will be stored in the `personen.xml` file located in:
`REST/WEB-INF/classes/helpers/`  
You will also find the according XML-Schema file there.


<br/>

## Eclipse IDE: Setup with Tomcat

#### Setup

1. Open Eclipse JEE
2. Import -> Existing Projects into Workspace
3. Select the folder of this repository
4. Finish
5. Ensure you have [Apache Tomcat](http://tomcat.apache.org/) installed

#### Start with a Tomcat Server

1. Ensure you don't already have a Tomcat instance running on your machine.
2. Download the [Archive](https://tomcat.apache.org/download-90.cgi) and extract it somewhere.
3. In Eclipse - in the `Server` tab (rightclick) -> select New -> Server
4. Select your version and if not already done, configure your runtime environment to use the extracted archive
5. Add the `REST-Example` Project to the Server
6. Select the server (rightclick) -> Publish
7. Start the server
