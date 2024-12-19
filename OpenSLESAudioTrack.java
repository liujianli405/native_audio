package com.baidu.che.codriver.tts;

import android.util.Log;

public final class OpenSLESAudioTrack {

    private final Object mPlayStateLock = new Object();
    private TrackProgressCallback trackProgressCallback;
    private boolean isCb = false;

    public OpenSLESAudioTrack(int streamType, int sampleRate, int channel,
                              int audioFormat, TrackProgressCallback callback) {
        nativeInit(streamType, sampleRate, channel, audioFormat);
        trackProgressCallback = callback;
    }

    public void play() {
        synchronized (mPlayStateLock) {
            nativePlay();
        }
    }

    public void write(String utterId, int progress, byte[] data, int size) {
        nativeWrite(utterId, progress, data, size);
    }

    public void write(String utterId, int progress, byte[] data, int offersize, int size) {
        this.write(utterId, progress, data, size);
    }

    public void pause() {
        nativePause();
    }

    public void stop() {
        nativeStop();
    }

    public void release() {
        nativeRelease();
    }

    public void setVolume(int volume) {
        nativeSetVolume(volume);
    }

    public void getVolume() {
        nativeGetVolume();
    }

    public int getStreamType() {
        return nativeGetStreamType();
    }

    public int getSampleRate() {
        return nativeGetSampleRate();
    }

    public int getChannelCount() {
        return nativeGetChannelCount();
    }

    public int getAudioFormat() {
        return nativeGetAudioFormat();
    }

    public int getPlayState() {
        return nativeGetPlayState();
    }

    public native void nativeInit(int streamType, int sampleRate, int channel, int audioFormat);

    public native void nativePlay();

    public native void nativePause();

    public native void nativeStop();

    public native void nativeRelease();

    public native void nativeWrite(String utterId, int progress, byte[] data, int size);

    public native void nativeSetVolume(int volume);

    public native int nativeGetVolume();

    public native int nativeGetStreamType();

    public native int nativeGetSampleRate();

    public native int nativeGetChannelCount();

    public native int nativeGetAudioFormat();

    public native int nativeGetPlayState();

    public void onCompletion(String utterId, int progress) {
        Log.d("onCompletion", "utterId: " + utterId + "    -----  PROGESS: " + progress);
        if (trackProgressCallback != null) {
            trackProgressCallback.progressCallback(utterId, progress);
        }
    }

    public interface TrackProgressCallback {
        void progressCallback(String utterId, int progress);
    }

    static {
        System.loadLibrary("opensles-audio");
    }
}
