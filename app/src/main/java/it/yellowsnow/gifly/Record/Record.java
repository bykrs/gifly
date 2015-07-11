package it.yellowsnow.gifly.Record;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import it.yellowsnow.gifly.ErrorHandler.ErrorGifly;
import it.yellowsnow.gifly.Gifly;
import it.yellowsnow.gifly.Helpers.CameraHelper;
import it.yellowsnow.gifly.Objects.Permalink;
import it.yellowsnow.gifly.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


public class Record extends Activity {

    final ErrorGifly error = new ErrorGifly();
    private it.yellowsnow.gifly.Interfaces.Record api;


    private Camera mCamera;
    private SurfaceView mPreview;
    private MediaRecorder mMediaRecorder;
    private VideoView playback;

    private boolean isRecording = false;
    private static final String TAG = "Recorder";
    private Button captureButton;
    private Button UploadVideo;
    private EditText hashtags;

    public String save_to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.record);
        mPreview = (SurfaceView) findViewById(R.id.surface_view);
        captureButton = (Button) findViewById(R.id.button_capture);
        UploadVideo = (Button) findViewById(R.id.UploadVideo);
        playback = (VideoView) findViewById(R.id.playback);
        hashtags = (EditText) findViewById(R.id.hashtags);


        new MediaPrepareTask().execute(null, null, null);

    }



    /**
     * The capture button controls all user interaction. When recording, the button click
     * stops recording, releases {@link android.media.MediaRecorder} and {@link android.hardware.Camera}. When not recording,
     * it prepares the {@link android.media.MediaRecorder} and starts recording.
     *
     * @param view the view generating the event.
     */


    public void onCaptureClick(View view) {
        if (isRecording) {
            // BEGIN_INCLUDE(stop_release_media_recorder)

            // stop recording and release camera
            mMediaRecorder.stop();  // stop the recording
            releaseMediaRecorder(); // release the MediaRecorder object
            mCamera.lock();         // take camera access back from MediaRecorder

            // inform the user that recording has stopped
            setCaptureButtonText("Upload");

            isRecording = false;
            releaseCamera();
            // END_INCLUDE(stop_release_media_recorder)

        } else {

            // BEGIN_INCLUDE(prepare_start_media_recorder)

            new MediaPrepareTask().execute(null, null, null);

            // END_INCLUDE(prepare_start_media_recorder)

        }
    }

    private void setCaptureButtonText(String title) {

        if(title.equals("Upload")){
            mPreview.setVisibility(View.INVISIBLE);
            playback.setVisibility(View.VISIBLE);
            playback.setVideoPath(save_to);
            playback.start();
            playback.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playback.start();
                }
            });
            captureButton.setVisibility(View.INVISIBLE);
            UploadVideo.setVisibility(View.VISIBLE);
            hashtags.setVisibility(View.VISIBLE);
        }
        else{
            if(playback.getVisibility() == View.VISIBLE) {
                finish();
                startActivity(getIntent());
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        // if we are using MediaRecorder, release it first
        releaseMediaRecorder();
        // release the camera immediately on pause event
        releaseCamera();
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            // clear recorder configuration
            mMediaRecorder.reset();
            // release the recorder object
            mMediaRecorder.release();
            mMediaRecorder = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
            mCamera.lock();
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            // release the camera for other applications
            mCamera.release();
            mCamera = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private boolean prepareVideoRecorder(){


        // BEGIN_INCLUDE (configure_preview)
        mCamera = CameraHelper.getDefaultCameraInstance();

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        mCamera.setDisplayOrientation(90);
        mCamera.setParameters(parameters);


        // BEGIN_INCLUDE (configure_media_recorder)
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();

        //Camera.Parameters parameters = mCamera.getParameters();


        mMediaRecorder.setCamera(mCamera);
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA);

        //Log.d(TAG, profile.videoBitRate+"<- videoBitRate r");
        profile.videoBitRate = 1024000;

        // Step 2: Set sources
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set all values contained in profile except audio settings
        mMediaRecorder.setOutputFormat(profile.fileFormat);
        mMediaRecorder.setVideoEncoder(profile.videoCodec);
        mMediaRecorder.setVideoEncodingBitRate(profile.videoBitRate);
        mMediaRecorder.setVideoFrameRate(profile.videoFrameRate);
        mMediaRecorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);
        mMediaRecorder.setOrientationHint(90);
        mMediaRecorder.setVideoFrameRate(24);

        // Step 4: Set output file
        save_to = CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_VIDEO).toString();
        mMediaRecorder.setOutputFile(save_to);

        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        // Step 5: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    /**
     * Asynchronous task for preparing the {@link android.media.MediaRecorder} since it's a long blocking
     * operation.
     */
    class MediaPrepareTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            // initialize video camera
            if (prepareVideoRecorder()) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mMediaRecorder.start();

                isRecording = true;
            } else {
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                Record.this.finish();
                //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, "/"));
            }
            // inform the user that recording has started
            Log.d(TAG, "Inform the gallery that there's a new file and refresh: "+save_to);
            galleryAddPic(save_to);

            setCaptureButtonText("Stop");

        }
    }

    public void galleryAddPic(String file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(file);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    public it.yellowsnow.gifly.Interfaces.Record getApi() {
        if (api == null) {
            api = it.yellowsnow.gifly.Services.Record.getService();
        }
        return api;
    }

    public void UploadVideo(View v){

        hashtags = (EditText) findViewById(R.id.hashtags);

        String hashtags_ = hashtags.getText().toString();


        if(hashtags_.trim().equals("")){
            error.displayErrorMessage(Record.this, "Please add some hashtags");
        }
        else{

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);
            //EditText VideoUri = (EditText) findViewById(R.id.VideoUri);
            progressBar.setVisibility(View.VISIBLE);

            final ErrorGifly error = new ErrorGifly();

            File file = new File(save_to);
            TypedFile typedfile = new TypedFile("application/octet-stream", file);

            getApi().post(hashtags_, typedfile, new Callback<List<Permalink>>() {

                @Override
                public void success(List<Permalink> permalinks, Response response) {
                    if (permalinks.isEmpty()) {
                        error.displaySadMessage(Record.this);
                    }
                    //mAdapter.setPosts(posts);
                    Reload_Interface(permalinks.get(0).title);
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    error.displayErrorMessage(Record.this);
                }
            });

        }
    }

    public void Reload_Interface(String title){
        Intent intent = new Intent(this, Gifly.class);
        intent.putExtra("title", title);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

}
