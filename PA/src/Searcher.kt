class Searcher(private val condition: (JSONType) -> Boolean) : Visitor {

    val searchResult = arrayListOf<Any>()

    fun searchFun(): ArrayList<Any>{
        return searchResult
    }

    override fun visit(jObject: JObject) {
        val map = jObject.getMap()
        if(condition(jObject)){
            searchResult.add(jObject)
        }
        for ((k,v) in map){
            v.accept(this)
        }
    }

    override fun visit(jArray: JArray) {
        val array = jArray.getArray()
        if(condition(jArray)){
            searchResult.add(jArray)
        }
        for(i in array){
            i.accept(this)
        }
    }

    override fun visit(jValue: JValue) {
        if(condition(jValue)){
            searchResult.add(jValue)
        }
    }
}