package constant;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class TestConstant {

    @NoArgsConstructor(access = PRIVATE)
    public class TestFileIn {
        public static final String EXCEL_ART_FRUIT_IN = "converter/artfruit/in/art_fruit_in.xlsx";


    }

    @NoArgsConstructor(access = PRIVATE)
    public class TestFileOut {
        public static final String EXCEL_ART_FRUIT_OUT = "converter/artfruit/out/art_fruit_out.xlsx";
    }
}
