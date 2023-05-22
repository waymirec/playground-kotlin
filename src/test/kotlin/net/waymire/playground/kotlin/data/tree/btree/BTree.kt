package net.waymire.playground.kotlin.data.tree.btree

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class BTreeTest {

    @Test
    fun `given a B-Tree, when toList() called, then return the tree in-order`() {
        val values = listOf(40, 41, 50, 56, 57, 60, 61, 68, 70, 73, 78, 80, 84, 87, 90, 93, 95, 97, 99)
        val tree = values.toBTree(5)
        val list = tree.toList()
        assertEquals(values, list)
    }

    @Test
    fun `given a B-Tree, when a leaf-node with extra capacity has a key removed, then remove key`() {
        val values = listOf(40, 41, 50, 56, 57, 60, 61, 68, 70, 73, 78, 80, 84, 87, 90, 93, 95, 97, 99)
        val expected = listOf(40, 41, 50, 56, 57, 60, 61, 68, 70, 73, 78, 80, 84, 87, 90, 93, 97, 99)
        val tree = values.toBTree(5)

        //tree.remove(95)
        val list = tree.toList()

        assertEquals(expected, list)
    }

    @Test
    fun `given a B-Tree, when an internal node without extra capacity has a key removed, then promote a value`() {
        val values = listOf(40, 41, 50, 56, 57, 60, 61, 68, 70, 73, 78, 80, 84, 87, 90, 93, 95, 97, 99)
        val tree = values.toBTree(5)
        val found = tree.contains(99)
        assertTrue(found)
    }
}