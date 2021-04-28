import java.nio.file.Paths

fun main(){
    val rootMap = HashMap<String, JSONType>()
    val mapa = HashMap<String, JSONType>()
    val comms = Comms(rootMap)
    val cadeira = "2"
    val objeto = JObject()

    //Array
    val a = JArray()
    val primeiro = JValue("a")
    val segundo = JValue("b")
    a.addJSON(primeiro)
    a.addJSON(segundo)

    //
    objeto.addJSON("objetao", JObject())
    objeto.addJSON("arrayzao", a)
    objeto.addJSON("valuezao", JValue(1))
    comms.addJSON(cadeira,objeto)
    comms.serializeFile(Paths.get("C:\\Users\\antun\\Desktop\\PGMV"),  cadeira  )
    //comms.search(objeto)
    val lista = setOf<String>("1","2")
    //comms.serializeFile2(Paths.get("C:\\Users\\antun\\Desktop\\PGMV"),  comms.convertToJson(lista) )

    val array1: ArrayList<Any> = arrayListOf()
    array1.add("1")
    array1.add(11)
    array1.add(true)
    val dt = DataClass("YAU",array1)
    comms.serializeFile2(Paths.get("C:\\Users\\antun\\Desktop\\PGMV"),  comms.mapObjects(dt) )

}