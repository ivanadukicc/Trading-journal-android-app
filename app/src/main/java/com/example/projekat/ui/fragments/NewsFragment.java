package com.example.projekat.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekat.R;
import com.example.projekat.model.NewsItem;
import com.example.projekat.ui.adapters.NewsAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsFragment extends Fragment {

    private RecyclerView rv;
    private NewsAdapter adapter;

    private ExecutorService exec = Executors.newFixedThreadPool(3);

    List<String> FEEDS = Arrays.asList(
            "https://www.coindesk.com/arc/outboundfeeds/rss/?outputType=xml",
            "https://cointelegraph.com/rss",
            "https://www.binance.com/en/blog/rss"
    );
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle s) {
        View v;
        v = inf.inflate(R.layout.news, c, false);
        rv = v.findViewById(R.id.rvNews);
        return v;
    }

    private void openLink(NewsItem n) {
        try {
            CustomTabsIntent tabs = new CustomTabsIntent.Builder().build();
            tabs.launchUrl(requireContext(), Uri.parse(n.url));
        }catch (Exception e) {
            Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(n.url));
            startActivity(i);
        }
    }
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v,s);
        adapter = new NewsAdapter(n -> openLink(n));

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        fetchAllFeeds();

    }
    private void fetchAllFeeds() {
        exec.execute(() -> {
            List<NewsItem> all = new ArrayList<>();
            for(String url : FEEDS ) {
                String source = Uri.parse(url).getHost();
                all.addAll(fetchRss(url, source));
            }
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> adapter.submitList(all));
            }

        });

    }
    private List<NewsItem> fetchRss(String rssUrl, String source) {
        List<NewsItem> list = new ArrayList<>();
        try (InputStream is = new URL(rssUrl).openStream()) {
            XmlPullParserFactory f = XmlPullParserFactory.newInstance();
            f.setNamespaceAware(false);
            XmlPullParser x = f.newPullParser();
            x.setInput(is, null);

            int event = x.getEventType();
            boolean isAtom = false;
            boolean inItem = false, inTitle = false, inLink = false;
            String title = null, link = null;

            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG: {
                        String tag = x.getName();
                        if ("feed".equalsIgnoreCase(tag)) isAtom = true;

                        if ((!isAtom && "item".equalsIgnoreCase(tag)) || (isAtom && "entry".equalsIgnoreCase(tag))) {
                            inItem = true;
                        } else if (inItem && "title".equalsIgnoreCase(tag)) {
                            inTitle = true;
                        } else if (inItem && "link".equalsIgnoreCase(tag)) {
                            if (isAtom) {

                                String href = x.getAttributeValue(null, "href");
                                if (href != null && href.length() > 0) link = href;
                            } else {

                                inLink = true;
                            }
                        }
                        break;
                    }
                    case XmlPullParser.TEXT: {
                        if (inItem && inTitle) {
                            title = x.getText();
                        }
                        if (inItem && inLink && (link == null || link.isEmpty())) {
                            link = x.getText();
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG: {
                        String tag = x.getName();
                        if ("title".equalsIgnoreCase(tag)) inTitle = false;
                        if ("link".equalsIgnoreCase(tag)) inLink = false;

                        if ((!isAtom && "item".equalsIgnoreCase(tag)) || (isAtom && "entry".equalsIgnoreCase(tag))) {
                            if (title != null && link != null) {
                                list.add(new NewsItem(title.trim(), link.trim(), source));
                            }

                            inItem = false;
                            inTitle = false;
                            inLink = false;
                            title = null;
                            link = null;
                        }
                        break;
                    }
                }
                event = x.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void onDestroyView() {
        super.onDestroyView();
        if(exec != null) {
            exec.shutdownNow();
        }
        rv = null;
        adapter = null;

    }
}
