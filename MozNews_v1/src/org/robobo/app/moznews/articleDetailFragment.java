package org.robobo.app.moznews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.loopj.android.image.SmartImageView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/**
 * A fragment representing a single article detail screen. This fragment is
 * either contained in a {@link articleListActivity} in two-pane mode (on
 * tablets) or a {@link articleDetailActivity} on handsets.
 */
public class articleDetailFragment extends SherlockFragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	public String[] viewItems;

	private static String TAG = "Article Detail Fragment";

	private String title, full_text, image_url, source_url, category,
			source_text;

	MenuItem share;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public articleDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {

			// Change ID received from ID to String
			viewItems = getArguments().getStringArray(ARG_ITEM_ID);
			Log.i(TAG, "ID= " + viewItems);

			title = viewItems[0];
			full_text = viewItems[1];
			image_url = viewItems[2];
			source_url = viewItems[3];
			source_text = viewItems[4];
			category = viewItems[5];

			// Log.i(TAG, "arguments= " + ID_received);

		}
		setHasOptionsMenu(true);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		share = menu.add(R.string.share);
		share.setIcon(R.drawable.action);
		share.setVisible(true);
		share.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		shareIt();
		switch (item.getItemId()) {
		case R.string.share:
			Log.i(TAG, "ID=" + item);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void shareIt() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		// Add data to the intent, the receiving app will decide what to do with
		// it.
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, "Partilhado do Moz News App: "
				+ title + " source: " + source_url);
		startActivity(Intent.createChooser(intent, "Escolhe como divulgar..."));

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_article_detail,
				container, false);

		SmartImageView image = (SmartImageView) rootView
				.findViewById(R.id.preview_sec);

		((TextView) rootView.findViewById(R.id.title)).setText(title);
		((TextView) rootView.findViewById(R.id.source)).setText(source_text);
		image.setImageUrl(image_url);

		((TextView) rootView.findViewById(R.id.article_detail))
				.setText(full_text);

		return rootView;
	}

}
