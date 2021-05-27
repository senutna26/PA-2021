import java.nio.file.Paths

fun main(){
    val rootMap = HashMap<String, JSONType>()
    val mapa = HashMap<String, JSONType>()

    val serializer = Serializer()

    //Objetorooot
    val comms = Comms(rootMap)
    val root = "1"
    val objeto = JObject()


    //Array
    val a = JArray()
    val primeiro = JValue("a")
    val segundo = JValue("b")
    a.addJSON(primeiro)
    a.addJSON(segundo)


    objeto.addJSON("objetao", JObject())
    objeto.addJSON("arrayzao", a)
    objeto.addJSON("valuezao", JValue(1))
    comms.addJSON(root,objeto)

    //serializar root
    //comms.serializeFile(Paths.get("C:\\Users\\antun\\Desktop\\Testes_PA"),  root  )
    serializer.serializeFile(objeto)

    //Pesquisar na root
    //comms.search(objeto)

    //3ª Parte - Reflexão
    val set1 = setOf<String>("1","2","Fecha aqui")
    comms.addJSON("converted1",comms.convertToJson(set1))
    //comms.serializeFile(Paths.get("C:\\Users\\antun\\Desktop\\Testes_PA"),  "converted1")

    /*val array1: ArrayList<Any> = arrayListOf()
    array1.add("1")
    array1.add(11)
    array1.add(true)
    val dt = DataClass("Primeira_String",array1)
    comms.serializeFile2(Paths.get("C:\\Users\\antun\\Desktop\\Testes_PA"),  comms.mapObjects(dt) )*/

}