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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.adapter.ComonCardAdapter;
import com.zzj.xiaozhan.adapter.ShouYeAdapter;
import com.zzj.xiaozhan.model.Card;
import com.zzj.xiaozhan.utils.Constants;
import com.zzj.xiaozhan.utils.LogUtil;
import com.zzj.xiaozhan.utils.NetworkUtil;
import com.zzj.xiaozhan.volley.AppStringRequest;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint.Join;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ShouYeFragment extends Fragment implements
		zrc.widget.ZrcListView.OnItemClickListener {
	// ˢ�¿ؼ�
	private ZrcListView zrcListview;
	private List<Card> datas = new ArrayList<Card>();
	private ShouYeAdapter adapter;
	private Card card;
	private long startTime;
	private long endTime;
	private long costlTime;
	private ProgressBar loadinProgress;
	private RelativeLayout loadingLayout;
	private RelativeLayout errorLayout;
	private LinearLayout networkLoading;
	private int page = 1; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.shouye_fragment, null, false);
		// zrcListview����
		zrcListview = (ZrcListView) view
				.findViewById(R.id.shouye_swipe_listview);
		loadinProgress = (ProgressBar) view.findViewById(R.id.progressbar_loading);		
		loadingLayout = (RelativeLayout) view.findViewById(R.id.loading_container);
		errorLayout = (RelativeLayout) view.findViewById(R.id.network_error_container);
		networkLoading = (LinearLayout) view.findViewById(R.id.network_and_loading);
		adapter = new ShouYeAdapter(getActivity(), datas);

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
			loadDatas(Constants.I_TOU_XIAN);
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
		loadDatas(Constants.I_TOU_XIAN + "?page="+page);
	}

	/**
	 * ˢ����Ӧ
	 */
	protected void refresh() {
		// TODO Auto-generated method stub
		//�����������
		datas.clear();
		startTime = System.currentTimeMillis();
		loadDatas(Constants.I_TOU_XIAN);
		costlTime = endTime - startTime;
		if (costlTime < 1500) {
			costlTime = 1500;
		}
		/*new Handler().postDelayed(new Runnable() {

			@Override
			public void run() { // TODO Auto-generated method stub
				

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
						Element content = doc.getElementsByClass("content")
								.get(0);
						Elements articles = content
								.getElementsByClass("content-main-box");
						if (articles.size() > 0) {
							for (Element article : articles) {

								if (article.getElementsByClass("details")
										.size() > 0) {
									Element elementContent = article
											.getElementsByClass("details").get(
													0);
									// ��������
									String text = elementContent.text();
									// ����ͼƬ
									String tontentImage;
									if (elementContent.select("img[src]")
											.size() > 0) {
										tontentImage = elementContent
												.select("img[src]").get(0)
												.attr("src");
									} else {
										tontentImage = "nodata";
									}
									LogUtil.i("��ҳ������3", "content�� " + text);
									LogUtil.i("��ҳ������3", "tontentImage�� "
											+ tontentImage);
									// �û�ͷ���ַ
									Element elementAvatar = article
											.getElementsByClass("avatar")
											.get(0);
									Element elementImage = elementAvatar
											.select("img[src]").get(0);
									String imageUrl = elementImage.attr("src");
									LogUtil.i("��ҳ������3", "imageUrl�� " + imageUrl);
									// �û���
									Element elementTitle = article
											.getElementsByClass("title").get(0);
									Element elementA = elementTitle
											.getElementsByTag("a").get(2);
									String name = elementA.text();
									// ʱ��(��Ҫ�ٴ�ת��ȥ������)
									String time = elementTitle.text();
									
									// ����
									String title;
									if (elementTitle.getElementsByTag("h1")
											.size() > 0) {
										Element elementH = elementTitle
												.getElementsByTag("h1").get(0);
										Element elementA1 = elementH
												.getElementsByTag("a").get(0);
										title = elementA1.text();
									} else {
										title = "nodata";
									}

									LogUtil.i("��ҳ������3", "name�� " + name);
									LogUtil.i("��ҳ������3", "time�� " + time);
									LogUtil.i("��ҳ������3", "title�� " + title);
									// �����ݴ洢��datas���ˢ��adapter
									card = new Card();
									card.setName(name);
									card.setTitle(title);
									card.setContent(text);
									card.setImageUrl(tontentImage);
									card.setUserImageUrl(imageUrl);
									card.setMore(time);
									datas.add(card);
									

								} else {
									continue;
								}
								
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
						}catch(Exception e){
							LogUtil.i("��ҳ������3", "Exception�� " + e.getMessage());
							Toast.makeText(getActivity(), "�������쳣", Toast.LENGTH_SHORT).show();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError e) {
						// TODO Auto-generated method stub
						LogUtil.i("��ҳ������", "��������");
						//LogUtil.i("��ҳ������", e.getMessage());
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

	}

	/**
	 * listView����¼�
	 */
	/*
	 * @Override public void onItemClick(AdapterView<?> parent, View view, int
	 * position, long id) { // TODO Auto-generated method stub
	 * 
	 * Intent intent = new Intent(ShouYeFragment.this.getActivity(),
	 * ShouYeDetailActivity.class); startActivity(intent);
	 * 
	 * }
	 */
	

	
	
}
