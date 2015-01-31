package ie.iamshanedoyle.craftbeers.ui;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import ie.iamshanedoyle.craftbeers.R;
import ie.iamshanedoyle.craftbeers.models.Beer;

/**
 * This activity is used for displaying information about a beer.
 *
 * @author Shane Doyle <@ElWexicano>
 */
public class BeerViewActivity extends ActionBarActivity {

    private Beer mBeer;
    private ImageView mImageViewLabel;
    private TextView mTextViewTitle;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_view);

        if (getIntent() != null) {
            if (getIntent().hasExtra(BeersActivity.EXTRA_BEER)) {
                mBeer = getIntent().getParcelableExtra(BeersActivity.EXTRA_BEER);
            }
        }

        if (mBeer == null) {
            return;
        }

        mTextViewTitle = (TextView) findViewById(R.id.text_view_title);

        if (mTextViewTitle != null) {
            mTextViewTitle.setText(mBeer.getName());
        }

        TextView textViewDescription = (TextView) findViewById(R.id.text_view_description);

        if (textViewDescription != null) {
            textViewDescription.setText(mBeer.getDescription());
        }

        mImageViewLabel = (ImageView) findViewById(R.id.image_view_label);

        if (mImageViewLabel != null && mBeer.hasLabel()) {
            Picasso.with(this).load(mBeer.getLabels().getIcon()).into(mImageViewLabel,
                    new Callback.EmptyCallback() {
                @Override
                public void onSuccess() {
                    getColorFromImage();
                }
            });
        }

        mActionBar = getSupportActionBar();

        getSupportActionBar().setTitle(mBeer.getName() + " Beer");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_beer_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Gets the color from the image to use as the action bar color.
     */
    private void getColorFromImage() {
        Bitmap bitmap = ((BitmapDrawable) mImageViewLabel.getDrawable()).getBitmap();

        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {

                Palette.Swatch swatch = palette.getVibrantSwatch();
                Palette.Swatch swatchDark = palette.getDarkVibrantSwatch();

                if (swatch != null) {
                    mActionBar.setBackgroundDrawable(new ColorDrawable(swatch.getRgb()));
                    mTextViewTitle.setTextColor(swatch.getTitleTextColor());
                }

                if (swatchDark != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(swatchDark.getRgb());
                    }
                }
            }
        });
    }

    /**
     * Show the about dialog.
     */
    private void showAboutDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.about_dialog);
        dialog.show();
    }
}