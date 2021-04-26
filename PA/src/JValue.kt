class JValue(private val any: Any) : JSONType {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    fun getValue(): Any {
        return any
    }




}