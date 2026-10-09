package constant;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class TestConstant {

    @NoArgsConstructor(access = PRIVATE)
    public class TestFileIn {
        public static final String EXCEL_ART_FRUIT_IN = "converter/artfruit/in/art_fruit_in.xlsx";
        public static final String EXCEL_LENTA_IN = "converter/lenta/in/lenta_in.xlsx";
        public static final String EXCEL_SBER_IN = "converter/sber/in/sber_in.xlsx";
        public static final String EXCEL_NIKA_IN = "converter/nika/in/nika_in.xlsx";
        public static final String EXCEL_SIEL_IN = "converter/siel/in/siel_in.xlsx";
        public static final String EXCEL_SIEL_MILK_IN = "converter/sielmilk/in/siel_milk_in.xlsx";
        public static final String EXCEL_METRO_PRIMARY_IN = "converter/metroprimary/in/metro_primary_in.xlsx";
        public static final String EXCEL_FRAGRANT_WORLD_MSK_IN = "converter/fragrantworldmsk/in/fragrant_world_msk_in.xlsx";
        public static final String EXCEL_RS_LENTA_IN = "converter/rslenta/in/rs_lenta_in.xlsx";
    }

    @NoArgsConstructor(access = PRIVATE)
    public class TestFileOut {
        public static final String EXCEL_ART_FRUIT_OUT = "converter/artfruit/out/art_fruit_out.xlsx";
        public static final String EXCEL_LENTA_OUT = "converter/lenta/out/lenta_out.xlsx";
        public static final String EXCEL_SBER_OUT = "converter/sber/out/sber_out.xlsx";
        public static final String EXCEL_NIKA_OUT = "converter/nika/out/nika_out.xlsx";
        public static final String EXCEL_SIEL_OUT = "converter/siel/out/siel_out.xlsx";
        public static final String EXCEL_SIEL_MILK_OUT = "converter/sielmilk/out/siel_milk_out.xlsx";
        public static final String EXCEL_METRO_PRIMARY_OUT = "converter/metroprimary/out/metro_primary_out.xlsx";
        public static final String EXCEL_FRAGRANT_WORLD_MSK_OUT = "converter/fragrantworldmsk/out/fragrant_world_msk_out.xlsx";
        public static final String EXCEL_RS_LENTA_OUT = "converter/rslenta/out/rs_lenta_out.xlsx";
    }
}
