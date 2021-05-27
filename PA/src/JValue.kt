class JValue(private var any: Any) : JSONType {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    fun getValue(): Any {
        return any
    }

    override fun toString(): String {
        return any.toString()
    }

    fun editValue(change: Any){
        any = change
    }

}