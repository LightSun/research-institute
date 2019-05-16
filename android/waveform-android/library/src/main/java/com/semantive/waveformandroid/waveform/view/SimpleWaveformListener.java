package com.semantive.waveformandroid.waveform.view;

/**
 * Created by heaven7 on 2019/5/9.
 */
public class SimpleWaveformListener implements WaveformView.WaveformListener {

    protected boolean mTouchDragging;
    protected float mTouchStart;

    protected int mTouchInitialOffset;
    protected int mOffset;
    protected int mOffsetGoal;

    protected int mFlingVelocity;
    protected long mWaveformTouchStartMsec;

    protected int mMaxPos;
    protected int mStartPos;
    protected int mEndPos;

    private final WaveformView2 mWaveformView;
    private int mWidth;

    //-----------------------------------------
    private PlayerDelegate playerDelegate;

    public SimpleWaveformListener(WaveformView2 waveformView) {
        mWaveformView = waveformView;
    }

    public void setPlayerDelegate(PlayerDelegate playerDelegate) {
        this.playerDelegate = playerDelegate;
    }

    @Override
    public void waveformTouchStart(float x) {
        mTouchDragging = true;
        mTouchStart = x;
        mTouchInitialOffset = mOffset;
        mFlingVelocity = 0;
        mWaveformTouchStartMsec = System.currentTimeMillis();
    }

    @Override
    public void waveformTouchMove(float x) {
        mOffset = trap((int) (mTouchInitialOffset + (mTouchStart - x)));
        updateDisplay();
    }

    @Override
    public void waveformTouchEnd() {
        mTouchDragging = false;
       // setOffsetGoalEnd();
    }

    @Override
    public void waveformFling(float vx) {
        mTouchDragging = false;
        mOffsetGoal = mOffset;
        mFlingVelocity = (int) (-vx);
        updateDisplay();
    }

    @Override
    public void waveformDraw() {
        mWidth = mWaveformView.getMeasuredWidth();
        if (mOffsetGoal != mOffset)
            updateDisplay();
        else if (playerDelegate != null && playerDelegate.isPlaying()) {
            updateDisplay();
        } else if (mFlingVelocity != 0) {
            updateDisplay();
        }
    }

    @Override
    public void waveformZoomIn() {
        mWaveformView.zoomIn();
        mStartPos = mWaveformView.getStart();
        mEndPos = mWaveformView.getEnd();
        mMaxPos = mWaveformView.maxPosX();
        mOffset = mWaveformView.getOffset();
        mOffsetGoal = mOffset;
        //enableZoomButtons();
        updateDisplay();
    }

    @Override
    public void waveformZoomOut() {
        mWaveformView.zoomOut();
        mStartPos = mWaveformView.getStart();
        mEndPos = mWaveformView.getEnd();
        mMaxPos = mWaveformView.maxPosX();
        mOffset = mWaveformView.getOffset();
        mOffsetGoal = mOffset;
        //enableZoomButtons();
        updateDisplay();
    }
    private int trap(int pos) {
        if (pos < 0)
            return 0;
        if (pos > mMaxPos)
            return mMaxPos;
        return pos;
    }
    protected void setOffsetGoalNoUpdate(int offset) {
        if (mTouchDragging) {
            return;
        }
        mOffsetGoal = offset;
        if (mOffsetGoal + mWidth / 2 > mMaxPos)
            mOffsetGoal = mMaxPos - mWidth / 2;
        if (mOffsetGoal < 0)
            mOffsetGoal = 0;
    }

    protected synchronized void updateDisplay() {
        if (playerDelegate != null && playerDelegate.isPlaying()) {
            int now = playerDelegate.getCurrentPosition() + playerDelegate.getPlayStartOffset();
            int frames = mWaveformView.millisecsToPixels(now);
            mWaveformView.setPlayback(frames);
            setOffsetGoalNoUpdate(frames - mWidth / 2);
            if (now >= playerDelegate.getEndPosition()) {
                handlePause();
            }
        }

        if (!mTouchDragging) {
            int offsetDelta;

            if (mFlingVelocity != 0) {
               // float saveVel = mFlingVelocity;
                offsetDelta = mFlingVelocity / 30;
                if (mFlingVelocity > 80) {
                    mFlingVelocity -= 80;
                } else if (mFlingVelocity < -80) {
                    mFlingVelocity += 80;
                } else {
                    mFlingVelocity = 0;
                }

                mOffset += offsetDelta;

                if (mOffset + mWidth / 2 > mMaxPos) {
                    mOffset = mMaxPos - mWidth / 2;
                    mFlingVelocity = 0;
                }
                if (mOffset < 0) {
                    mOffset = 0;
                    mFlingVelocity = 0;
                }
                mOffsetGoal = mOffset;
            } else {
                offsetDelta = mOffsetGoal - mOffset;

                if (offsetDelta > 10)
                    offsetDelta = offsetDelta / 10;
                else if (offsetDelta > 0)
                    offsetDelta = 1;
                else if (offsetDelta < -10)
                    offsetDelta = offsetDelta / 10;
                else if (offsetDelta < 0)
                    offsetDelta = -1;
                else
                    offsetDelta = 0;

                mOffset += offsetDelta;
            }
        }

        mWaveformView.setParameters(mStartPos, mEndPos, mOffset);
        mWaveformView.invalidate();
    }

    private void handlePause() {
        if(playerDelegate != null && playerDelegate.isPlaying()){
            playerDelegate.pause();
        }
        mWaveformView.setPlayback(-1);
    }
    protected void setOffsetGoalEnd() {
        setOffsetGoal(mEndPos - mWidth / 2);
    }
    protected void setOffsetGoal(int offset) {
        setOffsetGoalNoUpdate(offset);
        updateDisplay();
    }

}
