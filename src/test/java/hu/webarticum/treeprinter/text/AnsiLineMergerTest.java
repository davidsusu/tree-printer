package hu.webarticum.treeprinter.text;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AnsiLineMergerTest extends AbstractLineMergerTest {

    @Test
    void testAppendRichToEmpty() {
        assertThat(new AnsiLineMerger().merge("", 0, "\u001B[1mlo\u001B[0mrem")).isEqualTo("\u001B[1mlo\u001B[0mrem");
    }

    @Test
    void testAppendUnclosedRichToEmpty() {
        assertThat(new AnsiLineMerger().merge("", 0, "\u001B[1mlorem")).isEqualTo("\u001B[1mlorem\u001B[0m");
    }

    @Test
    void testInsertRichFarFromEmpty() {
        assertThat(new AnsiLineMerger().merge("", 5, "\u001B[1mlore\u001B[0mm")).isEqualTo("     \u001B[1mlore\u001B[0mm");
    }

    @Test
    void testInsertRichStart() {
        assertThat(new AnsiLineMerger().merge("loremipsum", 0, "\u001B[4ml\u001B[0morem")).isEqualTo("\u001B[4ml\u001B[0moremipsum");
    }

    @Test
    void testInsertRichInside() {
        assertThat(new AnsiLineMerger().merge("loremipsum", 2, "\u001B[4ml\u001B[0morem")).isEqualTo("lo\u001B[4ml\u001B[0moremsum");
    }

    @Test
    void testInsertRichFar() {
        assertThat(new AnsiLineMerger().merge("lorem", 10, "\u001B[1mlore\u001B[0mm")).isEqualTo("lorem     \u001B[1mlore\u001B[0mm");
    }

    @Test
    void testInsertUnclosedRichAtStartOfRich() {
        assertThat(new AnsiLineMerger().merge("\u001B[1mlorem\u001B[0m", 0, "\u001B[2mxy")).isEqualTo("\u001B[2mxy\u001B[0m\u001B[1mrem\u001B[0m");
    }

    @Test
    void testInsertUnclosedRichInsideRich() {
        assertThat(new AnsiLineMerger().merge("\u001B[1mlore\u001B[3mmipsum\u001B[0m", 2, "\u001B[2mdolor"))
                .isEqualTo("\u001B[1mlo\u001B[0m\u001B[2mdolor\u001B[0m\u001B[1m\u001B[3msum\u001B[0m");
    }

    @Test
    void testInsertUnclosedRichReplaceOverflowRich() {
        assertThat(new AnsiLineMerger().merge("\u001B[1mlore\u001B[3mmipsum\u001B[0m", 3, "\u001B[2mdolorsitamet"))
                .isEqualTo("\u001B[1mlor\u001B[0m\u001B[2mdolorsitamet\u001B[0m");
    }

}
