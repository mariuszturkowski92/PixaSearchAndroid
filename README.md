# PixaSearchAndroid

PixaSearch

Sample app that fetching images from pixabay api and display them in a grid view.

### Configuration

to get access to pixabay api you need to define your own api key in the file `local.properties` in the root of
the project. with property key `pixabay_api_key` and value `your_api_key`

You will need an API key for the Pixabay public web services. It can be retrieved from this page (you must be
logged in to see it):
https://pixabay.com/api/docs/#api_search_images

### Tech Stack

- Kotlin
- MVVM
- Coroutines Flow
- Hilt
- ktor
- Coil
- Jatpack Compose