package com.atami.mgodroid.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;

import android.util.Log;

public class MobileHTMLUtil {

	/**
	 * Cleans HTML fragments and makes them more user fridnly for mobile devices
	 * 
	 * @param html
	 *            an HTML fragment
	 * @return a cleaned HTML body
	 */
	public static String clean(String html) {

		Document doc = Jsoup.parseBodyFragment(html);
		Elements videos = doc
				.select("iframe[src~=(youtube\\.com)], object[data~=(youtube\\.com)], embed[src~=(youtube\\.com)]");

		// start by adding placeholder <a> tags to keep videos before clean
		for (Element video : videos) {
			Element div = new Element(Tag.valueOf("div"), "").attr("class",
					"video");
			Element thumbnail = new Element(Tag.valueOf("a"), "").attr("href",
					video.attr("src"));

			div.appendChild(thumbnail);
			video.replaceWith(div);
		}

		// have to whitelist AFTER replacing videos with images so that
		// the videos don't get "cleaned" out
		html = Jsoup.clean(doc.toString(), Whitelist.basicWithImages());
		doc = Jsoup.parseBodyFragment(html);

		videos = doc.select("a[href~=(youtube\\.com)]");

		// add images with styling to the <a> tags that point to youtube
		for (Element video : videos) {
			
			int len = video.attr("href").lastIndexOf("?");

			if (len <= 0) {
				len = video.attr("href").length();
			}

			String src = video.attr("href").substring(0, len);

			String videoID = src.replaceFirst(".*/([^/?]+).*", "$1");
			String thumbnailURL = String.format(
					"http://img.youtube.com/vi/%s/0.jpg", videoID);

			Element img = new Element(Tag.valueOf("img"), "")
					.attr("src",
							"https://raw.github.com/SeanPONeil/MGoBlog/master/MGoBlog/assets/play_button.png")
					.attr("style", "background:URL(" + thumbnailURL + ")");

			video.appendChild(img);
		}

		// Add MGoBlog css
		doc.head()
				.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"node_body.css\"></style>");

		// TODO: Find plain text links and wrap them in an anchor tag
		return doc.toString().replace("<p>&nbsp;</p>", "").trim();
	}

	public static CharSequence trimTrailingWhitespace(CharSequence source) {

		if (source == null)
			return "";

		int i = source.length();

		// loop back to the first non-whitespace character
		while (--i >= 0 && Character.isWhitespace(source.charAt(i))) {
		}

		return source.subSequence(0, i + 1);
	}

}
