package mekanism.stellar.common.provider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RecipePattern {

    @Nonnull
    public final String row1;
    @Nullable
    public final String row2;
    @Nullable
    public final String row3;

    private RecipePattern(String row1) {
        this(row1, null, null);
    }

    private RecipePattern(String row1, @Nullable String row2) {
        this(row1, row2, null);
    }

    private RecipePattern(String row1, @Nullable String row2, @Nullable String row3) {
        this.row1 = row1;
        this.row2 = row2;
        this.row3 = row3;
    }

    public static RecipePattern createPattern(DoubleLine row1) {
        return new RecipePattern(row1.columns);
    }

    public static RecipePattern createPattern(char row1, char row2) {
        return new RecipePattern(Character.toString(row1), Character.toString(row2));
    }

    public static RecipePattern createPattern(DoubleLine row1, DoubleLine row2) {
        return new RecipePattern(row1.columns, row2.columns);
    }

    public static RecipePattern createPattern(TripleLine row1) {
        return new RecipePattern(row1.columns);
    }

    public static RecipePattern createPattern(TripleLine row1, TripleLine row2) {
        return new RecipePattern(row1.columns, row2.columns);
    }

    public static RecipePattern createPattern(char row1, char row2, char row3) {
        return new RecipePattern(Character.toString(row1), Character.toString(row2), Character.toString(row3));
    }

    public static RecipePattern createPattern(DoubleLine row1, DoubleLine row2, DoubleLine row3) {
        return new RecipePattern(row1.columns, row2.columns, row3.columns);
    }

    public static RecipePattern createPattern(TripleLine row1, TripleLine row2, TripleLine row3) {
        return new RecipePattern(row1.columns, row2.columns, row3.columns);
    }

    public static class DoubleLine {

        private final String columns;

        private DoubleLine(String columns) {
            this.columns = columns;
        }

        public static DoubleLine of(char column1, char column2) {
            return new DoubleLine(Character.toString(column1) + column2);
        }
    }

    public static class TripleLine {

        private final String columns;

        private TripleLine(String columns) {
            this.columns = columns;
        }

        public static TripleLine of(char column1, char column2, char column3) {
            return new TripleLine(Character.toString(column1) + column2 + column3);
        }
    }
}