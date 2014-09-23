package main.sample;

import com.twotoasters.multicolumnlistadapter.sample.R;

public final class ToasterData {

    private static final Object[][] TOASTER_INFO = {
        {"Rachit", R.drawable.rachit},
        {"Adit", R.drawable.adit},
        {"Matt", R.drawable.matt},
        {"James", R.drawable.james},
        {"Josh", R.drawable.josh},
        {"Simon", R.drawable.simon},
        {"Kayla", R.drawable.kayla},
        {"Gonwa", R.drawable.gonwa},
        {"Sara", R.drawable.sara},
        {"Dustin", R.drawable.dustin},
        {"Darwin", R.drawable.darwin},
        {"Joe", R.drawable.joe},
        {"Karl", R.drawable.karl},
        {"Fred", R.drawable.fred},
        {"Scott", R.drawable.scott},
        {"Andrew", R.drawable.andrew},
        {"Kevin", R.drawable.kevin},
        {"Duncan", R.drawable.duncan},
        {"Dirk", R.drawable.dirk},
        {"Prachi", R.drawable.prachi},
        {"Chris", R.drawable.chris},
        {"Tom", R.drawable.tom},
        {"Curtis", R.drawable.curtis}
    };

    private static Toaster[] toasters;

    private ToasterData() { }

    public static Toaster[] getToasters() {
        if (toasters == null) {
            int numToasters = TOASTER_INFO.length;
            toasters = new Toaster[numToasters];
            for (int t = 0; t < numToasters; t++) {
                toasters[t] = new Toaster((String) TOASTER_INFO[t][0], (Integer) TOASTER_INFO[t][1]);
            }
        }
        return toasters;
    }

    public static class Toaster {
        public String name;
        public int imageResId;

        public Toaster(String name, int imageResId) {
            this.name = name;
            this.imageResId = imageResId;
        }
    }
}
