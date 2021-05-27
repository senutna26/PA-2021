import java.io.File
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

@Target(AnnotationTarget.PROPERTY)
annotation class Inject

@Target(AnnotationTarget.PROPERTY)
annotation class InjectAdd

class Injector {
    companion object {
        val map: MutableMap<String, KClass<*>> = mutableMapOf()
        var list = mutableListOf<KClass<*>>()

        init {
            val scanner = Scanner(File("C:\\Users\\antun\\Desktop\\PA\\out\\production\\PA\\JUnit\\di.properties"))
            while(scanner.hasNextLine()){
                val line = scanner.nextLine()
                val parts = line.split("=")
                if(!parts[1].contains(",")) {
                    map[parts[0]] = Class.forName(parts[1]).kotlin
                }else{
                    parts[1].split(',').forEach {
                        list.add(Class.forName(it).kotlin)
                    }
                }
            }
            scanner.close()
        }

        fun <T:Any> create(type: KClass<T>) : T {
            val o: T = type.createInstance()
            type.declaredMemberProperties.forEach {
                if(it.hasAnnotation<Inject>()) {
                    it.isAccessible = true
                    val key = type.simpleName + "." + it.name
                    if(!map.isEmpty()) {
                        val obj = map[key]!!.createInstance()
                        (it as KMutableProperty<*>).setter.call(o, obj)
                    }
                } else {
                    if (it.hasAnnotation<InjectAdd>()) {
                        it.isAccessible = true
                        list.forEach { act ->
                            var obj = act!!.createInstance()
                            val list2 = it.getter.call(o) as MutableList<Any>
                            list2.add(obj)
                        }
                    }
                }
            }
            return o
        }
    }
}

fun main () {

    val w = Injector.create(GUI::class) // substituir por criacao com injecao

    val rootMap = HashMap<String, JSONType>()
    val mapa = HashMap<String, JSONType>()

    //Objetorooot
    val comms = Comms(rootMap)
    val root = "1"
    val objeto = JObject()
    val objeto2 = JObject()

    //Array
    val a = JArray()
    val primeiro = JValue("a")
    val segundo = JValue("b")
    a.addJSON(primeiro)
    a.addJSON(segundo)


    objeto.addJSON("objetao", JObject())
    objeto.addJSON("arrayzao", a)
    objeto.addJSON("valuezao", JValue(1))
    a.addJSON(objeto2)
    comms.addJSON(root,objeto)

    w.open(objeto)

}