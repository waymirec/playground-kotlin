package net.waymire.playground.kotlin.data.tree.bptree

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class BTreeTest {
    @Test
    fun `given a B+Tree, when iterating, then return the tree in-order`() {
        val count = 1_000_000
        val tree = BTree<Int, String>(3)
        for (i in 1 .. count) tree.put(i, "Record #$i")
        val records = tree.asSequence().toList()
        val expected = (1..count).map { TreeRecord<Int, String>(it, "Record #$it") }
        assertEquals(expected, records)
    }

    @Test
    fun `given a B+Tree, when iterating keys, then return the tree in-order`() {
        val count = 1_000_000
        val tree = BTree<Int, String>(3)
        for (i in 1 .. count) tree.put(i, "Record #$i")
        val list = tree.keys()
        val expected = (1..count).toList()
        assertEquals(expected, list)
    }

    @Test
    fun `given a B+Tree, when iterating values, then return the tree in-order`() {
        val count = 1_000_000
        val tree = BTree<Int, String>(3)
        for (i in 1 .. count) tree.put(i, "Record #$i")
        val list = tree.values()
        val expected = (1..count).map { "Record #$it" }
        assertEquals(expected, list)
    }
}