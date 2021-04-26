import java.nio.file.Paths

fun main(){
    val rootMap = HashMap<String, JSONType>()
    val mapa = HashMap<String, JSONType>()
    val comms = Comms(rootMap)
    val cadeira = "j"
    val objeto = JObject(mapa)

    //Array
    val a = JArray()
    val primeiro = JValue("a")
    val segundo = JValue("b")
    a.addJSON(primeiro)
    a.addJSON(segundo)

    //
    objeto.addJSON("objetao", JObject(HashMap<String, JSONType>()))
    objeto.addJSON("arrayzao", a)
    objeto.addJSON("valuezao", JValue("c"))
    comms.addJSON(cadeira,objeto)
    comms.serializeFile(Paths.get("C:\\Users\\antun\\Desktop\\PGMV"),  cadeira  )
}