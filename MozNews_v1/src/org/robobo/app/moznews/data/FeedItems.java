package org.robobo.app.moznews.data;

public class FeedItems {
	public String title;
	public String summary;
	public String image_url;
	public String source_url;
	public String source_text;
	public String category;

	public FeedItems(String title, String summary, String source_text,
			String category, String image_url, String source_url) {
		// TODO Auto-generated constructor stub
		this.title = title;
		this.summary = summary;
		this.image_url = image_url;
		this.source_url = source_url;
		this.source_text = source_text;
		this.category = category;
	}
}
