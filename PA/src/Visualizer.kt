import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Tree
import org.eclipse.swt.widgets.TreeItem
import org.eclipse.swt.widgets.Widget

class Visualizer(private val tree: Tree): Visitor {

    private var depth = 0
    lateinit var parent: TreeItem


    override fun visit(jArray: JArray) {
        var obj: TreeItem
        if(depth == 0) {
            obj = TreeItem(tree, SWT.NONE)
            obj.text = "children"
        }else{
            obj = TreeItem(parent, SWT.NONE)
            obj.text = "children"
        }
        obj.data = jArray
        parent = obj
        depth++
        for (i in jArray.getArray()){
            i.accept(this)
            parent = obj
        }
    }

    override fun visit(jObject: JObject) {
        var obj: TreeItem
        if(depth == 0) {
            obj = TreeItem(tree, SWT.NONE)
        }else{
            obj = TreeItem(parent, SWT.NONE)
        }
        obj.text = "(Object)"
        obj.data = jObject
        parent = obj
        depth++
        for ((k,v) in jObject.getMap()){
            v.accept(this)
            parent = obj
        }

    }

    override fun visit(jValue: JValue) {
        var obj: TreeItem
        if(depth == 0) {
            obj = TreeItem(tree, SWT.NONE)
        }else{
            obj = TreeItem(parent, SWT.NONE)
        }
        obj.text = "name: " + "\"" + jValue.toString() + "\""
        obj.data = jValue
        depth++
    }


}