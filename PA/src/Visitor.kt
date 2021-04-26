interface Visitor {

    fun visit(jArray: JArray)
    fun visit(jObject: JObject)
    fun visit(jValue: JValue)


}