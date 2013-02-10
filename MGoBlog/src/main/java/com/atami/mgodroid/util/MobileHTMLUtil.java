package com.atami.mgodroid.util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

public class MobileHTMLUtil {

    /**
     * Cleans HTML fragments and makes them more user
     * fridnly for mobile devices
     * @param html an HTML fragment
     * @return a cleaned HTML body
     */
    public static String clean(String html){

        //Add MGoBlog css before storing
        Document doc = Jsoup.parseBodyFragment(html);
        Element headNode = doc.head();
        headNode.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"node_body.css\"></style>");

        //Iterate through iframes, replace Youtube embeds with
        //a thumbnail that links to Youtube
        for (Element iframe : doc.select("iframe")) {
            if (iframe.attr("src").contains("youtube")) {
                Element div = new Element(Tag.valueOf("div"), "").attr("class", "video");
                Element thumbnail = new Element(Tag.valueOf("a"), "").attr("href", iframe.attr("src"));
                String videoID = iframe.attr("src").replaceFirst(".*/([^/?]+).*", "$1");
                String thumbnailURL = String.format("http://img.youtube.com/vi/%s/0.jpg", videoID);
                Element img = new Element(Tag.valueOf("img"), "")
                        .attr("src", "play_button.png")
                        .attr("style", "background:URL(" + thumbnailURL + ")");
                thumbnail.appendChild(img);
                div.appendChild(thumbnail);
                iframe.replaceWith(div);
            }
        }

        for (Element embed : doc.select("embed")) {
            if (embed.attr("src").contains("youtube")) {
                Element div = new Element(Tag.valueOf("div"), "").attr("class", "video");
                Element thumbnail = new Element(Tag.valueOf("a"), "").attr("href", embed.attr("src"));
                String src = embed.attr("src").substring(0, embed.attr("src").lastIndexOf("?"));
                String videoID = src.replaceFirst(".*/([^/?]+).*", "$1");
                String thumbnailURL = String.format("http://img.youtube.com/vi/%s/0.jpg", videoID);
                Element img = new Element(Tag.valueOf("img"), "")
                        .attr("src", "play_button.png")
                        .attr("style", "background:URL(" + thumbnailURL + ")");
                thumbnail.appendChild(img);
                div.appendChild(thumbnail);
                embed.replaceWith(div);
            }

        }

        //TODO: Find plain text links and wrap them in an anchor tag

        return doc.toString().replace("<p>&nbsp;</p>", "").trim();
    }
    
    public static CharSequence trimTrailingWhitespace(CharSequence source) {

	    if(source == null)
	        return "";

	    int i = source.length();

	    // loop back to the first non-whitespace character
	    while(--i >= 0 && Character.isWhitespace(source.charAt(i))) {
	    }

	    return source.subSequence(0, i+1);
	}
}
