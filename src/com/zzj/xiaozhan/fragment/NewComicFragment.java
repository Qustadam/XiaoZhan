package com.zzj.xiaozhan.fragment;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.activity.HotComicDetailActivity;
import com.zzj.xiaozhan.activity.NewComicDetailActivity;
import com.zzj.xiaozhan.activity.ShouYeDetailActivity;
import com.zzj.xiaozhan.adapter.ComonCardAdapter;
import com.zzj.xiaozhan.adapter.ComonListAdapter;
import com.zzj.xiaozhan.adapter.NewComicListAdapter;
import com.zzj.xiaozhan.model.Card;
import com.zzj.xiaozhan.utils.Constants;
import com.zzj.xiaozhan.utils.LogUtil;
import com.zzj.xiaozhan.volley.AppStringRequest;

public class NewComicFragment extends Fragment implements OnRefreshListener,
		OnItemClickListener {

	// ˢ�¿ؼ�
	private SwipeRefreshLayout swipeLayout;
	private List<Card> datas = new ArrayList<Card>();
	private ListView listView;
	private NewComicListAdapter adapter;
	private Card card;
	private long startTime;
	private long endTime;
	private long costlTime;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.newcomic_fragment, null, false);
		// listview����
		listView = (ListView) view.findViewById(R.id.new_listview);
		adapter = new NewComicListAdapter(getActivity(), datas);
		listView.setAdapter(adapter);
		listView.setDivider(null);
		listView.setOnItemClickListener(this);
		// ����ˢ��
		swipeLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.new_swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		swipeLayout.setOnRefreshListener(this);
		// ��ȡ����
		// getDatas();
		loadDatas(Constants.NEW_COMIC_WEB);
		return view;
	}

	/**
	 * �������ȡ����
	 * 
	 * @param url
	 *            ��ַ
	 */
	public void loadDatas(String url) {
		LogUtil.i("������", "��ǰҳ�棺 " + getActivity() + " ִ����loadDatas����");
		RequestQueue queue = Volley.newRequestQueue(getActivity());
		AppStringRequest request = new AppStringRequest(Request.Method.POST,
				url, new Listener<String>() {
					// ������ҳ����
					@Override
					public void onResponse(String html) {
						// TODO Auto-generated method stub
						Document doc = Jsoup.parse(html);
						Element elementMain = doc.getElementsByClass("main")
								.get(0);
						Element elementLeft = elementMain.getElementsByClass(
								"left").get(0);
						Elements items = elementMain.getElementsByClass("item");
						if (items.size() > 0) {
							for (Element item : items) {
								// ����html�Ľڵ�һ����Ѱ����Ҫ������
								Element elementContent = item
										.getElementsByClass("content").get(0);
								Element elementTitle = item.getElementsByClass(
										"title").get(0);
								// �����ַ
								Element elementWebUrl = elementTitle.select(
										"a[href]").get(0);
								String url = elementWebUrl.attr("href");
								// ���ݵı���
								String title = elementWebUrl.text();
								// ��������
								Elements elementSpan = elementTitle
										.getElementsByTag("span");
								String time = elementSpan.text();
								// ���ݵķ����ַ
								Element elementCover = item.getElementsByClass(
										"cover").get(0);
								Element elementImage = elementCover.select(
										"img[src]").get(0);
								String imageUrl = elementImage.attr("src");

								LogUtil.i("������", "��ַ�� " + url);
								LogUtil.i("������", " ���⣺ " + title);
								LogUtil.i("������", " �����ַ�� " + imageUrl);
								LogUtil.i("������", " ���ڣ� " + time);
								
								// �����ݴ洢��datas���ˢ��adapter
								card = new Card();
								card.setTitle(title);
								card.setImageUrl(imageUrl);
								card.setMore(time);
								card.setWebUrl(url);
								datas.add(card);
								
							}
							adapter.notifyDataSetChanged();
							endTime = System.currentTimeMillis();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError e) {
						// TODO Auto-generated method stub
						LogUtil.i("������", "��������");
						LogUtil.i("������", e.getMessage());
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
		startTime = System.currentTimeMillis();
		loadDatas(Constants.NEW_COMIC_WEB);
		costlTime = endTime - startTime;
		if (costlTime < 1500) {
			costlTime = 1500;
		}
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				swipeLayout.setRefreshing(false);

			}
		}, costlTime);
	}

	/**
	 * listView����¼�
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		// HotComicDetailActivity.actionStart(this,
		// datas.get(position).getTitle(), datas.get(position).getWebUrl());

		Intent intent = new Intent(getActivity(), NewComicDetailActivity.class);
		intent.putExtra("title", datas.get(position).getTitle());
		intent.putExtra("webUrl", datas.get(position).getWebUrl());
		intent.putExtra("time", datas.get(position).getMore());
		startActivity(intent);

	}
	
	
}