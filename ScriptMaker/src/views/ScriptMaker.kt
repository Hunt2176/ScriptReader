package views

import javafx.fxml.FXML
import Scripter.*
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.MouseButton
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import java.nio.file.Paths

class ScriptMaker
{
    @FXML private lateinit var var_list: ListView<String>
    @FXML private lateinit var line_list: ListView<String>
    @FXML private lateinit var add_var: Button
    @FXML private lateinit var add_line: Button
    @FXML private lateinit var newScriptButton: MenuItem
    @FXML private lateinit var openScriptButton: MenuItem
    @FXML private lateinit var saveScriptButton: MenuItem

    private var localFilePath = ""
    private var localScript = Script()

    fun initialize()
    {
        updateFromScript()
        add_line.setOnMouseClicked { initializeNewLine() }
        add_var.setOnMouseClicked { initializeNewVariable() }
        openScriptButton.setOnAction {
            val file = chooseFile()
            if (file != null)
            {
                this.localFilePath = file.absolutePath
                this.setScript(ScriptReader().readIn(file))
            }
        }

        saveScriptButton.setOnAction { saveFile() }
        newScriptButton.setOnAction { newFile() }

        line_list.setCellFactory { lv ->
            val cell = ListCell<String>()
            val context = ContextMenu()

            context.items.add(MenuItem("Edit").apply { setOnAction { editLine(cell.index) } })
            context.items.add(MenuItem("Remove").apply { setOnAction { lv.items.remove(cell.item) } })

            cell.textProperty().bind(cell.itemProperty())
            cell.setOnMouseClicked {
                if (it.clickCount == 2 && it.button == MouseButton.PRIMARY)
                    editLine(cell.index)
            }
            cell.emptyProperty().addListener { _, _, isNowEmpty ->
                if (isNowEmpty)
                {
                    cell.contextMenu = null
                }
                else
                {
                    cell.contextMenu = context
                }
            }
            cell
        }
    }

    fun setScript(newScript: Script)
    {
        localScript = newScript
        updateFromScript()
    }

    private fun updateFromScript()
    {
        var_list.items.clear()
        var_list.items.addAll(localScript.getVariableList())

        line_list.items.clear()
        line_list.items.addAll(localScript.rawLines)
    }

    private var popUpWindow: Stage? = null
    private var popupController: Any? = null
    private fun initializeNewLine()
    {
        if (popUpWindow != null)
        {
            popUpWindow?.requestFocus()
            return
        }
        popUpWindow = Stage()
        popUpWindow?.title = "Add new Line"

        val loader = FXMLLoader(javaClass.getResource("/views/NewLine.fxml"))
        val parent = loader.load<Parent>()
        val controller = loader.getController<NewLine>()

        controller.setScript(localScript)
        controller.setOnComplete {
            if (it.getCurrentText().isNotEmpty())
            {
                this.localScript.addLine(it.getCurrentText())
                this.updateFromScript()
            }
            popUpWindow?.close()
            popUpWindow = null
        }

        popUpWindow?.setOnCloseRequest { popUpWindow = null }

        popUpWindow?.scene = Scene(parent)
        popupController = controller
        popUpWindow?.show()
    }

    private fun initializeNewVariable()
    {
        if (popUpWindow != null)
        {
            popUpWindow?.requestFocus()
            return
        }

        popUpWindow = Stage()
        popUpWindow?.title = "Add new Variable"

        val loader = FXMLLoader(javaClass.getResource("/views/NewVariable.fxml"))
        val parent = loader.load<Parent>()
        val controller = loader.getController<NewVariable>()

        controller.setOnComplete {
            if (it.getCurrentText().isNotEmpty())
            {
                this.localScript.addVariable(ScriptVariable(it.getCurrentText()))
                updateFromScript()
            }
            popUpWindow?.close()
            popUpWindow = null
        }

        popUpWindow?.setOnCloseRequest { popUpWindow = null }
        popUpWindow?.scene = Scene(parent)
        popupController = controller
        popUpWindow?.show()
    }

    private fun editLine(index: Int)
    {
        val line = line_list.items[index]
        initializeNewLine()
        (popupController as? NewLine)?.apply {
            setCurrentText(line)
            setOnComplete {
                if (it.getCurrentText().isNotEmpty()) {
                    line_list.items.removeAt(index)
                    line_list.items.add(index, it.getCurrentText())
                }
                popUpWindow?.close()
                popUpWindow = null
            }
        }
    }

    private fun newFile()
    {
        localFilePath = Paths.get("").toFile().absolutePath
        this.setScript(Script())
    }

    private fun saveFile()
    {
        val script = Script().apply { line_list.items.forEach { addLine(it) } }
        val picker = FileChooser()
        val file = picker.showSaveDialog(Stage())
        if (localFilePath.isNotEmpty()) picker.initialDirectory = File(localFilePath).parentFile
        if (file != null) ScriptWriter(file).writeScript(script)
    }

    private fun chooseFile(): File?
    {
        val picker = FileChooser()
        picker.title = "Open Script File"
        picker.initialDirectory = File(Paths.get("").toUri())
        picker.extensionFilters.add(FileChooser.ExtensionFilter("Script File", "*.script"))

        return picker.showOpenDialog(Stage())
    }

}