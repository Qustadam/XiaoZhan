package com.zzj.xiaozhan.model;

import java.util.List;

/**
 * ��Ƭ�����е�����
 */
public class Card {
	

	
	/**
	 * ͼƬ�ĵ�ַ
	 */
	private String imageUrl;
	/**
	 * �����˵�����
	 */
	private String name;
	/**
	 * ���µı���
	 */
	private String title;
	/**
	 * ���µ�����
	 */
	private String content;
	
	private String webUrl;

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
