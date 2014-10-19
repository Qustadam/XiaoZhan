package com.zzj.xiaozhan.utils;

import com.zzj.xiaozhan.R;

import io.vov.vitamio.widget.MediaController;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
/**
 * �Զ���mediaController
 * @author Tomi_Enn
 *
 */
public class AppMediaController extends MediaController {

	private Context context;

	public AppMediaController(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * ��д����
	 */
	@Override
	protected View makeControllerView() {
		return ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.media_controller_view, this);
	}

}
