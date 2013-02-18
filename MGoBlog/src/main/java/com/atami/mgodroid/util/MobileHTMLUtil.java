package com.atami.mgodroid.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;

public class MobileHTMLUtil {

    /**
     * Cleans Node HTML fragments and makes them more user fridnly for mobile devices
     *
     * @param html an HTML fragment
     * @return a cleaned HTML body
     */
    public static String cleanNode(String html) {

        Document doc = Jsoup.parseBodyFragment(html);
        Elements videos = doc
                .select("iframe[src~=(youtube\\.com)], embed[src~=(youtube\\.com)]");

        // start by adding placeholder <a> tags to keep videos before cleanNode
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
        html = Jsoup.clean(doc.toString(), Whitelist.relaxed());
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

    public static String addLinkToBody(String html, String link) {
        Document doc = Jsoup.parse(html);
        Element href = new Element(Tag.valueOf("a"), "");
        href.attr("href", link);
        href.text(link);
        doc.body().appendChild(href);
        return doc.toString();
    }

    public static String cleanComments(String html) {

        Document doc = Jsoup.parseBodyFragment(html);
        Elements media = doc.select("img[src], iframe[src], embed[src]");

        for (Element m : media) {
            Element p = new Element(Tag.valueOf("p"), "");
            Element e = new Element(Tag.valueOf("a"), "").attr("href",
                    m.attr("src"));
            e.text(m.attr("src"));

            p.appendChild(e);
            m.replaceWith(p);
        }

        // have to whitelist AFTER replacing videos with images so that
        // the videos don't get "cleaned" out
        html = Jsoup.clean(doc.toString(), Whitelist.relaxed());
        doc = Jsoup.parseBodyFragment(html);

        // Add MGoBlog css
        doc.head()
                .append("<link rel=\"stylesheet\" type=\"text/css\" href=\"node_body.css\"></style>");

        // TODO: Find plain text links and wrap them in an anchor tag
        return doc.toString().replace("<p>&nbsp;</p>", "").trim();
    }

}
