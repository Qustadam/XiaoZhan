package com.zzj.xiaozhan.model;

/**
 * ��Ƶ��
 * 
 * @author Tomi_Enn
 * 
 */
public class Audio {
	/**
	 * ��Ƶ���
	 */
	public String number;
	/**
	 * ��Ƶ����
	 */
	public String audioTitle;
	/**
	 * ����ʱ��
	 */
	public String time;
	/**
	 * ��Ƶ�����ַ
	 */
	public String audioUrl;
	/**
	 * ��ҳ��ַ
	 */
	public String webUrl;
	/**
	 * ��ҳ����
	 */
	public String webName;
	/**
	 * ��ҳ����
	 */
	public String webAuthor;
	public String getWebAuthor() {
		return webAuthor;
	}

	public void setWebAuthor(String webAuthor) {
		this.webAuthor = webAuthor;
	}

	/**
	 * ��ҳ����
	 */
	public String webContent;
	/**
	 * ��ҳͼƬ
	 */
	public String webImage;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getAudioTitle() {
		return audioTitle;
	}

	public void setAudioTitle(String audioTitle) {
		this.audioTitle = audioTitle;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAudioUrl() {
		return audioUrl;
	}

	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getWebName() {
		return webName;
	}

	public void setWebName(String webName) {
		this.webName = webName;
	}

	public String getWebContent() {
		return webContent;
	}

	public void setWebContent(String webContent) {
		this.webContent = webContent;
	}

	public String getWebImage() {
		return webImage;
	}

	public void setWebImage(String webImage) {
		this.webImage = webImage;
	}

	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
	}

	/**
	 * ����
	 */
	public String more;
}
