package hu.webarticum.treeprinter.text;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

abstract class AbstractLineMergerTest {

    @Test
    void testEmpty() {
        assertThat(new PlainLineMerger().merge("", 0, "")).isEmpty();
    }

    @Test
    void testBlank() {
        assertThat(new PlainLineMerger().merge("", 10, "")).isBlank();
    }

    @Test
    void testAppendToEmpty() {
        assertThat(new PlainLineMerger().merge("", 0, "lorem")).isEqualTo("lorem");
    }

    @Test
    void testInsertFarFromEmpty() {
        assertThat(new PlainLineMerger().merge("", 5, "lorem")).isEqualTo("     lorem");
    }

    @Test
    void testInsertEmpty() {
        assertThat(new PlainLineMerger().merge("lorem", 2, "")).isEqualTo("lorem");
    }

    @Test
    void testInsertEmptyFar() {
        assertThat(new PlainLineMerger().merge("lorem", 10, "")).isEqualTo("lorem     ");
    }

    @Test
    void testAppend() {
        assertThat(new PlainLineMerger().merge("lorem", 5, "ipsum")).isEqualTo("loremipsum");
    }

    @Test
    void testInsertAtStart() {
        assertThat(new PlainLineMerger().merge("lorem", 0, "xy")).isEqualTo("xyrem");
    }

    @Test
    void testInsertInside() {
        assertThat(new PlainLineMerger().merge("lorem", 2, "xy")).isEqualTo("loxym");
    }

    @Test
    void testInsertReplace() {
        assertThat(new PlainLineMerger().merge("lorem", 0, "ipsum")).isEqualTo("ipsum");
    }

    @Test
    void testInsertOverflow() {
        assertThat(new PlainLineMerger().merge("lorem", 2, "ipsum")).isEqualTo("loipsum");
    }

    @Test
    void testInsertReplaceOverflow() {
        assertThat(new PlainLineMerger().merge("lorem", 0, "ipsumxy")).isEqualTo("ipsumxy");
    }

    @Test
    void testInsertAfter() {
        assertThat(new PlainLineMerger().merge("lorem", 5, "ipsum")).isEqualTo("loremipsum");
    }

    @Test
    void testInsertFar() {
        assertThat(new PlainLineMerger().merge("lorem", 9, "ipsum")).isEqualTo("lorem    ipsum");
    }

}
