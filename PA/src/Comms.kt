import java.io.File
import java.nio.file.Path
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.collections.Collection as Collection1

class Comms(private val rootMap: MutableMap<String, JSONType>){

    fun serializeFile(path: Path, root: String){
        val file = File(path.toString() + "\\" + root)
        //file.writeText("wakeup")
        val serializer = Serializer(file)
        rootMap[root]?.accept(serializer)
    }

    fun serializeFile2(path: Path, root: JSONType){
        val file = File(path.toString() + "\\" + "yau")
        //file.writeText("wakeup")
        val serializer = Serializer(file)
        root.accept(serializer)
    }



    fun search(root: JSONType){
        val searchAnything: (JSONType) -> Boolean = { value -> value is JValue && value.getValue() is String }
        val searcher = Searcher(searchAnything)
        //rootMap[root]?.accept(searcher)
        root.accept(searcher)
        searcher.searchFun()

    }

    fun addJSON(root: String,json: JSONType){
        rootMap[root] = json
    }

    fun removeJSON(root: String){
        rootMap.remove(root)
    }

    /*Conversão para JSON*/

    fun convertToJson(any: Any): JSONType{

        var anyObj: JSONType = JValue(Any())

        when(any){

            is String -> anyObj = JValue(any)
            is Int -> anyObj = JValue(any)
            is Float -> anyObj = JValue(any)
            is Double -> anyObj = JValue(any)
            is Long -> anyObj = JValue(any)
            is Boolean -> anyObj = JValue(any)
            is Char -> anyObj = JValue(any)
            is Short -> anyObj = JValue(any)
            null -> anyObj = JValue(any)

            is Collection<*> -> {

                val temp = JArray()

                for(i in any){
                    val objs = convertToJson(i)
                    temp.addJSON(objs)
                }

                anyObj = temp

            }

            is Map<*,*> -> {

                val temp = JObject()

                for((k,v) in any){
                    val objs = convertToJson(v!!)
                    temp.addJSON(k as String,objs)
                }

                anyObj = temp

            }

        }

        print("" + anyObj)
        return anyObj


    }

    fun mapObjects(dataClass: Any): JObject {

        var anyObj = JObject()

        val kclazz: KClass<Any> = dataClass::class as KClass<Any>

        val objProp = kclazz.declaredMemberProperties

        for(i in objProp){

            if(!(i.hasAnnotation<IgnoreProperty>())){
                if(i.hasAnnotation<RenameProperty>()){
                    i.getter.call(dataClass)?.let { convertToJson(it) }?.let {
                        i.findAnnotation<RenameProperty>()?.let { it1 -> anyObj.addJSON(it1.newName, it) }
                    }
                }else{
                    i.getter.call(dataClass)?.let { convertToJson(it) }?.let { anyObj.addJSON(i.name, it) }
                }
            }

        }


        return anyObj

    }

}