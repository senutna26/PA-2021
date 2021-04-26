class Searcher: Visitor {

    val searchResult = arrayListOf<Any>()

    fun search(): ArrayList<Any>{
        return searchResult
    }

    override fun visit(jObject: JObject) {
        val map = jObject.getMap()
        for ((k,v) in map){
            v.accept(this)
        }
    }

    override fun visit(jArray: JArray) {
        val array = jArray.getArray()
        for(i in array){
            i.accept(this)
        }
    }

    override fun visit(jValue: JValue) {
        if(jValue.getValue() is String){
            searchResult.add(jValue)
        }
    }
}