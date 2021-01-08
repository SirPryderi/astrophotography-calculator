package io.github.sirpryderi.astrophotographycalculator.model

data class EVScene(val name: String, val groups: List<String>, val minEv: Int, val maxEv: Int) {
    override fun toString(): String {
        return "${groups.joinToString(", ")}, $name [$minEv,$maxEv]"
    }
}
