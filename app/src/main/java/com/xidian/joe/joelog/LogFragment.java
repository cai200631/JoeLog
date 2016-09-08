package com.xidian.joe.joelog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.xidian.joe.joelog.Bean.JoeLog;
import com.xidian.joe.joelog.Bean.LogLab;
import com.xidian.joe.joelog.Utils.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Administrator on 2016/8/1.
 */
public class LogFragment extends Fragment {
    public static final String EXTRA_LOG_ID = "com.xidian.joe.joelog.log_id";
    public static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE_CODE = 0;
    private static final int REQUEST_IMG_CODE = 1;

    private JoeLog mLog;
    private EditText mTitleEditText;
    private Button mDateButton;
    private EditText mContentEditText;
    private ImageView mImageView;
    private ImageButton mImageButton;

    private File mPhotoFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID logID = (UUID) getArguments().getSerializable(EXTRA_LOG_ID);
        mLog = LogLab.getInstance(getActivity()).getLog(logID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);  //第三个参数告知布局生成器是否将生成的视图添加给父视图；
        mTitleEditText = (EditText) view.findViewById(R.id.log_edit_title);
        mDateButton = (Button) view.findViewById(R.id.log_btn_date);
        mContentEditText = (EditText) view.findViewById(R.id.log_edit_content);
        mImageView = (ImageView) view.findViewById(R.id.log_image_view);
        mImageButton = (ImageButton) view.findViewById(R.id.log_image_btn);

        init();
        return view;
    }

    private void init() {
        mDateButton.setText(mLog.showDate());
        mTitleEditText.setText(mLog.getTitle());
        mContentEditText.setText(mLog.getContent());
        if (!TextUtils.isEmpty(mLog.getPic())) {
            mImageView.setVisibility(View.VISIBLE);
//            Bitmap bitmap = BitmapFactory.decodeFile(mLog.getPic());
            Bitmap bitmap = cropBitmap(mLog.getPic());
            mImageView.setImageBitmap(bitmap);
//            bitmap.recycle();
        }

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.getInstance(mLog.getDate());
                dialog.setTargetFragment(LogFragment.this, REQUEST_DATE_CODE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLog.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mContentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLog.setContent(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView.setVisibility(View.VISIBLE);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mPhotoFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getTime() + ".jpg");
                System.out.println(mPhotoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(intent, REQUEST_IMG_CODE);
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null);
//                final AlertDialog dialog = new AlertDialog.Builder(getActivity(),R.style.processDialog).create();
                final Dialog dialog = new Dialog(getActivity(), R.style.processDialog);
                ImageView img = (ImageView) imgEntryView.findViewById(R.id.large_image);
                img.setImageBitmap(BitmapFactory.decodeFile(mLog.getPic()));
                dialog.setContentView(imgEntryView);
                dialog.show();
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });


    }

    public static LogFragment newInstance(UUID logId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_LOG_ID, logId);
        LogFragment fragment = new LogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE_CODE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mLog.setDate(date);
            mDateButton.setText(mLog.showDate());
        } else if (requestCode == REQUEST_IMG_CODE) {
            if (mPhotoFile.exists()) {
                Bitmap bitmap = cropBitmap(mPhotoFile.getAbsolutePath());
//                Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
                mImageView.setImageBitmap(bitmap);
//                bitmap.recycle();
                mLog.setPic(mPhotoFile.getAbsolutePath());
            } else {
                ToastUtils.showToast(getActivity(), "照片获取失败");
            }
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        LogLab.getInstance(getActivity()).saveLogs();
    }

    public String getTime() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return format.format(date);
    }

    private Bitmap cropBitmap(String imageUri) {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels; //宽度
        int height = dm.heightPixels; //高度
        //加载图像尺寸而不是图像本身
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true; //bitmap为null 只是把图片的宽高放在Options里
//        Bitmap bitmap
        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);
        //设置图片压缩比例 如果两个比例大于1 图像一边将大于屏幕
        if (heightRatio > 1 && widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }
        //图像真正解码
        bmpFactoryOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imageUri, bmpFactoryOptions);
    }
}