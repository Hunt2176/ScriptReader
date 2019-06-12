package main

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import views.ScriptMaker

fun main()
{
    ApplicationMain().initialize()
}

class ApplicationMain: Application()
{
    var fxmlLoader: FXMLLoader? = null

    fun initialize()
    {
        launch(ApplicationMain::class.java)
    }

    override fun start(primaryStage: Stage?)
    {
        primaryStage?.title = "Script Maker"

        fxmlLoader = FXMLLoader(javaClass.getResource("/views/ScriptMaker.fxml"))
        val root = fxmlLoader?.load<Parent>()
        val controller = fxmlLoader?.getController<ScriptMaker>()

        primaryStage?.scene = Scene(root)
        primaryStage?.show()

    }
}