package com.senior.cyber.tv.web.data;

import com.senior.cyber.frmk.common.base.WicketFactory;
import com.senior.cyber.frmk.common.wicket.markup.html.FullCalendarItem;
import com.senior.cyber.frmk.common.wicket.markup.html.FullCalendarProvider;
import com.senior.cyber.tv.dao.entity.Channel;
import com.senior.cyber.tv.dao.entity.Show;
import com.senior.cyber.tv.web.pages.show.ShowModifyPage;
import com.senior.cyber.tv.web.repository.ChannelRepository;
import com.senior.cyber.tv.web.repository.ShowRepository;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.ApplicationContext;

import java.util.*;

public class ShowProvider extends FullCalendarProvider {

    private static final String RED = "#d32f2f";
    private static final String PINK = "#c2185b";
    private static final String PURPLE = "#7b1fa2";
    private static final String DEEP_PURPLE = "#512da8";
    private static final String INDIGO = "#303f9f";
    private static final String BLUE = "#1976d2";
    private static final String LIGHT_BLUE = "#0288d1";
    private static final String CYAN = "#0097a7";
    private static final String TEAL = "#00796b";
    private static final String GREEN = "#388e3c";
    private static final String LIGHT_GREEN = "#689f38";
    private static final String LIME = "#afb42b";
    private static final String YELLOW = "#fbc02d";
    private static final String AMBER = "#ffa000";
    private static final String ORANGE = "#f57c00";
    private static final String DEEP_ORANGE = "#e64a19";
    private static final String BROWN = "#5d4037";
    private static final String GRAY = "#616161";
    private static final String BLUE_GRAY = "#455a64";

    private String channelId;

    public ShowProvider() {
    }

    public ShowProvider(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public List<FullCalendarItem> query(Date start, Date end) {
        ApplicationContext context = WicketFactory.getApplicationContext();
        ShowRepository showRepository = context.getBean(ShowRepository.class);
        ChannelRepository channelRepository = context.getBean(ChannelRepository.class);

        Map<String, String> memory = new HashMap<>();

        Channel channel = null;
        if (this.channelId != null && !"".equals(this.channelId)) {
            Optional<Channel> optionalChannel = channelRepository.findById(Long.parseLong(this.channelId));
            channel = optionalChannel.orElseThrow();
        }

        List<Show> shows = null;
        if (channel == null) {
            shows = showRepository.findByScheduleBetween(start, end);
        } else {
            shows = showRepository.findByChannelAndScheduleBetween(channel, start, end);
        }

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");

        List<String> colors = Arrays.asList(RED, PINK, PURPLE, DEEP_PURPLE, INDIGO, BLUE, LIGHT_BLUE, CYAN, TEAL, GREEN, LIGHT_GREEN, LIME, YELLOW, AMBER, ORANGE, DEEP_ORANGE, BROWN, GRAY, BLUE_GRAY);

        List<FullCalendarItem> items = new ArrayList<>(shows.size());
        int j = colors.size();
        for (Show show : shows) {
            if (show.getDuration() == 0) {
                continue;
            }
            String color = null;
            if (memory.containsKey(show.getName())) {
                color = memory.get(show.getName());
            } else {
                color = colors.get(j % colors.size());
                memory.put(show.getName(), color);
                j = j + 1;
            }
            LocalDateTime dt = LocalDateTime.parse(DateFormatUtils.format(show.getSchedule(), "yyyy-MM-dd") + "T" + DateFormatUtils.format(show.getStartAt(), "HH:mm:ss"), formatter);
            Date s = dt.toDate();
            Date e = dt.plusMinutes(show.getDuration()).toDate();
            FullCalendarItem item = new FullCalendarItem();
            if (channel != null) {
                item.setTitle(show.getName());
            } else {
                item.setTitle(show.getChannel().getName() + " - " + show.getName());
            }
            item.setStart(s);
            item.setEnd(e);
            item.setAllDay(false);
            item.setBackgroundColor(color);
            item.setBorderColor(color);
            item.setTextColor("#FFFFFF");
            RequestCycle cycle = RequestCycle.get();
            PageParameters parameters = new PageParameters();
            parameters.add("uuid", show.getId());
            String url = cycle.urlFor(ShowModifyPage.class, parameters).toString();
            item.setResourceId(url);
            item.setUrl(url);
            items.add(item);
        }

        return items;
    }

}
