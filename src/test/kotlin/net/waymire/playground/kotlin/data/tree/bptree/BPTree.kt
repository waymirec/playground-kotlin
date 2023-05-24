package net.waymire.playground.kotlin.data.tree.bptree

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class BPTreeTest {
    @Test
    fun `given a B+Tree, when iterating keys, then return the tree in-order`() {
        val count = 1_000_000
        val tree = BPTree<Int, String>(3)
        for (i in 1 .. count) tree.put(i, "Record #$i")
        val list = tree.keysSequence().toList()
        val expected = (1..count).toList()
        assertEquals(expected, list)
    }

    @Test
    fun `given a B+Tree, when iterating values, then return the tree in-order`() {
        val count = 1_000_000
        val tree = BPTree<Int, String>(3)
        for (i in 1 .. count) tree.put(i, "Record #$i")
        val list = tree.valuesSequence().toList()
        val expected = (1..count).map { "Record #$it" }
        assertEquals(expected, list)
    }
}