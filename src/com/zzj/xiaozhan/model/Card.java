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
	 * �����˵�ͷ��
	 */
	private String userImageUrl;
	/**
	 * ���µı���
	 */
	private String title;
	/**
	 * ���µ�����
	 */
	private String content;
	/**
	 * ��ת���ӵ�ַ
	 */
	private String webUrl;
	/**
	 * ���������
	 */
	private String more;
	
	public String getUserImageUrl() {
		return userImageUrl;
	}

	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}

	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
	}

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
