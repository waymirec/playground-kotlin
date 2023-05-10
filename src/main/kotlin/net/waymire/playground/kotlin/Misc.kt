package net.waymire.playground.kotlin

fun <T: Any> MutableList<T>.toImmutableIterator(): Iterator<T> {
    val immutableCollection: List<T> = this
    return immutableCollection.iterator()
}