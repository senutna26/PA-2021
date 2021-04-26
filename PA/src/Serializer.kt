import java.io.File

class Serializer(private val file: File) : Visitor {

    var nTabs = 0

    override fun visit(jObject: JObject) {
        val map = jObject.getMap()
        file.appendText("{ \n")
        increaseTabs()
        var x = 0
        for ((k,v) in map){
            loopTabs()
            file.appendText("\"" + k + "\": ")
            v.accept(this)
            if(x < map.size -1 ){
                file.appendText(",")
            }
            file.appendText("\n")
            x++
        }
        decreaseTabs()
        loopTabs()
        file.appendText("}")


    }

    override fun visit(jArray: JArray) {
        val array = jArray.getArray()
        file.appendText("[ \n")
        increaseTabs()
        var y = 0
        for (i in array){
            loopTabs()
            i.accept(this)
            if(y < array.size -1 ){
                file.appendText(",")
            }
            file.appendText("\n")
            y++
        }
        decreaseTabs()
        loopTabs()
        file.appendText("]")
    }

    override fun visit(jValue: JValue) {
        file.appendText("\"" + jValue.getValue().toString() + "\"")
    }

    fun loopTabs(){
        var i = 0
        while(i < nTabs){
            file.appendText("\t")
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