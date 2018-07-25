package io.nambm.buildhabit.constant;

public class AppConstant {

    public static final String MON = "mon";
    public static final String TUE = "tue";
    public static final String WED = "wed";
    public static final String THU = "thu";
    public static final String FRI = "fri";
    public static final String SAT = "sat";
    public static final String SUN = "sun";

    public static final long DAY_IN_MILLISECOND = 86400000L;

    public enum DAY_OF_WEEK {
        MON(0, "mon"), TUE(1, "tue"), WED(2, "wed"), THU(3, "thu"), FRI(4, "fri"), SAT(5, "sat"), SUN(6, "sun");
        public final int index;
        public final String shortName;

        DAY_OF_WEEK(int index, String shortName) {
            this.index = index;
            this.shortName = shortName;
        }
    }

    public static final String TEMPLATE = "template";
}
