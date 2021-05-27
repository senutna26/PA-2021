import java.io.File
import java.nio.file.Path

class Serializer() : Visitor {

    var nTabs = 0
    private var fullTextSerialized: String = ""


    fun serializeFile(objToSerialize: JSONType){
        objToSerialize.accept(this)

    }

    fun serializeToFile(objToSerialize: JSONType, file: File){
        file.delete()
        clearSerializedText()
        objToSerialize.accept(this)
        file.appendText(fullTextSerialized)
        clearSerializedText()
    }


    fun getFullTextSerialized(): String{
        return fullTextSerialized
    }

    fun clearSerializedText(){
        fullTextSerialized = ""
    }

    fun writeToFile(path: Path){
        val file = File(path.toString() + "\\" + "root")
        file.appendText(fullTextSerialized)
    }

    fun write(any: String){
        fullTextSerialized += any
    }

    override fun visit(jObject: JObject) {
        val map = jObject.getMap()
        write("{ \n")
        increaseTabs()
        var x = 0
        for ((k,v) in map){
            loopTabs()
            write("\"" + k + "\": ")
            v.accept(this)
            if(x < map.size -1 ){
                write(",")
            }
            write("\n")
            x++
        }
        decreaseTabs()
        loopTabs()
        write("}")


    }

    override fun visit(jArray: JArray) {
        val array = jArray.getArray()
        write("[ \n")
        increaseTabs()
        var y = 0
        for (i in array){
            loopTabs()
            i.accept(this)
            if(y < array.size -1 ){
                write(",")
            }
            write("\n")
            y++
        }
        decreaseTabs()
        loopTabs()
        write("]")
    }

    override fun visit(jValue: JValue) {
        write("\"" + jValue.getValue().toString() + "\"")
    }

    fun loopTabs(){
        var i = 0
        while(i < nTabs){
            write("\t")
            i += 1
        }
    }

    fun increaseTabs(){
        nTabs += 1
    }

    fun decreaseTabs(){
        nTabs -= 1
    }



}