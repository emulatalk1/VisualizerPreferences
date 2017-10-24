package com.vnspectre.visualizerpreferences.AudioVisuals;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Build;

import com.vnspectre.visualizerpreferences.R;

/**
 * Created by Spectre on 10/24/17.
 */

public class AudioInputReader {

    private final VisualizerView mVisualizerView;
    private final Context mContext;
    private MediaPlayer mPlayer;
    private Visualizer mVisualizer;

    public AudioInputReader(VisualizerView mVisualizerView, Context mContext) {
        this.mVisualizerView = mVisualizerView;
        this.mContext = mContext;
        initVisualizer();
    }

    private void initVisualizer() {

        //Setup media player
        mPlayer = MediaPlayer.create(mContext, R.raw.tuyam);
        mPlayer.setLooping(true);

        // Setup the Visualizer
        // Connect it to the media player
        mVisualizer = new Visualizer(mPlayer.getAudioSessionId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mVisualizer.setMeasurementMode(Visualizer.MEASUREMENT_MODE_PEAK_RMS);
            mVisualizer.setScalingMode(Visualizer.SCALING_MODE_NORMALIZED);
        }

        // Set the size of the byte array returned for visualization
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[0]);

        // Whenever audio data is available, update the visualizer view
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {

            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                // If the Visualizer is ready and has data, send that data to the VisualizerView
                if (mVisualizer != null && mVisualizer.getEnabled()) {
                    mVisualizerView.updateFFT(fft);
                }
            }
        },
        Visualizer.getMaxCaptureRate(), false, true);

        // Start everything
        mVisualizer.setEnabled(true);
        mPlayer.start();
    }

    public void shutdown(boolean isFinishing) {

        if (mPlayer != null) {
            mPlayer.pause();
            if (isFinishing) {
                mVisualizer.release();
                mPlayer.release();
                mPlayer = null;
                mVisualizer = null;
            }
        }

        if (mVisualizer != null) {
            mVisualizer.setEnabled(false);
        }
    }

    public void restart() {

        if (mPlayer != null) {
            mPlayer.start();
        }

        mVisualizer.setEnabled(true);
        mVisualizerView.restart();
    }
}
