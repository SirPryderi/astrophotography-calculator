package io.github.sirpryderi.astrophotographycalculator.dummy

import io.github.sirpryderi.astrophotographycalculator.R
import io.github.sirpryderi.astrophotographycalculator.model.Message
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<Message> = ArrayList()

    private val COUNT = 3

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createDummyItem(i))
        }
    }

    private fun addItem(item: Message) {
        ITEMS.add(item)
    }

    private fun createDummyItem(position: Int): Message {
        return Message("Message $position", R.drawable.ic_help_24)
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class DummyItem(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}