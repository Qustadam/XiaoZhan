package com.zzj.xiaozhan.fragment;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.activity.MainActivity;
import com.zzj.xiaozhan.activity.ShouYeDetailActivity;
import com.zzj.xiaozhan.activity.UserInformation;
import com.zzj.xiaozhan.adapter.ComonCardAdapter;
import com.zzj.xiaozhan.model.Card;
import com.zzj.xiaozhan.utils.LogUtil;
import com.zzj.xiaozhan.volley.AppStringRequest;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Paint.Join;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ShouYeFragment extends Fragment implements OnRefreshListener,
		OnItemClickListener {
	// ˢ�¿ؼ�
	private SwipeRefreshLayout swipeLayout;
	private List<Card> datas = new ArrayList<Card>();
	private ListView shouListView;
	private ComonCardAdapter adapter;
	private Card card;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.shouye_fragment, null, false);
		// listview����
		shouListView = (ListView) view.findViewById(R.id.shou_listview_content);
		adapter = new ComonCardAdapter(getActivity(), datas);
		shouListView.setAdapter(adapter);
		shouListView.setDivider(null);
		shouListView.setOnItemClickListener(this);
		// ����ˢ��
		swipeLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.shou_swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		swipeLayout.setOnRefreshListener(this);
		// ��ȡ����
		getDatas();
	//	loadDatas("http://www.xiaozhan.cc/");
		return view;
	}

	private void getDatas() {
		// TODO Auto-generated method stub
		String[] name = { "Tomi_Enn", "Sam" };
		String[] title = { "����ʳʬ��", "������" };
		String[] content = {
				"�ڷ������ӵ��ִ������С�������������һ����ʳ��ʬ�Ĺ��ˣ����ǳ�֮Ϊʳʬ����һ�գ���ľ��������ĳλ����Ů�ӣ�����������һ����֮�¹ʡ��Դˣ��������۵����˳��ֿ�ʼת���ˡ���",
				"�����ڶ���ĳС��ׯ��·���ܵ��������˹�ľ���ָ����������Ϊһ����ɫ�ĺ�����Ϊ�˴�����Ŀ�꣬���ҵ�������Ŀ��One Piece��·��̤�ϼ����ó̡�һ·��������������ĥ�ѣ�Ҳ��ʶ����¡�������������ա��㼪���ޱ���һ���Ը����ĺ��ѡ�����Я��һͬչ����������ɫ�ʵĴ�ð�ա�" };
		for (int i = 0; i < name.length; i++) {
			card = new Card();
			card.setName(name[i]);
			card.setTitle(title[i]);
			card.setContent(content[i]);
			datas.add(card);
		}
		adapter.notifyDataSetChanged();
	}

	public void loadDatas(String url) {
		LogUtil.i("������", "ִ����loadDatas����");
		RequestQueue queue = Volley.newRequestQueue(getActivity());
		AppStringRequest request = new AppStringRequest(Request.Method.POST,
				url, new Listener<String>() {
					// ������ҳ����
					@Override
					public void onResponse(String html) {
						// TODO Auto-generated method stub
						Document doc = Jsoup.parse(html);
						Element content = doc.getElementsByClass("left").get(0);
						Elements links = content.getElementsByClass("item");
						if (links.size() > 0) {
							for (Element link : links) {
								// ����html�Ľڵ�һ����Ѱ����Ҫ������
								Element elementBg = link.getElementsByClass(
										"bd").get(0);
								Element elementContents = elementBg
										.getElementsByClass("content").get(0);
								Element elementTitle = elementContents
										.getElementsByClass("title").get(0);
								Element elementUrl = elementTitle.select(
										"a[href]").get(0);
								// �����ַ
								String url = elementUrl.attr("href");
								// ���ݵı���
								String title = elementUrl.text();
								// ��������
								Elements elementSpan = elementContents
										.getElementsByTag("span");
								String detail = elementSpan.text();
								// ��ȡͼ���ַ
								Element elementIcon = link.getElementsByClass(
										"avatar").get(0);
								Element elementImage = elementIcon.select(
										"img[src]").get(0);
								String imageUrl = elementImage.attr("src");
								LogUtil.i("������", "��ַ�� " + url + " ���⣺ " + title
										+ " ͼƬ��ַ�� " + imageUrl);
								LogUtil.i("������", "���ģ� " + detail);
								
								//�����ݴ洢��datas���ˢ��adapter
								card = new Card();
								card.setTitle(title);
								card.setImageUrl(imageUrl);
								card.setContent(detail);
								card.setWebUrl(url);
								datas.add(card);

							}
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						LogUtil.i("������", "��������");
					}
				});
		queue.add(request);

	}

	/**
	 * ˢ����Ӧ
	 */
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				swipeLayout.setRefreshing(false);

			}
		}, 3000);
	}

	/**
	 * listView����¼�
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		Intent intent = new Intent(ShouYeFragment.this.getActivity(),
				ShouYeDetailActivity.class);
		startActivity(intent);

	}
}
