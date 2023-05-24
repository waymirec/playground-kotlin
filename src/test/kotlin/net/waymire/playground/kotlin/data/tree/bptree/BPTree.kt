package net.waymire.playground.kotlin.data.tree.bptree

import org.junit.jupiter.api.Test

internal class BPTreeTest {
    @Test
    fun `foo`() {
        val tree = BPTree<Int, String>(3)
        for(i in 1..10) {
           tree.put(i, i.toString())
        }
        println("foo")
    }
}