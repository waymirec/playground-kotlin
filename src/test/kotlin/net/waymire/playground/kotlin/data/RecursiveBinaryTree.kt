package net.waymire.playground.kotlin.data

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class RecursiveBinaryTreeTest {
    @ParameterizedTest
    @MethodSource("net.waymire.playground.kotlin.data.BinaryTreeTest#listOfInts")
    fun `given a BST, when in-order sort requested, then should sort in-order`(values: List<Int>) {
        val tree = values.toRecursiveTree()
        val expected = values.sorted()
        val sorted = tree.inOrder()
        assertEquals(expected, sorted)
    }

    @Test
    fun `given a BST, when pre-order sort requested, then should sort pre-order`() {
        val values = listOf(43, 18, 15, 20, 50, 47, 55)
        val expected = values.toList()
        val tree = values.toRecursiveTree()
        val sorted = tree.preOrder()
        assertEquals(expected, sorted)
    }

    @Test
    fun `given a BST, when post-order sort requested, then should sort post-order`() {
        val values = listOf(43, 18, 15, 20, 50, 47, 55)
        val expected = listOf(15, 20, 18, 47, 55, 50, 43)
        val tree = values.toRecursiveTree()
        val sorted = tree.postOrder()
        assertEquals(expected, sorted)
    }

    @Test
    fun `given a BST, when breadth-first-order sort requested, then should sort breadth-first-order`() {
        val values = listOf(43, 18, 15, 20, 50, 47, 55)
        val expected = listOf(43,18,50,15,20,47,55)
        val tree = values.toRecursiveTree()
        val sorted = tree.breadthFirstOrder()
        assertEquals(expected, sorted)
    }

    @ParameterizedTest
    @MethodSource("net.waymire.playground.kotlin.data.BinaryTreeTest#listOfInts")
    fun `given value exists in tree, when contains called, then return true`(values: List<Int>) {
        val tree = values.toTree()
        val check = values.last()
        val result = tree.contains(check)
        assertTrue(result)
    }

    @ParameterizedTest
    @MethodSource("net.waymire.playground.kotlin.data.BinaryTreeTest#listOfInts")
    fun `given value does not exist in tree, when contains called, then return false`(values: List<Int>) {
        val tree = values.toTree()
        val result = tree.contains(Int.MAX_VALUE)
        assertFalse(result)
    }

    companion object {
        @JvmStatic
        fun listOfInts() = Stream.of(
            (1..100).map { Random.nextInt(1, 1000) },
            (1..1_000).map { Random.nextInt(1, 10_000) },
            (1..10_000).map { Random.nextInt(1, 100_000) },
            (1..100_000).map { Random.nextInt(1, 1_000_000) },
            (1..1_000_000).map { Random.nextInt(1, 10_000_000) },
//            (1..10_000_000).map { Random.nextInt(1, 100) },
//            (1..100_000_000).map { Random.nextInt(1, 100) },
//            (1..1_000_000_000).map { Random.nextInt(1, 100) }
        )
    }
}