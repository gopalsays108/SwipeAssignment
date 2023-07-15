## Project Overview
The project is a multi-module Android application that follows modern architecture and best practices. It consists of two main modules: a core module and a service layer module.

The core module is responsible for handling the presentation layer using the MVVM (Model-View-ViewModel) architectural pattern.

The service layer module focuses on network calls and database operations. It leverages Koin, a lightweight dependency injection framework, to provide dependency management and improve code modularity. The module integrates Kotlin Coroutines for asynchronous and structured concurrency, enabling efficient and responsive app behavior.

In this project we have used Koin DI to inject our dependencies seamlessly. To ensure robust and efficient data management, the project utilizes Retrofit for network requests.
It employs sealed and generic classes to handle and process responses from the server, promoting code reusability and reducing boilerplate code. We have also utilized kotlin flows to read response.
For image loading, the project utilizes Glide, a popular image loading library, to efficiently load and display images from various sources.

To propagate data changes and update the UI, LiveData and Kotlin Flows are utilized. LiveData allows for observable data changes, while Kotlin Flows provide support for reactive streams and asynchronous data processing.

To optimize RecyclerView performance, the project utilizes DiffUtil, which calculates the difference between two lists and efficiently updates the UI, resulting in smooth and efficient list updates.
We have also used Navigation component in this.
We have also used many UI compopnent like recyclerview, guidlines, made custom chip layout.

Overall, the project demonstrates the use of modern technologies and best practices to create a well-structured, efficient, and visually appealing Android application.
##Tech Used
1. Multi module
2. Koin for Dependency Injection
3. MVVM
4. Coroutines
5. Sealed and generic class for handling response
6. Retrofit
7. Navigation component
8. Kotlin FLows
9. Glide
10. BuildConfig to store Base URL and API keys
11. ViewBinding
12. Data classes
13. LiveDatas
14. Diff Utils

## For UI
1. Recyclerview
2. Guidlines
3. Constrainview
4. NestedScrollView
5. HorizontalScrollView
6. CardView
7. Custom chip using HorizontalScrollView
8. Drawables files

##Other tools
1. Android studio
2. Github
