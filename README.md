# Message Router

Submitted by: Aman Palariya
Entry number: 2019csb1068

## Overview
The entry point of the program is `App.java`.

`logger` package contains a logger interface (also acts as a facade) and an implementation using the JUL library.

`dbms` package contains a very simple class for executing queries on a database at a given URL.

`config` package is used to read the configuration and make it available to all components of the application.

`core` package contains the main classes e.g. Router, Listener, Package, Acknowledgement, etc. However, the classes in `core` are agnostic to the datasources.

`custom` package contains realizations of classes in `core` package.

## Working

A `Listener` listens for incoming messages. Any number of `Subscriber`s can be added to a `Listener`. When a messages arrives, the `Listener` notifies all its `Subscriber`s. The subscribers can optionally return an `Acknowledgement`. `Subscriber` can do anything with the messages they receive.

The `HttpListener` is a special listener that listens on an HTTP socket and waits for a POST request. It accepts the messages in XML format.
After converting the XML to a `Message`, it notifies its `Subscriber`s.

In our case, we add one `Subscriber` to our `HttpListener`. This `Subscriber` calls the `HttpRouter` for routing the message.

A `Router` when given a message routes it to the appropriate client. It depends on `RouteFinder` (for find the routing entry from routing table, not necessarily an SQL table, it can fetch from any datasource) and `RouteLogger` (for logging RECEIVED and SENT events, to any datasource).

In our case, `HttpRouter` uses `SqlRouteFinder` and `SqlRouteLogger` which, as their names suggest depend on specific datasource.

## Use of SOLID principles and design patterns

Single responsibility principle is realized often in code because all classes are design such that they do only one job.
For example, `Router` only send the message to its appropriate destination but `Router` does not find the destination, this task is delegated to `RouteFinder`.
Similarly, `HttpListener` does not serialize or deserialize the XML messages, it delegates these tasks to `XmlMessageParser`.
There are many such examples.

Liscov substitution principle is also seen in `Logger`, `Router`, `Listener`, etc. because they can be assigned `JULogger`, `HttpRouter`, `HttpListener` respectively and these can be replaced without affecting other code.

Dependency inversion is also realized because high level class (e.g. `SqlRouteFinder`) does not directly connect to database, instead it uses `SqlDatabaseReader` which handles the lower level API. This is also an example of Facade design pattern.

Some design pattern in use

- Bridge pattern can be seen in (`Router`, `RouterFinder`) pair, and (`Router`, `RouterLogger`)
- Observer pattern is seen in `Listener`-`Subscriber`
- For providing global access to logger, `Singleton` pattern is used

## Compilation and testing

The project is made with Gradle.
To run the project, configure the `config.json` file in `main/resources`
Initialize the tables of the with appropriate schema.
Finally, execute `./gradlew run` to start the message routing service.

For testing the project, run `./gradlew test`.

Make sure that ports 8000, and 8001 are free before testing (or change the ports in tests to some free ports).