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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.activity.MusicDetailActivity;
import com.zzj.xiaozhan.activity.NewComicDetailActivity;
import com.zzj.xiaozhan.activity.ShouYeDetailActivity;
import com.zzj.xiaozhan.adapter.ComonCardAdapter;
import com.zzj.xiaozhan.adapter.ComonListAdapter;
import com.zzj.xiaozhan.adapter.HotAdapter;
import com.zzj.xiaozhan.adapter.ShouYeAdapter;
import com.zzj.xiaozhan.model.Audio;
import com.zzj.xiaozhan.model.Card;
import com.zzj.xiaozhan.utils.Constants;
import com.zzj.xiaozhan.utils.LogUtil;
import com.zzj.xiaozhan.utils.NetworkUtil;
import com.zzj.xiaozhan.volley.AppStringRequest;

public class MusicFragment extends Fragment implements zrc.widget.ZrcListView.OnItemClickListener {

	// ˢ�¿ؼ�
	private SwipeRefreshLayout swipeLayout;
	private ListView listView;
	private HotAdapter adapter;
	private long startTime;
	private long endTime;
	private long costlTime;
	private Audio audio;
	private List<Audio> datas = new ArrayList<Audio>();
	private LinearLayout firstLayout;
	private LinearLayout secondLayout;
	private ScrollView myScorllView;
	private ZrcListView zrcListview;
	private ProgressBar loadinProgress;
	private RelativeLayout loadingLayout;
	private RelativeLayout errorLayout;
	private LinearLayout networkLoading;
	private int page = 1; 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.hotcomic_fragment, null, false);
		// listview����
		zrcListview = (ZrcListView) view.findViewById(R.id.hot_swipe_container);
		loadinProgress = (ProgressBar) view.findViewById(R.id.progressbar_loading);		
		loadingLayout = (RelativeLayout) view.findViewById(R.id.loading_container);
		errorLayout = (RelativeLayout) view.findViewById(R.id.network_error_container);
		networkLoading = (LinearLayout) view.findViewById(R.id.network_and_loading);
		
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

		
		adapter = new HotAdapter(getActivity(), datas);
		zrcListview.setAdapter(adapter);
		zrcListview.setDivider(null);
		zrcListview.setOnItemClickListener(this);
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
			loadDatas(Constants.DIAN_JIYI);
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
		page++;
		loadDatas(Constants.DIAN_JIYI+ "page/"+page);
	}

	/**
	 * ˢ����Ӧ
	 */
	protected void refresh() {
		// TODO Auto-generated method stub
		//�����������
		datas.clear();
		startTime = System.currentTimeMillis();
		loadDatas(Constants.DIAN_JIYI);
		costlTime = endTime - startTime;
	}
	
	
	
	
	
	
	/**
	 * �������ȡ����
	 * 
	 * @param url
	 *            ��ַ
	 */
	public void loadDatas(String url) {
		LogUtil.i("������", "ִ����loadDatas����");
		RequestQueue queue = Volley.newRequestQueue(getActivity());
		AppStringRequest request = new AppStringRequest(Request.Method.POST,
				url, new Listener<String>() {
					// ������ҳ����
					@Override
					public void onResponse(String html) {
						// TODO Auto-generated method stub
						try{
						Document doc = Jsoup.parse(html);
						// LogUtil.i("������", "html�� " + html);

						Element mainElement = doc.getElementsByClass("content")
								.get(0);

						Elements links = mainElement
								.getElementsByClass("excerpt");
						LogUtil.i("��ӡ����", "sizes:   " + links.size());

						if (links.size() > 0) {
							for (Element link : links) {
								// ����
								Element headElement = link.getElementsByTag(
										"header").get(0);
								String head = headElement.text();
								LogUtil.i("��ӡ����2", "head:   " + head);
								// ����
								Element noteElement = link.getElementsByClass(
										"note").get(0);
								String note = noteElement.text();
								LogUtil.i("��ӡ����2", "note:   " + note);
								// ͼƬ����ҳ��ַ
								Element imageElement = link.getElementsByClass(
										"focus").get(0);
								Element src = imageElement.select("img[src]")
										.get(0);
								Element href = imageElement.select("a[href]")
										.get(0);
								String imageUrl = src.attr("src");
								String webUrl = href.attr("href");
								LogUtil.i("��ӡ����2", "imageUrl:   " + imageUrl);
								LogUtil.i("��ӡ����2", "webUrl:   " + webUrl);
								// ����
								Element nameElement = link.getElementsByClass(
										"muted").get(0);
								String name = nameElement.text();
								LogUtil.i("��ӡ����2", "name:   " + name);
								// ʱ��
								Element timeElement = link.getElementsByClass(
										"muted").get(1);
								String time = timeElement.text();
								LogUtil.i("��ӡ����2", "time:   " + time);

								audio = new Audio();
								audio.setWebName(head);
								audio.setWebAuthor(name);
								audio.setWebImage(imageUrl);
								audio.setWebUrl(webUrl);
								audio.setWebContent(note);
								audio.setTime(time);
								datas.add(audio);
							}
						}

						 adapter.notifyDataSetChanged();
						// endTime = System.currentTimeMillis();
						 zrcListview.setRefreshSuccess("���سɹ�");
							// �������ظ��๦��
							zrcListview.startLoadMore();
							endTime = System.currentTimeMillis();
							networkLoading.setVisibility(View.GONE);
							loadingLayout.setVisibility(View.GONE);
							loadinProgress.setVisibility(View.GONE);
						}catch(Exception e){
							//LogUtil.i("��ҳ������3", "Exception�� " + e.getMessage());
							Toast.makeText(getActivity(), "�������쳣", Toast.LENGTH_SHORT).show();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError e) {
						// TODO Auto-generated method stub
						LogUtil.i("������", "��������");
						LogUtil.i("������", e.getMessage());
						 zrcListview.setRefreshFail("����ʧ��");
							loadingLayout.setVisibility(View.GONE);
							loadinProgress.setVisibility(View.GONE);
							enableNetworkErrorView(true);
					}
				});
		queue.add(request);

	}

	@Override
	public void onItemClick(ZrcListView parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), MusicDetailActivity.class);
		intent.putExtra("title", datas.get(position).getWebName());
		intent.putExtra("webUrl", datas.get(position).getWebUrl());
		intent.putExtra("image", datas.get(position).getWebImage());
		intent.putExtra("time", datas.get(position).getTime());
		intent.putExtra("author", datas.get(position).getWebAuthor());
		intent.putExtra("content", datas.get(position).getWebContent());
		startActivity(intent);
	}

}
