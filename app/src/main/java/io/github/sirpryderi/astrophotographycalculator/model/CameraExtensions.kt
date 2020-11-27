package io.github.sirpryderi.astrophotographycalculator.model

import android.content.Context
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

fun loadCameras(context: Context): ArrayList<Camera> {
    val inputStream: InputStream = context.assets.open("cameras.csv")
    val reader = BufferedReader(InputStreamReader(inputStream))
    val cameras = ArrayList<Camera>()

    // read off the header
    reader.readLine()

    while (reader.ready()) {
        val line = reader.readLine()
        val columns = line.split(",")
        val camera = Camera(columns[0], columns[1], columns[2].toDoubleOrNull(), columns[3].toIntOrNull(), columns[4].toDoubleOrNull(), columns[5].toIntOrNull(), columns[6].toDouble(), columns[7].toDouble(), columns[8].toInt(), columns[9].toInt())
        cameras.add(camera)
    }

    cameras.sortBy { it.toString() }

    return cameras
}

fun List<Camera>.find(string: String): Camera? {
    val results = this.filter { camera -> camera.toString() == string }
    return if (results.isEmpty()) null
    else results.first()
}

fun List<Camera>.toCameraNames(): List<String> {
   return this.map { camera -> camera.toString() }
}