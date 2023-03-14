# Progetto per lo Scorpio

## Cose da cambiare per far funzionare il programma
 
### Step 1:

In elasticsearch.yml che si trova nella cartella config aggiungi queste 2 righe alla fine del documento

```sh
network.host: "192.168.xx.xx"
http.port: 9200
```

##### dove le xx vanno sostituite con il tuo effettivo ip di casa.

### Step 2:

Per la parte Kotlin invece, per prima parte aggiungi queste dipendenze al build.grandle
```sh
    implementation 'com.loopj.android:android-async-http:1.4.9'
    implementation "org.jetbrains.anko:anko-commons:0.10.1"
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    implementation 'com.android.support:multidex:1.0.3'
```

##### servono per le chiamate asincrone, chiamate crud (okhttp:4.9.0), per i file json etc.


### Step 3:

Aggiugi il consenso a chiamare http e non solo https sul manifest in questo modo:
```sh
<?xml version="1.0" encoding="utf-8"?>
<manifest ...>
    <uses-permission android:name="android.permission.INTERNET" />
    <application
        ...
        android:usesCleartextTraffic="true"
        ...>
        ...
    </application>
</manifest>
```

se non l'hai già fatto aggiungi il permesso per usare internet all'app (da aggiungere all'inzio del manifest):
```sh
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="it.unife.projectscorpio">
    
    <uses-permission android:name="android.permission.INTERNET" />


    <application ...
```

### Step 4:

Crea le tue chiamate crud nelle classi, ecco degli esempi di get e post: 

#GET
```sh
fun get() {
    val client = OkHttpClient()
    val url = URL("https://reqres.in/api/users?page=2")

    val request = Request.Builder()
            .url(url)
            .get()
            .build()

    val response = client.newCall(request).execute()

    val responseBody = response.body!!.string()

    //Response
    println("Response Body: " + responseBody)

    //we could use jackson if we got a JSON
    val mapperAll = ObjectMapper()
    val objData = mapperAll.readTree(responseBody)

    objData.get("data").forEachIndexed { index, jsonNode ->
        println("$index $jsonNode")
    }
}
```

#POST
```sh
fun post() {
    val client = OkHttpClient()
    val url = URL("https://reqres.in/api/users")

    //just a string
    var jsonString = "{\"name\": \"Rolando\", \"job\": \"Fakeador\"}"

    //or using jackson
    val mapperAll = ObjectMapper()
    val jacksonObj = mapperAll.createObjectNode()
    jacksonObj.put("name", "Rolando")
    jacksonObj.put("job", "Fakeador")
    val jacksonString = jacksonObj.toString()

    val mediaType = "application/json; charset=utf-8".toMediaType()
    val body = jacksonString.toRequestBody(mediaType)

    val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

    val response = client.newCall(request).execute()

    val responseBody = response.body!!.string()

    //Response
    println("Response Body: " + responseBody)

    //we could use jackson if we got a JSON
    val objData = mapperAll.readTree(responseBody)

    println("My name is " + objData.get("name").textValue() + ", and I'm a " + objData.get("job").textValue() + ".")
}
```

### [Altri Esempi di chiamate su Kotlin](https://square.github.io/okhttp/recipes/)

### Step 5:

### [Chiamate per interogare e riempire Elasticsearch](https://github.com/LisaHJung/Part-1-Intro-to-Elasticsearch-and-Kibana)
