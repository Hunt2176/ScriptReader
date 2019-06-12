package Scripter

import java.io.File

class ScriptWriter(val file: File)
{
    fun writeLines(lines: Collection<String>)
    {
        file.writeText(lines.joinToString("\n"))
    }

    fun writeScript(script: Script, useVariables: Boolean = false)
    {
        if (useVariables)
        {
            writeLines(script.rawLines)
        }
        else
        {
            writeLines(script.rawLines.mapIndexed { index, _ -> script.getLine(index) })
        }
    }
}