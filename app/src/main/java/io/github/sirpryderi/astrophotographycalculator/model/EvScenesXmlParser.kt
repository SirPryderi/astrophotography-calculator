package io.github.sirpryderi.astrophotographycalculator.model

import android.content.res.XmlResourceParser
import org.xmlpull.v1.XmlPullParser

private val ns: String? = null
private val entries = mutableListOf<EVScene>()
private val groups = mutableListOf<String>()


fun loadScenesFromXml(xml: XmlResourceParser): List<EVScene> {
    entries.clear()

    xml.next()
    xml.next()
    xml.require(XmlPullParser.START_TAG, ns, "scenes")

    while (xml.name != "scenes" || xml.eventType != XmlPullParser.END_TAG) {
        xml.nextTag()

        if (xml.name == "group" && xml.eventType == XmlPullParser.START_TAG) {
            groups.add(xml.getAttributeValue(ns, "name"))
        }

        if (xml.name == "group" && xml.eventType == XmlPullParser.END_TAG) {
            groups.removeLast()
        }

        if (xml.name == "scene" && xml.eventType == XmlPullParser.START_TAG) {
            var evMin = xml.getAttributeIntValue(ns, "ev", 0)
            var evMax = evMin

            val sequence = xml.getAttributeValue(ns, "ev").split(",")
            if (sequence.size == 2) {
                evMin = sequence[0].toInt()
                evMax = sequence[1].toInt()
            }

            xml.next()
            val name = xml.text
            entries.add(EVScene(name, groups.toMutableList(), evMin, evMax))
        }
    }

    return entries
}
