package Scripter

import java.io.File
import java.lang.Exception

class ScriptReader
{
    fun readIn(file: File, variables: Map<ScriptVariable, String>? = null): Script
    {
        return try
        {
            if (variables == null)
            {
                Script(file.readLines()).apply { removeAll("") }
            }
            else
            {
                Script(file.readLines(), variables).apply { removeAll("") }
            }
        }
        catch (e: Exception)
        {
            Script(arrayListOf())
        }
    }

    fun readIn(file: File, variables: VariableMapper): Script
    {
        return readIn(file, variables.getMap())
    }
}