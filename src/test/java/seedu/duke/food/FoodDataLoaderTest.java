package seedu.duke.food;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests loading and validating food catalogue files.
 */
public class FoodDataLoaderTest {

    @TempDir
    private Path temporaryDirectory;

    @Test
    public void load_groupsItemsByCourtAndStall() throws IOException {
        Path foodFile = writeFoodFile(List.of(
                "YIH|Indian|Bhel Puri|3.00",
                "PGPR|Vietnamese|Beef Pho|5.50",
                "YIH|Indian|Plain Dosai|2.90",
                "PGPR|Vietnamese|Spring Roll|2.50",
                "PGPR|Chicken Rice|Char Siew Rice|3.30",
                ""
        ));

        List<FoodCourt> foodCourts = FoodDataLoader.load(foodFile);

        assertEquals(
                List.of("YIH", "PGPR"),
                foodCourts.stream()
                        .map(FoodCourt::name)
                        .toList()
        );

        FoodCourt yih = foodCourts.get(0);

        assertEquals(
                List.of("Indian"),
                yih.stalls()
                        .stream()
                        .map(FoodStall::name)
                        .toList()
        );

        assertEquals(
                List.of("Bhel Puri", "Plain Dosai"),
                yih.stalls().get(0).menu()
                        .stream()
                        .map(FoodItem::name)
                        .toList()
        );

        FoodCourt pgpr = foodCourts.get(1);

        assertEquals(
                List.of("Vietnamese", "Chicken Rice"),
                pgpr.stalls()
                        .stream()
                        .map(FoodStall::name)
                        .toList()
        );
    }

    @Test
    public void load_invalidFieldCount_throwsException()
            throws IOException {
        Path foodFile = writeFoodFile(List.of(
                "PGPR|Vietnamese|Beef Pho"
        ));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> FoodDataLoader.load(foodFile)
        );

        assertTrue(exception.getMessage().contains("line 1"));
    }

    @Test
    public void load_invalidPrice_throwsException()
            throws IOException {
        Path foodFile = writeFoodFile(List.of(
                "PGPR|Vietnamese|Beef Pho|not-a-price"
        ));

        assertThrows(
                IllegalStateException.class,
                () -> FoodDataLoader.load(foodFile)
        );
    }

    @Test
    public void load_blankField_throwsException()
            throws IOException {
        Path foodFile = writeFoodFile(List.of(
                "PGPR|Vietnamese||5.50"
        ));

        assertThrows(
                IllegalStateException.class,
                () -> FoodDataLoader.load(foodFile)
        );
    }

    @Test
    public void load_negativePrice_throwsException()
            throws IOException {
        Path foodFile = writeFoodFile(List.of(
                "PGPR|Vietnamese|Beef Pho|-5.50"
        ));

        assertThrows(
                IllegalStateException.class,
                () -> FoodDataLoader.load(foodFile)
        );
    }

    private Path writeFoodFile(List<String> lines)
            throws IOException {
        Path foodFile = temporaryDirectory.resolve("food.txt");
        Files.write(foodFile, lines);
        return foodFile;
    }

    @Test
    public void load_missingFile_throwsException() {
        Path missingFile = temporaryDirectory.resolve("missing-food.txt");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> FoodDataLoader.load(missingFile)
        );

        assertTrue(exception.getMessage().contains("Could not read food data"));
    }
}
