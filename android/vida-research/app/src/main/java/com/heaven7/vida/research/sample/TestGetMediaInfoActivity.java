package com.heaven7.vida.research.sample;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;

import com.heaven7.java.base.util.DefaultPrinter;
import com.heaven7.java.base.util.IOUtils;
import com.heaven7.java.pc.schedulers.Schedulers;
import com.heaven7.vida.research.BaseActivity;
import com.heaven7.vida.research.R;

import java.io.FileDescriptor;
import java.util.Locale;

import butterknife.OnClick;

/**
 * 获取音频专辑信息
 * Created by heaven7 on 2019/5/28.
 */
public class TestGetMediaInfoActivity extends BaseActivity {

    private static final String TAG = "TestGetMediaInfo";
    // private static final String FILE = Environment.getExternalStorageDirectory() + "/vida/resource/musics/M7.mp3";
    private static final String FILE = Environment.getExternalStorageDirectory() +
            "/KuwoMusic/music/当你-林俊杰-908195.mp3";

    @Override
    protected int getLayoutId() {
        return R.layout.ac_get_media_info;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {

    }

    @OnClick(R.id.bt_start)
    public void onClickStart(View view) {
        Schedulers.io().newWorker().schedule(new Runnable() {
            @Override
            public void run() {
                getInfoByMediaStore();
                getInfoByRetriever();
            }
        });
    }

    private void getInfoByMediaStore() {
        String[] projection = {
                // MediaStore.Audio.Media.SIZE,
                // MediaStore.Audio.Media._ID,// id
                MediaStore.Audio.Media.DATA,      // path
                MediaStore.Audio.Media.ALBUM_ID,
                //MediaStore.Audio.Media.ALBUM,      // 专辑
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DISPLAY_NAME,
        };
        //String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri,
                projection, null, null, null);
        try {
            if (cursor == null) {
                System.out.println("cursor is null.");
                return;
            }
            System.out.println(cursor.getCount());
            while (cursor.moveToNext()) {
                long album_id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                DefaultPrinter.getDefault().debug(TAG, "getInfoByMediaStore",
                        String.format(Locale.getDefault(),
                                "displayName = %s, title = %s, artist = %s,  duration = %d",
                                displayName, title, artist, duration));
                System.out.println("album_art: " + getAlbumArt(album_id));
                System.out.println("path: " + path);
            }
            System.out.println("cursor done ------------- <<<");
        } finally {
            IOUtils.closeQuietly(cursor);
        }
    }

    public Bitmap getAlbumArt(long album_id) {
        Bitmap bm = null;
        try {
            final Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
            ParcelFileDescriptor pfd = getContentResolver()
                    .openFileDescriptor(uri, "r");

            if (pfd != null) {
                FileDescriptor fd = pfd.getFileDescriptor();
                bm = BitmapFactory.decodeFileDescriptor(fd);
            }
        } catch (Exception e) {
        }
        return bm;
    }

    private Bitmap getBitmap(String file){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(file);
            byte[] data = mmr.getEmbeddedPicture();
            if(data != null){
                return BitmapFactory.decodeByteArray(data, 0, data.length);
            }
        }catch (Exception e){
            //ignore
        }finally {
            mmr.release();
        }
        return null;
    }

    private void getInfoByRetriever() {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(FILE);
            // api level 10, 即从GB2.3.3开始有此功能
            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            // 专辑名
            String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            // 媒体格式
            String mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            // 艺术家
            String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            // 播放时长单位为毫秒
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            // 从api level 14才有，即从ICS4.0才有此功能
            String bitrate = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
            // 创建时间
            String date = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
        } finally {
            mmr.release();
        }
    }
}
