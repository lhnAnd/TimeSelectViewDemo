package com.example.downloaddemo.callback;

public interface OnControllListener {
	//isClips 1 表示确定是  -1表示确定不是  0表示不确定
	void onPlayVideo(long startTime, long lastTime, long nextTime, boolean byHand, int isClips, long MsgCreateTime);


	void onStopVideo(long startTime, long nextTime, boolean byHand);

	void onOutOfLimit();
}
