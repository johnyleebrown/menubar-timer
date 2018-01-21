package me.grigorii.menubartimer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

/**
 * This file is part of Menu bar Timer.
 *
 * Menu bar Timer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Menu bar Timer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Menu bar Timer. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Created by github.com/johnyleebrown
 *
 */
public class Helper {

    @Deprecated
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

    @Deprecated
    private static void setWorkTime(int[] workTime, int h, int t, int o) {
        workTime[0] = h;
        workTime[1] = t;
        workTime[2] = o;
    }

    @Deprecated
    public static void printDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        final String strDate = simpleDateFormat.format(calendar.getTime());
        System.out.println(strDate);
    }
}
