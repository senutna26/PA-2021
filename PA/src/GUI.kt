import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.graphics.Point
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*
import java.io.File


interface ShellSetup {
    fun changeTree(gui: GUI)
    fun excludeNodes(string: String, gui: GUI)
}

open class IconPlugin() : ShellSetup {
    override fun changeTree(gui: GUI) {
        iconTree(gui.tree.items.first())
    }

    var isRoot = true
    fun iconTree(treeItem: TreeItem){
        if(isRoot == true){
            treeItem.setImage(Image(Display.getDefault(), "src\\Images\\folderIcon.png"))
            isRoot = false
        }
        treeItem.items.forEach {
            if(it.data !is JValue)
                it.setImage(Image(Display.getDefault(), "src\\Images\\folderIcon.png"))
            else
                it.setImage(Image(Display.getDefault(), "src\\Images\\fileIcon.png"))
            iconTree(it)
        }
    }

    override fun excludeNodes(string: String, gui: GUI) {
        runTree(string,gui.tree.items.first())
    }

    fun runTree(jsonType: String, treeItem: TreeItem){
        if(!treeItem.isDisposed){
            treeItem.items.forEach {
                if (jsonType == "value" && it.data is JValue)
                    it.dispose()
                else if (jsonType == "array" && it.data is JArray)
                    it.dispose()
                runTree(jsonType, it)
            }
        }
    }
}

interface Action {
    val name: String
    fun execute(gui: GUI)
}

class EditObject : Action {
    override val name: String
        get() = "Open editor"

    override fun execute(gui: GUI) {
        val objSelected = gui.tree.selection.first()

        val d = Display.getDefault()
        val shell2 = Shell(d)
        shell2.setSize(200,200)
        shell2.layout = GridLayout(1,false)

        val label = Text(shell2,SWT.BORDER)
        label.text = ""

        var button = Button(shell2, SWT.PUSH)
        button.text = "Change field"

        button.addListener(SWT.Selection) {
            if(objSelected.data is JValue) {
                var t = label.text
                (objSelected.data as JValue).editValue(t)
                objSelected.text = "name: " + "\"" + t + "\""
                gui.tree.update()
            }
        }

        shell2.open()
        while (!shell2.isDisposed) {
            if (!d.readAndDispatch()) d.sleep()
        }

    }
}

class WriteToFile : Action {
    override val name: String
        get() = "Write"

    override fun execute(gui: GUI) {
        val file = File("src\\Files" + "\\" + (gui.tree.selection.first().data as JSONType).toString())
        gui.serializer.serializeToFile(gui.tree.selection.first().data as JSONType, file)
    }
}

class Validate: Action {

    private var onlyJsons: Boolean = true

    override val name: String
        get() = "Validate"

    override fun execute(gui: GUI) {
        validateTree(gui.tree.items.first())
        val d = Display.getDefault()
        val shell2 = Shell(d)
        shell2.setSize(200,200)
        shell2.layout = GridLayout(1,false)

        val label = Label(shell2,SWT.NONE)
        label.alignment = SWT.CENTER
        if(onlyJsons) {
            label.text = "Validação correta! \n Apenas objetos JSON"
            shell2.setBackground((Color(57, 255, 20)))
            label.setBackground((Color(57, 255, 20)))
        }else {
            label.text = "Validação falhada! \n Alguns dos objetos contidos \n não são do tipo JSON"
            label.setBackground((Color(255, 42, 27)))
            shell2.setBackground((Color(255, 42, 27)))
        }
        shell2.open()
        while (!shell2.isDisposed) {
            if (!d.readAndDispatch()) d.sleep()
        }

    }

    fun validateTree(treeItem: TreeItem){
        treeItem.items.forEach {
            if(it.data !is JSONType)
                onlyJsons = false
            validateTree(it)
        }

    }

}

class GUI() {
    val shell: Shell

    var tree: Tree

    val label: Label
    val texto: Text

    @Inject
    private lateinit var setup: ShellSetup

    @InjectAdd
    var actions = mutableListOf<Action>()

    val serializer = Serializer()



    init {

        shell = Shell(Display.getDefault())
        shell.setSize(450, 300)
        shell.text = "JSON"
        shell.layout = GridLayout(2,true)
        shell.setBackground(Color(224, 224, 248))

        tree = Tree(shell, SWT.SINGLE or SWT.BORDER)
        tree.layoutData = GridData(130,170)

        label = Label(shell, SWT.NONE)
        label.text = "jj"

        tree.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                serializer.clearSerializedText()
                serializer.serializeFile(tree.selection.first().data as JSONType)
                label.text = serializer.getFullTextSerialized()
                label.parent.layout()
            }
        })

        texto = Text(shell, SWT.BORDER)
        texto.layoutData = GridData(139,15)
        texto.message = "Search here"
        texto.setBackground((Color(255, 206, 148)))

        texto.addListener(SWT.Modify) {

            var textWritted = texto.text

            searchTree(tree.items.first(),textWritted)

            if (tree.items.first().text.contains(textWritted) && textWritted != "") {
                tree.items.first().setBackground(Color(255, 206, 148))
            } else {
                tree.items.first().setBackground(Color(255, 255, 255))
            }


        }



    }

    fun searchTree(treeItem: TreeItem, texto: String){
            treeItem.items.forEach {
                if (it.text.contains(texto) && texto != "") {
                    it.setBackground(Color(255, 206, 148))
                } else {
                    it.setBackground(Color(255, 255, 255))
                }
                searchTree(it, texto)
            }
    }

    fun open(root: JObject) {
        val visualizer = Visualizer(tree)
        root.accept(visualizer)
        tree.expandAll()
        shell.open()
        val display = Display.getDefault()
        actions.forEach { action ->
            val button = Button(shell,SWT.PUSH)
            button.text = action.name
            button.layoutData = GridData(100,25)
            button.addListener(SWT.Selection) { action.execute(this) }
        }
        if(this::setup.isInitialized) {
            setup.changeTree(this)
            setup.excludeNodes("",this)
        }


        while (!shell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }
        display.dispose()
    }
}


// auxiliares para varrer a árvore

fun Tree.expandAll() = traverse { it.expanded = true }

fun Tree.traverse(visitor: (TreeItem) -> Unit) {
    fun TreeItem.traverse() {
        visitor(this)
        items.forEach {
            it.traverse()
        }
    }
    items.forEach { it.traverse() }
}

fun main() {
    val rootMap = HashMap<String, JSONType>()
    val mapa = HashMap<String, JSONType>()

    //Objetorooot
    val comms = Comms(rootMap)
    val root = "1"
    val objeto = JObject()
    val objeto2 = JObject()

    //Array
    val a = JArray()
    val primeiro = JValue("a")
    val segundo = JValue("b")
    a.addJSON(primeiro)
    a.addJSON(segundo)


    objeto.addJSON("objetao", JObject())
    objeto.addJSON("arrayzao", a)
    objeto.addJSON("valuezao", JValue(1))
    a.addJSON(objeto2)
    comms.addJSON(root,objeto)

    GUI().open(objeto)
}