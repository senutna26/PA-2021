class JObject() : JSONType{

    private val map: MutableMap<String, JSONType> = mutableMapOf()

    fun getMap(): MutableMap<String, JSONType> {
        return map
    }

    fun addJSON(key: String,json: JSONType){
        map[key] = json
    }

    fun removeJSON(key: String){
        map.remove(key)
    }

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }


}