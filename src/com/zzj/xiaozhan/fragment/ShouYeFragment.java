package com.zzj.xiaozhan.fragment;

import java.util.ArrayList;
import java.util.List;

import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.activity.MainActivity;
import com.zzj.xiaozhan.activity.ShouYeDetailActivity;
import com.zzj.xiaozhan.activity.UserInformation;
import com.zzj.xiaozhan.adapter.ComonCardAdapter;
import com.zzj.xiaozhan.model.Card;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ShouYeFragment extends Fragment implements OnRefreshListener, OnItemClickListener {

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
		shouListView = (ListView) view.findViewById(R.id.shou_listview_content);
		adapter = new ComonCardAdapter(getActivity(), datas);
		shouListView.setAdapter(adapter);
		shouListView.setDivider(null);
		shouListView.setOnItemClickListener(this);
		swipeLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.shou_swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		swipeLayout.setOnRefreshListener(this);
		getDatas();
		return view;
	}

	private void getDatas() {
		// TODO Auto-generated method stub
		String[] name = {"Tomi_Enn", "Sam"};
		String[] title = {"����ʳʬ��","������"};
		String[] content = {"�ڷ������ӵ��ִ������С�������������һ����ʳ��ʬ�Ĺ��ˣ����ǳ�֮Ϊʳʬ����һ�գ���ľ��������ĳλ����Ů�ӣ�����������һ����֮�¹ʡ��Դˣ��������۵����˳��ֿ�ʼת���ˡ���",
			"�����ڶ���ĳС��ׯ��·���ܵ��������˹�ľ���ָ����������Ϊһ����ɫ�ĺ�����Ϊ�˴�����Ŀ�꣬���ҵ�������Ŀ��One Piece��·��̤�ϼ����ó̡�һ·��������������ĥ�ѣ�Ҳ��ʶ����¡�������������ա��㼪���ޱ���һ���Ը����ĺ��ѡ�����Я��һͬչ����������ɫ�ʵĴ�ð�ա�"
		};
		for(int i=0; i < name.length; i++){
			card = new Card();
			card.setName(name[i]);
			card.setTitle(title[i]);
			card.setContent(content[i]);
			datas.add(card);
		}
		adapter.notifyDataSetChanged();
	}

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

	public void showContent(View view) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		Intent intent = new Intent(ShouYeFragment.this.getActivity(), ShouYeDetailActivity.class);
		startActivity(intent);
		
	}
}
