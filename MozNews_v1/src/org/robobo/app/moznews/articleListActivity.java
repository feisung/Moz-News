package org.robobo.app.moznews;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/**
 * An activity representing a list of articles. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link articleDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link HeadlinesFragment} and the item details (if present) is a
 * {@link articleDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link HeadlinesFragment.Callbacks} interface to listen for item selections.
 */
public class articleListActivity extends SherlockFragmentActivity implements
		HeadlinesFragment.OnHeadlineSelectedListener {

	public static final String PREFS_NAME = "articleListActivity";
	private static String TAG = "Article List Activity";
	public static final String url = "http://mozapp.necto.me/api/stories.php";
	private ProgressDialog progressDialog;
	private String jsonFeed;

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_list);

		// Restore preferences
		SharedPreferences data = getSharedPreferences(PREFS_NAME, 0);
		String feed = data.getString("jsonFeed", jsonFeed);

		if (findViewById(R.id.article_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((HeadlinesFragment) getSupportFragmentManager().findFragmentById(
					R.id.article_list)).setActivateOnItemClick(true);
		}

		if (feed == null) {

			if (GetContentIfNetworkAvailable() == true) {
				// instantiate and execute AsyncTask
//				progressDialog = new ProgressDialog(articleListActivity.this);
//				progressDialog.setTitle("Buscando Conteudo...");
//				progressDialog.setMessage("Aguarde Por Favor.");
//				progressDialog.setCancelable(false);
//				progressDialog.setIndeterminate(true);
//				progressDialog.show();
				Toast.makeText(
						this,
						"Buscando novas noticias, aguarde por favor...",
						Toast.LENGTH_LONG).show();
				new ExecutePostAsync().execute(url);

			} else {
				Toast.makeText(
						this,
						"Você parece não ter acesso à rede no momento, por favor, tente novamente mais tarde.",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			// data already loaded just start activity with data there
			Log.i(TAG, "Data already loaded");
			HeadlinesFragment headlinesFragment = (HeadlinesFragment) getSupportFragmentManager()
					.findFragmentById(R.id.article_list);
			headlinesFragment.SearchResultBuilder(feed, mTwoPane);

			// Refresh Data quietly
			if (GetContentIfNetworkAvailable() == true) {
				// instantiate and execute AsyncTask
				new ExecutePostAsync().execute(url);
			} else {
				Toast.makeText(
						this,
						"Você parece não ter acesso à rede no momento, por favor, tente novamente mais tarde.",
						Toast.LENGTH_SHORT).show();
			}

		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	public boolean GetContentIfNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// if no network is available networkInfo will be null
		// otherwise check if we are connected
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.about:
			Toast.makeText(this,
					"This App was written by Robobo for Mozambique",
					Toast.LENGTH_SHORT).show();
			return true;
		case R.id.refresh:
			Toast.makeText(this, "Carregando novo conteudo . . .",
					Toast.LENGTH_SHORT).show();
			refreshData();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void refreshData() {
		// TODO Auto-generated method stub
		if (GetContentIfNetworkAvailable() == true) {
			// instantiate and execute AsyncTask
			new ExecutePostAsync().execute(url);
			Log.i(TAG, "Reloading Data");
		} else {
			Toast.makeText(
					this,
					"Você parece não ter acesso à rede no momento, por favor, tente novamente mais tarde.",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Callback method from {@link HeadlinesFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onHeadlineSelected(String[] index) {
		Log.i(TAG, "Item Selected=" + index);
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putStringArray(articleDetailFragment.ARG_ITEM_ID, index);
			articleDetailFragment fragment = new articleDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.article_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, articleDetailActivity.class);
			detailIntent.putExtra(articleDetailFragment.ARG_ITEM_ID, index);
			startActivity(detailIntent);
		}
	}

	/*
	 * To-Do: Clean up unused elements ` Async HTTP Class
	 * 
	 * 
	 * 
	 * **************************************************************************
	 * ******
	 */
	private class ExecutePostAsync extends AsyncTask<String, Void, String> {
		/*
		 * Background Task to get the data
		 */

		protected void onPreExecute() {
			// Log.i(TAG, "Pre Execute");

		}

		@Override
		protected String doInBackground(String... params) {
			String output = null;
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(params[0]);
			// Log.v(TAG, "IP Address in use: " + ServerAddress);

			try {
				HttpResponse response = httpClient.execute(httpGet);
				// Log.v(TAG, "Post sent");

				HttpEntity httpEntity = response.getEntity();
				output = EntityUtils.toString(httpEntity);
				// Log.i(TAG, "The Output is" + output); //For Debug
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Log.i(TAG, "Got Site Result: " + output);

			return output;
		}

		protected void onPostExecute(String output) {

//			if (progressDialog.isShowing() == true) {
//				progressDialog.dismiss();
//			}
			// Log.i(TAG, "Got Here with " + output);
			HeadlinesFragment headlinesFragment = (HeadlinesFragment) getSupportFragmentManager()
					.findFragmentById(R.id.article_list);

			// Updates the ListView
			headlinesFragment.SearchResultBuilder(output, mTwoPane);

		}

	}
}
