package com.zzj.xiaozhan.model;

/**
 * ������
 * 
 * @author Tomi_Enn
 * 
 */
public class Video {
	
	
	/**
	 * �������
	 */
	public String number;
	/**
	 * ��������
	 */
	public String title;
	/**
	 * ����ʱ��
	 */
	public String time;
	/**
	 * ���������ַ
	 */
	public String videoUrl;
	/**
	 * �����󲥷ŵ�ַ
	 */
	public String playVideoUrl;
	/**
	 * �ļ���С
	 */
	private String size;
	
	public String getPlayVideoUrl() {
		return playVideoUrl;
	}
	public void setPlayVideoUrl(String playVideoUrl) {
		this.playVideoUrl = playVideoUrl;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
