The project structure contains the following items:

* src/main: core classes of the project
* src/test: unit test classes (Junit based)
* src/androidTest: integration classes (Espresso based)

Key Dependencies:

* Volley was used to perform the HTTP requests
* LazyList was used for asynchronous URL Image loading, providing LRU based cache.
* RecyclerView was used instead of classic ListView to increase efficiency
* Android Material based design using the design compact library

Performance:

Memory tests were performed, in any case the total amount of used memory was above 20MB in Samsung
Galaxy SIII, Nexus 4 and Nexus S. No leaks were detected but LeakCanary will be nice to include.

App Architecture:

The App provides support for Smartphone and Tablet using the strategy based on providing different
layouts for each platform.
In the case of Smartphones, a single Activity will contain a Fragment to show the List of Books and
the details of the selected Book.
In the case of Table, a Master Detail approach has been followed, showing in one side the list
of Books and the other side the selected Book.

In both cases, all the logic is encapsulated and reused in the right Fragments.

For simplicity, a standard service-layered separation of concerns was followed
(UI <=> External Services <=> Server). It can be easily escalated to use a MVP approach splitting
the logic from the Activity and allowing a better dependency injection and testability.




