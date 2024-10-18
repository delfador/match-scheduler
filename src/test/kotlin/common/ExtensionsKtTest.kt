package common

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.ruud.common.pairs

class ExtensionsKtTest {
    @Test
    fun `pairs should be empty for one item`() {
        assertThat(listOf(1).pairs()).isEmpty()
    }

    @Test
    fun `pairs should give correct pairs for 3 items`() {
        assertThat(listOf(1, 2, 3).pairs()).containsExactlyInAnyOrder(
            Pair(1, 2),
            Pair(1, 3),
            Pair(2, 3),
        )
    }

    @Test
    fun `pairs should be in ascending order`() {
        assertThat(listOf(3, 1, 2).pairs()).containsExactlyInAnyOrder(
            Pair(1, 2),
            Pair(1, 3),
            Pair(2, 3),
        )
    }

    @Test
    fun `pairs should give correct size for bigger collection`() {
        val items = ('a'..'j').toSet()
        val nChooseK = (items.size) * (items.size - 1) / 2
        assertThat(items.pairs()).hasSize(nChooseK)
    }

    @Test
    fun `pairs includes pairs of duplicate items`() {
        assertThat(listOf(1, 1, 2).pairs()).containsExactlyInAnyOrder(
            Pair(1, 1),
            Pair(1, 2),
            Pair(1, 2),
        )
    }
}
