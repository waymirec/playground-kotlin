package net.waymire.playground.kotlin.data.tree.btree

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class BTreeTest {

    @Test
    fun `foo`() {
        val values = listOf(10, 20, 30, 40, 50, 60, 70, 80, 90)
        val tree = values.toBTree(6)
        println("foo")
    }

    @Test
    fun `given a B-Tree, when toList() called, then return the tree in-order`() {
        val values = listOf(10, 30, 50, 70, 90, 20, 40, 60, 80)
        val tree = values.toBTree(6)

        val expected = listOf(10, 20, 30, 40, 50, 60, 70, 80, 90)
        val list = tree.toList()
        assertEquals(expected, list)

        val found = tree.contains(80)
        assertTrue(found)
    }

    @Test
    fun `given a B-Tree, when a leaf-node with enough keys a key removed, then remove key`() {
        val values = listOf(10, 20, 30, 40, 50, 60, 70, 80, 90)
        val tree = values.toBTree(6)
        val found = tree.contains(80)
        assertTrue(found)
    }
}