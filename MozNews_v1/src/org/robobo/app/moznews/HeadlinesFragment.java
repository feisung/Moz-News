package org.robobo.app.moznews;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.robobo.app.moznews.data.FeedItems;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

/**
 * A list fragment representing a list of articles. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link articleDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the
 * {@link OnHeadlineSelectedListener} interface.
 */
public class HeadlinesFragment extends ListFragment implements
		OnItemClickListener {

	// The listener we are to notify when a headline is selected
	OnHeadlineSelectedListener mHeadlineSelectedListener = null;
	private static final String TAG = "Search Activity";
	public TextView tv;
	public ImageView imageview;
	String query;
	// Server Address that replies to Search Queries
	public static final String url = "http://mozapp.necto.me/api/stories.php";
	private ArrayList<FeedItems> resultArray;
	public static final String PREFS_NAME = "articleListActivity";


	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		return inflater.inflate(R.layout.activity_news, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		loadCategory(0);

	}

	/**
	 * Load and display the headlines for the given news category.
	 * 
	 * @param categoryIndex
	 *            the index of the news category to display.
	 */
	public void loadCategory(int categoryIndex) {
		// mHeadlinesList.clear();
		// int i;
		// NewsCategory cat =
		// NewsSource.getInstance().getCategory(categoryIndex);
		// for (i = 0; i < cat.getArticleCount(); i++) {
		// mHeadlinesList.add(cat.getArticle(i).getHeadline());
		// }
		// mListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mHeadlineSelectedListener = (OnHeadlineSelectedListener) activity;
		} catch (ClassCastException ex) {
			Log.e(TAG, "Casting the activity as a Callbacks listener failed"
					+ ex);
			mHeadlineSelectedListener = null;
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	/**
	 * Default constructor required by framework.
	 */
	public HeadlinesFragment() {
		super();
	}

	/**
	 * Sets the listener that should be notified of headline selection events.
	 * 
	 * @param listener
	 *            the listener to notify.
	 */
	public void setOnHeadlineSelectedListener(
			OnHeadlineSelectedListener listener) {
		mHeadlineSelectedListener = listener;
	}

	/**
	 * Represents a listener that will be notified of headline selections.
	 */
	public interface OnHeadlineSelectedListener {
		/**
		 * Called when a given headline is selected.
		 * 
		 * @param strings
		 *            the index of the selected headline.
		 */
		public void onHeadlineSelected(String[] strings);
	}

	/**
	 * Handles a click on a headline.
	 * 
	 * This causes the configured listener to be notified that a headline was
	 * selected.
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Log.i(TAG, "Got here with pos=" + position +
		// " mHeadlineSelectedListener=" + mHeadlineSelectedListener);

		if (null != mHeadlineSelectedListener) {
			mHeadlineSelectedListener.onHeadlineSelected(getDetail(position));
		}

	}

	/*
	 * This method provides Title, Source_url, Full Text and other relevant UI
	 * elements
	 */
	public String[] getDetail(int id) {
		FeedItems searchResults = resultArray().get(id);
		// We return to the next view: Title, Source_url+Name, Full_text,
		// Image_Url

		String[] viewItems = { searchResults.title, searchResults.summary,
				searchResults.image_url, searchResults.source_url,
				searchResults.source_text, searchResults.category, };
		return viewItems;
	}

	public ArrayList<FeedItems> resultArray() {
		return resultArray;

	}

	// public class to display results
	public void SearchResultBuilder(String output, Boolean mTwoPane) {

		// Log.i(TAG, "Result Builder with " + output);
		final ArrayList<FeedItems> results = Result(output);
		resultArray = results;

		ListAdapter myListAdapter = new ResultsItemAdepter(getActivity(),
				R.layout.listitem, results);
		setListAdapter(myListAdapter);
		// getListView().setOnItemClickListener(HeadlinesFragment.this);
		getListView().setOnItemClickListener(this);
		
		if(mTwoPane){
			//Only Load the detail view if the screen is adequate
			mHeadlineSelectedListener.onHeadlineSelected(getDetail(0));
		}

	}

	protected ArrayList<FeedItems> Result(String output) {

		ArrayList<FeedItems> results = new ArrayList<FeedItems>();
		try {
			// get JSONObject from result
			JSONObject resultObject = new JSONObject(output);
			// Log.i(TAG, "Got Site Result: " + resultObject);
			JSONArray responseArray = resultObject.getJSONArray("articles");
			// Log.i(TAG, "Got Result array: " + responseArray); // For Debug
			// loop through each result in the array
			for (int t = 0; t < responseArray.length(); t++) {
				// each item is a JSONObject
				JSONObject responseObject = responseArray.getJSONObject(t);
				// Log.i(TAG, "Loading Array Data: " + responseObject);
				// //For Debug

				FeedItems result = new FeedItems(responseObject.getString(
						"title").toString(), responseObject.get("full_text")
						.toString(), responseObject.get("source_text")
						.toString(), responseObject.get("category").toString(),
						responseObject.getString("img_url").toString(),
						responseObject.getString("source_url").toString());
				results.add(result);
								// Log.i(TAG,
				// "\n Product Name: "
				// + responseObject.getString("title")
				// .toString());
			}
			
			//If all goes well commit that new data
			SharedPreferences settings = this.getActivity().getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			//Log.i(TAG, "Output=" + output);
			editor.putString("jsonFeed", output);

			// Commit the edits!
			editor.commit();


		} catch (Exception e) {
			Log.i(TAG, "Something went wrong with JSON Parsing"); 
			//tv.setText("Was Unable to return any result!");
			Toast.makeText(
					HeadlinesFragment.this.getActivity(),
					"Some problems getting new data, try again later...",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return results;
	}

	public class ResultsItemAdepter extends ArrayAdapter<FeedItems> {
		private ArrayList<FeedItems> results;

		public ResultsItemAdepter(Context context, int textViewResourceId,
				ArrayList<FeedItems> results) {
			super(context, textViewResourceId, results);
			this.results = results;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.listitem, null);
			}

			FeedItems feedItems = results.get(position);
			// Log.i(TAG, "Value for SR= " + searchResults +
			// " and position: "+ position);
			if (feedItems != null) {
				TextView product_name = (TextView) v
						.findViewById(R.id.product_name);
				TextView description = (TextView) v
						.findViewById(R.id.http_response);
				SmartImageView image = (SmartImageView) v
						.findViewById(R.id.preview_sec);

				if (product_name != null) {
					product_name.setText(feedItems.title);
					// Log.i(TAG, "*Name: " + searchResults.product_name);
				}

				if (description != null) {
					description.setText(feedItems.summary);
					// Log.i(TAG, "*Description: "+
					// searchResults.product_description);
				}

				if (image != null) {
					image.setImageUrl(feedItems.image_url);
					// image.setImageBitmap(getBitmap(searchResults.image_url));
					// Log.i(TAG, "*Bitmap: " + searchResults.image_url);
				}
			} else {
				TextView product_name = (TextView) v
						.findViewById(R.id.product_name);
				product_name
						.setText("Unfortunately No Matched Results, Please Retry.");
			}
			return v;
		}
	}

	public void setActivatedPosition(int position) {
		// TODO Auto-generated method stub
		Log.i(TAG, "ActivationOnItemClick= " + position);
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;

	}

	/**
	 * Sets choice mode for the list
	 * 
	 * @param selectable
	 *            whether list is to be selectable.
	 */
	public void setSelectable(boolean selectable) {
		if (selectable) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		} else {
			getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
		}
	}
}
