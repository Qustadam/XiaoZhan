package com.zzj.xiaozhan.activity;

import com.zzj.xiaozhan.R;
import com.zzj.xiaozhan.R.array;
import com.zzj.xiaozhan.R.drawable;
import com.zzj.xiaozhan.R.id;
import com.zzj.xiaozhan.R.layout;
import com.zzj.xiaozhan.R.menu;
import com.zzj.xiaozhan.R.string;
import com.zzj.xiaozhan.adapter.DrawerListAdapter;
import com.zzj.xiaozhan.fragment.ComicKuaiDiFragment;
import com.zzj.xiaozhan.fragment.HotComicFragment;
import com.zzj.xiaozhan.fragment.MyFavorFragment;
import com.zzj.xiaozhan.fragment.SettingFragment;
import com.zzj.xiaozhan.fragment.ShouYeFragment;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends Activity {

	private String[] mFragmentTitles;
	private DrawerLayout mDrawerLayout;
	private LinearLayout drawerView;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private DrawerListAdapter drawerListAdapter;
	private HotComicFragment hotComicFragment;
	private ComicKuaiDiFragment comicKuaiDiFragment;
	private MyFavorFragment myFavorFragment;
	private SettingFragment settingFragment;
	private ShouYeFragment shouYeFragment;
	/**
	 * ���˹���
	 */
	private ImageView loginImage;
	/**
	 * ���ڶ�Fragment���й���
	 */
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ���ó��뵼��
		mTitle = mDrawerTitle = getTitle();
		mFragmentTitles = getResources().getStringArray(R.array.drawer_item);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerView = (LinearLayout) findViewById(R.id.drawer_view);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		loginImage = (ImageView) findViewById(R.id.user_login);
		//�����������
		fragmentManager = getFragmentManager();
		// Ϊlist view����adapter
		drawerListAdapter = new DrawerListAdapter(this);
		mDrawerList.setAdapter(drawerListAdapter);
		// Ϊlist����click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		// ʵ��drawer�Ĵ򿪹رչ���
		mDrawerToggle = new ActionBarDrawerToggle(this, /* ���� Activity */
		mDrawerLayout, /* DrawerLayout ���� */
		R.drawable.ic_drawer, /* ͼ�������滻'Up'���� */
		R.string.drawer_open, /* "�� drawer" ���� */
		R.string.drawer_close) { /* "�ر� drawer" ���� */

			/** ��drawer������ȫ�رյ�״̬ʱ���� */
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // ������onPrepareOptionsMenu()�ĵ���
			}

			/** ��drawer������ȫ�򿪵�״̬ʱ���� */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // ������onPrepareOptionsMenu()�ĵ���
			}

		};
		// ����drawer������ΪDrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		//����Ĭ������ҳ
		mDrawerList.setItemChecked(0, true);
		setFragmentSelection(0);
		setTitle("��ҳ");
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// ��onRestoreInstanceState������ͬ��������״̬.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// ���¼����ݸ�ActionBarDrawerToggle, �������true����ʾapp ͼ�����¼��Ѿ�������
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// �����������action bar items...

		return super.onOptionsItemSelected(item);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			setFragmentSelection(position);
		}
	}

	/**
	 * ���ݴ����index����������ѡ�е�fragmentҳ��
	 * 
	 * @param index
	 *            ÿ��fragmentҳ��Ӧ���±ꡣ
	 */
	public void setFragmentSelection(int position) {
		// TODO Auto-generated method stub
		// ����һ��Fragment����
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// �����ص����е�Fragment���Է�ֹ�ж��Fragment��ʾ�ڽ����ϵ����
		hideFragments(transaction);
		switch (position) {
		case 0:
			if (shouYeFragment == null) {

				shouYeFragment = new ShouYeFragment();
				transaction.add(R.id.content_frame, shouYeFragment);
			} else {

				transaction.show(shouYeFragment);
			}
			break;
		case 1:
			if (hotComicFragment == null) {
				hotComicFragment = new HotComicFragment();
				transaction.add(R.id.content_frame, hotComicFragment);
			} else {
				transaction.show(hotComicFragment);
			}
			break;
		case 2:
			if (comicKuaiDiFragment == null) {
				comicKuaiDiFragment = new ComicKuaiDiFragment();
				transaction.add(R.id.content_frame, comicKuaiDiFragment);
			} else {
				transaction.show(comicKuaiDiFragment);
			}
			break;
		case 3:
			if (myFavorFragment == null) {
				myFavorFragment = new MyFavorFragment();
				transaction.add(R.id.content_frame, myFavorFragment);
			} else {
				transaction.show(myFavorFragment);
			}
			break;
		default:
			if (settingFragment == null) {
				settingFragment = new SettingFragment();
				transaction.add(R.id.content_frame, settingFragment);
			} else {
				transaction.show(settingFragment);
			}
			break;
		}
		transaction.commit();

		// ������ѡ���item, ���±���, ���ر�drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mFragmentTitles[position]);
		mDrawerLayout.closeDrawer(drawerView);

	}

	/**
	 * �����е�Fragment����Ϊ����״̬��
	 * 
	 * @param transaction
	 *            ���ڶ�Fragmentִ�в���������
	 */
	private void hideFragments(FragmentTransaction transaction) {
		// TODO Auto-generated method stub

		if (shouYeFragment != null) {
			transaction.hide(shouYeFragment);
		}
		if (hotComicFragment != null) {
			transaction.hide(hotComicFragment);
		}
		if (comicKuaiDiFragment != null) {
			transaction.hide(comicKuaiDiFragment);
		}
		if (myFavorFragment != null) {
			transaction.hide(myFavorFragment);
		}
		if (settingFragment != null) {
			transaction.hide(settingFragment);
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle("mTitle");
	}

	/**
	 * �û���½���߲鿴�û���Ϣ
	 * @param view
	 */
	public void toLogin(View view){
		
		Intent intent = new Intent(MainActivity.this, UserInformation.class);
		startActivity(intent);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private void onOptionsItemSelected() {
		// TODO Auto-generated method stub

	}

}
