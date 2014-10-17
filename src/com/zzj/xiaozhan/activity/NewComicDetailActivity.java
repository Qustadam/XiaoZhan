package com.zzj.xiaozhan.activity;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.adapter.NewComicListAdapter;
import com.zzj.xiaozhan.adapter.VideoListAdapter;
import com.zzj.xiaozhan.model.Card;
import com.zzj.xiaozhan.model.Video;
import com.zzj.xiaozhan.utils.LogUtil;
import com.zzj.xiaozhan.utils.NoScrollListView;
import com.zzj.xiaozhan.volley.AppStringRequest;
import com.zzj.xiaozhan.volley.LruBitmapCache;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class NewComicDetailActivity extends Activity {

	private TextView newComicTitle;
	private TextView newComicTag;
	private TextView newComicTime;
	private TextView newComicLocal;
	private TextView newComicfocus;
	private TextView newComicContent;
	private TextView newComicComments;
	private TextView newComicUp;
	public String imageUrl;
	public String local;
	public String tag;
	public String focus;
	public String content;
	public List<Video> datas = new ArrayList<Video>();
	private NoScrollListView listView;
	private VideoListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newcomic_detail_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		init();
		// ��ȡ�����ݹ���������
		String title = getIntent().getStringExtra("title");
		String webUrl = getIntent().getStringExtra("webUrl");
		String time = getIntent().getStringExtra("time");
		newComicTitle.setText(title);
		newComicTime.setText("��  ����"+time);
		loadDatas(webUrl);
		//�����ݷŵ�listview��	
		adapter = new VideoListAdapter(getApplicationContext(), datas);
		listView.setAdapter(adapter);
	}

	private void init() {
		// TODO Auto-generated method stub
		newComicTitle = (TextView) findViewById(R.id.newcomic_title);
		newComicTag = (TextView) findViewById(R.id.newcomic_tag);
		newComicTime = (TextView) findViewById(R.id.newcomic_time);
		newComicLocal = (TextView) findViewById(R.id.newcomic_local);
		newComicfocus = (TextView) findViewById(R.id.newcomic_focus);
		newComicContent = (TextView) findViewById(R.id.newcomic_content);
		newComicComments = (TextView) findViewById(R.id.newcomic_comments);
		newComicUp = (TextView) findViewById(R.id.newcomic_up);
		listView = (NoScrollListView) findViewById(R.id.newcomic_detail_listview);
	}

	/**
	 * �������ȡ����
	 * 
	 * @param url
	 *            ��ַ
	 */
	public void loadDatas(String url) {
		LogUtil.i("������", "��ǰҳ�棺 " + this + " ִ����loadDatas����");
		RequestQueue queue = Volley.newRequestQueue(this);
		AppStringRequest request = new AppStringRequest(Request.Method.POST,
				url, new Listener<String>() {
					// ������ҳ����
					@Override
					public void onResponse(String html) {
						// TODO Auto-generated method stub
						try {
							
						
						Document doc = Jsoup.parse(html);
						Element elementMain = doc.getElementsByClass("main")
								.get(0);
						Element elementLeft = elementMain.getElementsByClass(
								"left").get(0);
						Element elementInfobox = elementLeft
								.getElementsByClass("infobox").get(0);
						// ��ȡ����imageUrl
						Element elementPoster = elementInfobox
								.getElementsByClass("poster-sec").get(0);
						Element elementImage = elementPoster.select("img[src]")
								.get(0);
						imageUrl = elementImage.attr("src");
						// �������ڲ��ҽڵ�
						Element elementInfo = elementInfobox
								.getElementsByClass("info-sec").get(0);
						Element ElementDesc = elementInfo.getElementsByClass(
								"desc-wrapper").get(0);
						Elements rows = ElementDesc.getElementsByClass("row");

						// ��ȡ��ַ
						Element span0 = rows.get(0);
						local = span0.text();

						// ��ȡ��ǩ
						tag = rows.get(2).text();

						// ��ȡ����
						Element span3 = rows.get(3);
						focus = span3.text();

						// ��ȡ���
						Element span4 = rows.get(4).getElementsByClass("intro")
								.get(0);
						content = span4.text();

						LogUtil.i("������", "ͼƬ��ַ�� " + imageUrl);
						LogUtil.i("������", " ��ַ�� " + local);
						LogUtil.i("������", " ��ǩ�� " + tag);
						LogUtil.i("������", " ���㣺 " + focus);
						LogUtil.i("������", " ��飺 " + content);

						// ����������Ƶ�б�
						Element elementTab = elementLeft.getElementsByClass(
								"tab").get(0);
						Element elementMain0 = elementTab.getElementsByClass(
								"main0").get(0);
						Element elementBlock = elementMain0.getElementsByClass(
								"block").get(0);
						Element elementOlt = elementBlock.getElementsByClass(
								"olt").get(0);
						Element tbody = elementOlt.getElementsByTag("tbody")
								.get(0);
						// ����������Ƶ�б�
						Elements trs = tbody.getElementsByTag("tr");
						if (trs.size() > 0) {
							
							for (Element tr : trs) {
								String number = tr.getElementsByTag("td")
										.get(0).text();
								String videoName = tr.getElementsByTag("td")
										.get(1).text();
								String videoDate = tr.getElementsByTag("td")
										.get(2).text();
								LogUtil.i("������", " number�� " + number
										+ " videoName�� " + videoName
										+ " videoDate�� " + videoDate);
								Video video = new Video();
								video.setNumber(number);
								video.setTitle(videoName);
								video.setTime(videoDate);
								datas.add(video);
							}
							adapter.notifyDataSetChanged();
						}

						// �ص����̸߳���UI
						runOnUiThread(new Runnable() {
							public void run() {

								// ����UI
								newComicTag.setText(tag);
								newComicLocal.setText(local);
								newComicfocus.setText(focus);
								newComicContent.setText("��  �飺"+content);
								if(!datas.isEmpty()){
									for (Video data : datas) {
										String videoNumber = data.getNumber();
										String videoTitle = data.getTitle();
										String videoTime = data.getTime();
										LogUtil.i("������", " ���̣߳� " + videoNumber+videoTitle+videoTime);
									}
								
									
								}
							}
						});
						} catch (Exception e) {
							// TODO: handle exception
							LogUtil.i("�����쳣", e.getMessage());
						}
						
					}
						
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						LogUtil.i("������", "��������");
						LogUtil.i("������", error.getMessage());
						if (error.networkResponse == null) {
							LogUtil.i("�������","���ӳ�ʱ");
						}else{ 
							LogUtil.i("�������","��ַ����ȷ����������");
						if(error.networkResponse.statusCode == 404){
							LogUtil.i("�������","��ַ������");
						}
						if(error.networkResponse.statusCode >= 500){
							LogUtil.i("�������","��������������");
						}
						}
						
					}
				});
		queue.add(request);

	}

}
