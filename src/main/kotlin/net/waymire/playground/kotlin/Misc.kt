package net.waymire.playground.kotlin

class SortedList<T: Comparable<T>>(private val delegate: MutableList<T> = mutableListOf()): MutableList<T> by delegate {
    constructor(collection: Collection<T>) : this(collection.toMutableList())
    override fun add(value: T): Boolean {
        val result = delegate.add(value)
        if (result) delegate.sort()
        return result
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val result = delegate.addAll(elements)
        if (result) delegate.sort()
        return result
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val result = delegate.addAll(index, elements)
        if (result) delegate.sort()
        return result
    }
}
fun <T: Comparable<T>> sortedListOf(): SortedList<T> = SortedList()
fun <T: Comparable<T>> sortedListOf(vararg elements: T): SortedList<T> =
    if (elements.isEmpty()) SortedList() else SortedList(mutableListOf())

fun <T: Any> MutableList<T>.toImmutableIterator(): Iterator<T> {
    val immutableCollection: List<T> = this
    return immutableCollection.iterator()
}

val Int.isEven get() = this % 2 == 0
val Int.isOdd get() = !isEven
fun Boolean.asInt() = if (this) 1 else 0
operator fun Int.plus(bool: Boolean) = this + bool.asInt()