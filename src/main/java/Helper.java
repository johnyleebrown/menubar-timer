import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Greg on 1/3/18.
 */
public class Helper {

    public static int[] calculateNextTime(String hundreds, String tens, String ones, boolean action) {
        int h = Integer.valueOf(hundreds);
        int t = Integer.valueOf(tens);
        int o = Integer.valueOf(ones);
        int[] workTime = new int[3];

        if (action) {
            if (o == 9) {
                if (t == 9) {
                    if (h == 9) {
                        return null;
                    }
                    setWorkTime(workTime, ++h, 0, 0);
                    return workTime;
                }
                setWorkTime(workTime, h, ++t, 0);
            } else {
                setWorkTime(workTime, h, t, ++o);
            }
        } else {
            if (o == 0) {
                if (t == 0) {
                    if (h == 0) {
                        return null;
                    }
                    setWorkTime(workTime, --h, 9, 9);
                    return workTime;
                }
                setWorkTime(workTime, h, --t, 9);
            } else {
                setWorkTime(workTime, h, t, --o);
            }
        }

        return workTime;
    }

    private static void setWorkTime(int[] workTime, int h, int t, int o) {
        workTime[0] = h;
        workTime[1] = t;
        workTime[2] = o;
    }

    public static void printDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        final String strDate = simpleDateFormat.format(calendar.getTime());
        System.out.println(strDate);
    }
}
