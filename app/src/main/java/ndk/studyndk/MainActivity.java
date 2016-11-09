package ndk.studyndk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private ImageView outputImage;
    private View.OnClickListener blurHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            try {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
                long start = System.currentTimeMillis();
                Bitmap out = doBlurJniBitMap(bmp, 44, false);
                long end = System.currentTimeMillis();
                ((Button) findViewById(R.id.sample_text)).setText("耗时: " + (end - start));
                outputImage.setImageBitmap(out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private View.OnClickListener blurHandler2=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
                long start = System.currentTimeMillis();
                Bitmap out = ImageBlur.doBlur(bmp, 44, false);
                long end = System.currentTimeMillis();
                ((Button) findViewById(R.id.sample_text)).setText("耗时: " + (end - start));
                outputImage.setImageBitmap(out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public  Bitmap doBlurJniBitMap(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }
        //Jni BitMap
        MainActivity.this.doBlur(bitmap, radius);

        return (bitmap);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        Button tv = (Button) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        findViewById(R.id.convert1).setOnClickListener(blurHandler);
        findViewById(R.id.convert2).setOnClickListener(blurHandler2);
        outputImage = (ImageView) findViewById(R.id.outputImageView);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public  native void doBlur(Bitmap bitmap, int r);

}
