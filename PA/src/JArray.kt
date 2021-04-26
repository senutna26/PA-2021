import java.io.File

class JArray() : JSONType {

    private val array: ArrayList<JSONType> = arrayListOf()

    fun getArray(): ArrayList<JSONType> {
        return array
    }

    fun addJSON(json: JSONType){
        array.add(json)
    }

    fun removeJSON(json: JSONType){
        array.remove(json)
    }

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

}