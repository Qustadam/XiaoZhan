package com.zzj.xiaozhan.fragment;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;
import zrc.widget.ZrcListView.OnStartListener;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.zzj.xiaozhan.utils.NetworkUtil;
import com.zzj.xiaozhan.volley.AppStringRequest;

public class NewComicFragment extends Fragment implements OnStartListener,
		zrc.widget.ZrcListView.OnItemClickListener {

	// ˢ�¿ؼ�
	private ZrcListView zrcListview;
	private List<Card> datas = new ArrayList<Card>();
	private NewComicListAdapter adapter;
	private Card card;
	private long startTime;
	private long endTime;
	private long costlTime;
	private ProgressBar loadinProgress;
	private RelativeLayout loadingLayout;
	private RelativeLayout errorLayout;
	private LinearLayout networkLoading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.newcomic_fragment, null, false);
		// listview����
		zrcListview = (ZrcListView) view.findViewById(R.id.new_swipe_container);
		loadinProgress = (ProgressBar) view.findViewById(R.id.progressbar_loading);		
		loadingLayout = (RelativeLayout) view.findViewById(R.id.loading_container);
		errorLayout = (RelativeLayout) view.findViewById(R.id.network_error_container);
		networkLoading = (LinearLayout) view.findViewById(R.id.network_and_loading);
		
		adapter = new NewComicListAdapter(getActivity(), datas);

		// ��������ˢ�µ���ʽ
		SimpleHeader header = new SimpleHeader(getActivity());
		header.setTextColor(0xff0066aa);
		header.setCircleColor(0xff33bbee);
		zrcListview.setHeadable(header);
		// ���ü��ظ������ʽ
		SimpleFooter footer = new SimpleFooter(getActivity());
		footer.setCircleColor(0xff33bbee);
		zrcListview.setFootable(footer);

		// �����б�����ֶ���
		zrcListview.setItemAnimForTopIn(R.anim.topitem_in);
		zrcListview.setItemAnimForBottomIn(R.anim.bottomitem_in);
		// ����ˢ���¼��ص�
		zrcListview.setOnRefreshStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				refresh();
			}
		});

		// ���ظ����¼��ص�
		zrcListview.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				loadMore();
			}
		});

		zrcListview.setAdapter(adapter);
		zrcListview.setDivider(null);
		zrcListview.setOnItemClickListener(this);
		 //�ж�����״̬
		 networkStates();

		return view;
	}
	/**
	 * �ж�����״̬
	 */
	private void networkStates() {
		// TODO Auto-generated method stub
		if(NetworkUtil.isConnected(getActivity())){
			//��������
			loadingLayout.setVisibility(View.VISIBLE);
			loadinProgress.setVisibility(View.VISIBLE);
			loadDatas(Constants.NEW_COMIC_WEB);
			enableNetworkErrorView(false);
		}else{
			loadingLayout.setVisibility(View.GONE);
			loadinProgress.setVisibility(View.GONE);
			//����������ͼ
			enableNetworkErrorView(true);
		}
	}
	
	
	private void enableNetworkErrorView(boolean networkAvailable) {
		// TODO Auto-generated method stub
		if(networkAvailable){
			errorLayout.setVisibility(View.VISIBLE);
			Button tryButton = (Button) errorLayout
					.findViewById(R.id.try_button);
			tryButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					refresh();
				}
			});
		}else{
			errorLayout.setVisibility(View.GONE);
		}
		
	}
	
	
	
	protected void loadMore() {
		// TODO Auto-generated method stub

	}

	/*
	 * @Override public void onRefresh() { // TODO Auto-generated method stub
	 * startTime = System.currentTimeMillis();
	 * loadDatas(Constants.NEW_COMIC_WEB); costlTime = endTime - startTime; if
	 * (costlTime < 1500) { costlTime = 1500; } new Handler().postDelayed(new
	 * Runnable() {
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub
	 * swipeLayout.setRefreshing(false);
	 * 
	 * } }, costlTime); }
	 */

	protected void refresh() {
		// TODO Auto-generated method stub
		startTime = System.currentTimeMillis();
		loadDatas(Constants.NEW_COMIC_WEB);
		costlTime = endTime - startTime;
		if (costlTime < 1500) {
			costlTime = 1500;
		}
		/*new Handler().postDelayed(new Runnable() {

			@Override
			public void run() { // TODO Auto-generated method stub
				adapter.notifyDataSetChanged();
				zrcListview.setRefreshSuccess("���سɹ�");
				// �������ظ��๦��
				zrcListview.startLoadMore();

			}
		}, costlTime);*/
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
							zrcListview.setRefreshSuccess("���سɹ�");
							// �������ظ��๦��
							zrcListview.startLoadMore();
							endTime = System.currentTimeMillis();
							networkLoading.setVisibility(View.GONE);
							loadingLayout.setVisibility(View.GONE);
							loadinProgress.setVisibility(View.GONE);
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError e) {
						// TODO Auto-generated method stub
						LogUtil.i("������", "��������");
						LogUtil.i("������", e.getMessage());
						zrcListview.setRefreshFail("����ʧ��");
						enableNetworkErrorView(true);
					}
				});
		queue.add(request);

	}

	/**
	 * listView����¼�
	 */

	@Override
	public void onItemClick(ZrcListView parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), NewComicDetailActivity.class);
		intent.putExtra("title", datas.get(position).getTitle());
		intent.putExtra("webUrl", datas.get(position).getWebUrl());
		intent.putExtra("time", datas.get(position).getMore());
		startActivity(intent);

	}

}
