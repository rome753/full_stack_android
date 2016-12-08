package cc.rome753.fullstack;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends BaseActivity {

    public static void start(BaseActivity activity, String imageUrl){
        Intent i = new Intent(activity, ImageActivity.class);
        i.putExtra("image_url", imageUrl);
        activity.startActivity(i);
    }

    String mImageUrl;
    PhotoViewAttacher mAttacher;

    @BindView(R.id.image)
    PhotoView imageView;

    @Override
    public int setView() {
        return R.layout.activity_image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar.setDisplayHomeAsUpEnabled(true);
        mImageUrl = getIntent().getStringExtra("image_url");

        mAttacher = new PhotoViewAttacher(imageView);

        Glide.with(mActivity).load(mImageUrl)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mAttacher.update();
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            selectImage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectImage() {

    }
}
