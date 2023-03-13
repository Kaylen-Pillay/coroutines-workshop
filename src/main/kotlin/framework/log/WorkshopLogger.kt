@file:Suppress("unused", "UnusedReceiverParameter")

package framework.log

import com.andreapivetta.kolor.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.*

/**
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author: kaylen.pillay
 **/

object WorkshopLogger {

    private const val TAG = "WorkshopLogger"

    fun Any.logThreadInfo(message: String) {
        val messagePrefix = Kolor.foreground("Thread Info Log", Color.BLUE)
        val messageSuffix = Kolor.foreground("END;", Color.BLUE)
        val displayMessage = """
            ${getCurrentTimeStamp()} - $TAG : $messagePrefix ${"{".lightYellow()}
                ${"Message".green()}: "$message",
                ${"Thread Name".green()}: "${Thread.currentThread().name}",
                ${"Thread Id".green()}: "${Thread.currentThread().id}"
            ${"}".lightYellow()} $messageSuffix
        """.trimIndent()
        println(displayMessage)
    }

    suspend fun logCoroutineInfo(message: String) {
        val coroutineName = currentCoroutineContext()[CoroutineName.Key]?.name
            ?: "Not Defined. Try adding CoroutineName(<name>) as an element to your coroutine context."

        val messagePrefix = Kolor.foreground("Coroutine Info Log", Color.BLUE)
        val messageSuffix = Kolor.foreground("END;", Color.BLUE)
        val displayMessage = """
            ${getCurrentTimeStamp()} - $TAG : $messagePrefix ${"{".lightYellow()}
                ${"Message".green()}: "$message",
                ${"Coroutine Name".green()}: "$coroutineName",
                ${"Thread Name".green()}: "${Thread.currentThread().name}",
                ${"Thread Id".green()}: "${Thread.currentThread().id}"
            ${"}".lightYellow()} $messageSuffix
        """.trimIndent()
        println(displayMessage)
    }

    fun Any.logObjectInfo(any: Any, message: String? = null) {
        val messagePrefix = Kolor.foreground("Object Info Log", Color.BLUE)
        val messageSuffix = Kolor.foreground("END;", Color.BLUE)
        val displayMessage = """
            ${getCurrentTimeStamp()} - $TAG : $messagePrefix
                ${(message ?: "Object").green()}: "$any"
            $messageSuffix
        """.trimIndent()
        println(displayMessage)
    }

    fun Any.logInfo(message: String, comparedInstant: Instant? = null, comparedMessage: String? = null): Instant {
        val instant = Clock.System.now()
        val messagePrefix = Kolor.foreground("Info Log", Color.BLUE)
        val messageSuffix = Kolor.foreground("END;", Color.BLUE)
        val displayMessageWithoutCompareInstant = """
            ${getCurrentTimeStamp(instant)} - $TAG : $messagePrefix ${"{".lightYellow()}
                ${"Message".green()}: "$message"
            ${"}".lightYellow()} $messageSuffix
        """.trimIndent()
        val displayMessageWithCompareInstant = """
            ${getCurrentTimeStamp(instant)} - $TAG : $messagePrefix ${"{".lightYellow()}
                ${"Message".green()}: "$message",
                ${(comparedMessage ?: "Time Since Last Info Log").green()}: "${
                    (instant - (comparedInstant ?: instant))
                }"
            ${"}".lightYellow()} $messageSuffix
        """.trimIndent()
        println(if (comparedInstant != null) displayMessageWithCompareInstant else displayMessageWithoutCompareInstant)

        return instant
    }

    fun Exception.logException() {
        val messagePrefix = Kolor.foreground("Info Log", Color.BLUE)
        val messageSuffix = Kolor.foreground("END;", Color.BLUE)
        val displayMessage = """
            ${getCurrentTimeStamp()} - $TAG : $messagePrefix ${"{".lightYellow()}
                ${"Exception".green()}: "$this"
            ${"}".lightYellow()} $messageSuffix
        """.trimIndent()
        println(displayMessage)
    }

    fun getCurrentTimeStamp(instant: Instant = Clock.System.now()): String {
        val dateTime: LocalDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val prefix = "${
            dateTime.dayOfWeek.toString().formatToCapital()
        } ${
            dateTime.dayOfMonth
        } ${
            dateTime.month.toString().formatToCapital()
        } ${
            dateTime.year
        } - ".cyan()

        val suffix = "${dateTime.time}".cyan()
        return """
            [${prefix}${suffix}]
        """.trimIndent()
    }

    private fun String.formatToCapital(): String {
        return lowercase().replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }
}