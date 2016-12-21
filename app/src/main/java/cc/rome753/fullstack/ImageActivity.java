package cc.rome753.fullstack;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import cc.rome753.fullstack.callback.HttpHandler;
import cc.rome753.fullstack.manager.OkhttpManager;
import uk.co.senab.photoview.PhotoView;

public class ImageActivity extends BaseActivity {

    public static void start(BaseActivity activity, String imageUrl){
        Intent i = new Intent(activity, ImageActivity.class);
        i.putExtra("image_url", imageUrl);
        activity.startActivity(i);
    }

    String mImageUrl;

    @BindView(R.id.image)
    PhotoView mPhotoView;

    @Override
    public int setView() {
        return R.layout.activity_image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionBar.setDisplayHomeAsUpEnabled(true);
        mImageUrl = getIntent().getStringExtra("image_url");

        Glide.with(mActivity).load(mImageUrl).into(mPhotoView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_online).setVisible(false);
        menu.findItem(R.id.action_edit).setVisible(true);
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

    private static int REQUEST_GET_IMAGE = 1;

    private void selectImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GET_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_GET_IMAGE && resultCode == RESULT_OK){
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                // 获取缩略图-压缩大小
                Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 500, 500);
                mPhotoView.setImageBitmap(thumbnail);

                // 保存到本地-压缩质量
                String path = Utils.saveBitmap(thumbnail);
                // 上传头像
                OkhttpManager.upload("avatar", new File(path), new HttpHandler() {
                    @Override
                    public void onSuccess(String response) {
                        Utils.toast(response);
                    }

                    @Override
                    public void onFailure() {
                    }
                });
            } catch (Exception e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("test","scale: "+mPhotoView.getScale());
        return super.dispatchTouchEvent(ev);
    }

}
