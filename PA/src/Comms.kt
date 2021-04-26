import java.io.File
import java.nio.file.Path

class Comms(private val rootMap: MutableMap<String, JSONType>){

    fun serializeFile(path: Path, root: String){
        val file = File(path.toString() + "\\" + root)
        //file.writeText("wakeup")
        val serializer = Serializer(file)
        rootMap[root]?.accept(serializer)
    }

    fun search(root: String){
        val searcher = Searcher()
        rootMap[root]?.accept(searcher)
    }

    fun addJSON(root: String,json: JSONType){
        rootMap[root] = json
    }

    fun removeJSON(root: String){
        rootMap.remove(root)
    }

}