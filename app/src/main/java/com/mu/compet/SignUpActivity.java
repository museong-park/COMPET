package com.mu.compet;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {


    private static final int RC_GET_IMAGE = 1;
    private static final int RC_CAMERA = 2;
    private static final int RC_CROP = 3;

    ImageView imageProfile;
    ImageView imageCamera;

    EditText editId;
    EditText editNickName;
    EditText editPassword;
    EditText editPasswordCheck;

    Button completeButton;

    private String mCurrentPhotoPath;
    private Uri contentUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initToolBar(getString(R.string.activity_sign_up));

        if (savedInstanceState != null) {
            contentUri = Uri.parse(savedInstanceState.getString("media_url"));
        }

        imageProfile = (ImageView) findViewById(R.id.image_profile);
        imageCamera = (ImageView) findViewById(R.id.image_camera);

        editId = (EditText) findViewById(R.id.edit_id);
        editNickName = (EditText) findViewById(R.id.edit_nickname);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editPasswordCheck = (EditText) findViewById(R.id.edit_password_check);
        editPasswordCheck.addTextChangedListener(setTextWatcher());

        completeButton = (Button) findViewById(R.id.btn_complete);

        imageCamera.bringToFront();


        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!path.exists()) {
            path.mkdirs();
        }
    }

    public void addPictureClicked(View view) {
        // 갤러리를 통한 이미지 가져오기
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RC_GET_IMAGE);

    }

    public void addCameraClicked(View view) {
        // 카메라를 통한 이미지 가져오기
        dispatchTakePictureIntent();

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                contentUri = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, RC_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private TextWatcher setTextWatcher() {

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String edit = s.toString();
                if (edit.length() > 0 && editId.getText().toString().length() > 0
                        && editNickName.getText().toString().length() > 0
                        && editPasswordCheck.getText().toString().length() > 0) { //활성화
                    completeButton.setBackgroundColor(getResources().getColor(R.color.mainRedColor));
                    completeButton.setEnabled(true);

                } else {
                    completeButton.setBackgroundColor(getResources().getColor(R.color.gray));
                    completeButton.setEnabled(false);
                }


            }
        };
        return textWatcher;
    }


    private void initToolBar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        TextView titleText = (TextView) findViewById(R.id.toolbar_title);
        titleText.setText(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void loginCompleteSignUp(View view) {

        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void duplicateIdCheckClicked(View view) {
        // id 중복 요청
    }

    public void duplicateNickNameCheckClicked(View view) {
        // nickname 중복 요청
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RC_GET_IMAGE:
                    contentUri = intent.getData();
                case RC_CAMERA:
                    rotatePhoto();
                    cropImage(contentUri);
                    break;
                case RC_CROP:
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        Bitmap bitmap = (Bitmap) extras.get("data");
                        imageProfile.setImageBitmap(bitmap);

                        if (mCurrentPhotoPath != null) {
                            File f = new File(mCurrentPhotoPath);
                            if (f.exists()) {
                                f.delete();
                            }
                            mCurrentPhotoPath = null;
                        }
                    }
                    break;
            }
        }
    }

    private void cropImage(Uri contentUri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri of image
        cropIntent.setDataAndType(contentUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, RC_CROP);
    }

    public Bitmap getBitmap() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inInputShareable = true;
        options.inDither = false;
        options.inTempStorage = new byte[32 * 1024];
        options.inPurgeable = true;
        options.inJustDecodeBounds = false;

        File f = new File(mCurrentPhotoPath);

        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            //TODO do something intelligent
            e.printStackTrace();
        }

        Bitmap bm = null;

        try {
            if (fs != null) bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, options);
        } catch (IOException e) {
            //TODO do something intelligent
            e.printStackTrace();
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }

    public void rotatePhoto() {
        ExifInterface exif;
        try {
            if (mCurrentPhotoPath == null) {
                mCurrentPhotoPath = contentUri.getPath();
            }
            exif = new ExifInterface(mCurrentPhotoPath);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = exifOrientationToDegrees(exifOrientation);
            if (exifDegree != 0) {
                Bitmap bitmap = getBitmap();
                Bitmap rotatePhoto = rotate(bitmap, exifDegree);
                saveBitmap(rotatePhoto);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public static Bitmap rotate(Bitmap image, int degrees) {
        if (degrees != 0 && image != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) image.getWidth(), (float) image.getHeight());

            try {
                Bitmap b = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), m, true);

                if (image != b) {
                    image.recycle();
                    image = b;
                }

                image = b;
            } catch (OutOfMemoryError ex) {
                ex.printStackTrace();
            }
        }
        return image;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current state
        if(contentUri != null) {
            savedInstanceState.putString("media_url", contentUri.toString());
        }
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    public void saveBitmap(Bitmap bitmap) {
        File file = new File(mCurrentPhotoPath);
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
