package id.kris.lenganrobot;

import android.os.AsyncTask;

public class SendCombinationTask extends AsyncTask<Void, Void, Void> {

    private static final long DELAY = 1000; // 1 second delay
    private KontrolActivity activity;

    public SendCombinationTask(KontrolActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            // 1. Base to 30
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new PublishToSliderBase(activity).execute("30°");
                }
            });
            Thread.sleep(DELAY);

            // 2. Grip to 180
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new PublishToSliderGrip(activity).execute("180°");
                }
            });
            Thread.sleep(DELAY);

            // 3. Shoulder to 30
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new PublishToSliderShoulder(activity).execute("30°");
                }
            });
            Thread.sleep(DELAY);

            // 4. Elbow to 30
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new PublishToSliderElbow(activity).execute("30°");
                }
            });
            Thread.sleep(DELAY);

            // 5. Grip to 0
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new PublishToSliderGrip(activity).execute("0°");
                }
            });
            Thread.sleep(DELAY);

            // 6. Base to 120
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new PublishToSliderBase(activity).execute("120°");
                }
            });
            Thread.sleep(DELAY);

            // 7. Grip to 180
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new PublishToSliderGrip(activity).execute("180°");
                }
            });
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
