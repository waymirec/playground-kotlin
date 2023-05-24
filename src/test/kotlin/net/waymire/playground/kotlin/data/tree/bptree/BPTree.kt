package net.waymire.playground.kotlin.data.tree.bptree

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class BPTreeTest {
    @Test
    fun `foo`() {
        val tree = BPTree<Int, String>(3)
        for(i in 1..10) {
           tree.put(i, i.toString())
        }
    }

    @Test
    fun `given a B+Tree, when iterating keys, then return the tree in-order`() {
        val tree = BPTree<Int, String>(3)
        for (i in 1 .. 10) tree.put(i, "Record #$i")
        val list = tree.keysSequence().toList()
        val expected = (1..10).toList()
        assertEquals(expected, list)
    }

    @Test
    fun `given a B+Tree, when iterating values, then return the tree in-order`() {
        val tree = BPTree<Int, String>(3)
        for (i in 1 .. 10) tree.put(i, "Record #$i")
        val list = tree.valuesSequence().toList()
        val expected = (1..10).map { "Record #$it" }
        assertEquals(expected, list)
    }
}