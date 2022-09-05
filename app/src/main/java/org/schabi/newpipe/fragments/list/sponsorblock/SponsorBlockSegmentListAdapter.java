package org.schabi.newpipe.fragments.list.sponsorblock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.schabi.newpipe.R;
import org.schabi.newpipe.util.SponsorBlockUtils;
import org.schabi.newpipe.util.VideoSegment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class SponsorBlockSegmentListAdapter extends
        RecyclerView.Adapter<SponsorBlockSegmentListAdapter.SponsorBlockSegmentItemViewHolder> {
    private final Context context;
    private final ArrayList<VideoSegment> segments = new ArrayList<>();

    public SponsorBlockSegmentListAdapter(final Context context) {
        this.context = context;
    }
    public void setItems(final VideoSegment[] items) {
        this.segments.clear();

        if (items != null) {
            Collections.addAll(this.segments, items);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SponsorBlockSegmentListAdapter.SponsorBlockSegmentItemViewHolder onCreateViewHolder(
            @NonNull final ViewGroup parent, final int viewType) {
        return new SponsorBlockSegmentItemViewHolder(
                LayoutInflater
                        .from(context)
                        .inflate(R.layout.list_segments_item, parent, false));
    }

    @Override
    public void onBindViewHolder(
            @NonNull final SponsorBlockSegmentListAdapter.SponsorBlockSegmentItemViewHolder holder,
            final int position) {
        final VideoSegment segment = segments.get(position);
        holder.updateFrom(segment);
    }

    @Override
    public int getItemCount() {
        return segments.size();
    }

    public static class SponsorBlockSegmentItemViewHolder extends RecyclerView.ViewHolder {
        private final View itemSegmentColorView;
        private final TextView itemSegmentNameTextView;
        private final TextView itemSegmentStartTimeTextView;
        private final TextView itemSegmentEndTimeTextView;

        public SponsorBlockSegmentItemViewHolder(@NonNull final View itemView) {
            super(itemView);

            itemSegmentColorView = itemView.findViewById(R.id.item_segment_color_view);
            itemSegmentNameTextView = itemView.findViewById(
                    R.id.item_segment_category_name_textview);
            itemSegmentStartTimeTextView = itemView.findViewById(
                    R.id.item_segment_start_time_textview);
            itemSegmentEndTimeTextView = itemView.findViewById(R.id.item_segment_end_time_textview);
        }

        private void updateFrom(final VideoSegment segment) {
            // category color
            final Integer segmentColor =
                    SponsorBlockUtils.parseSegmentCategory(segment.category, itemView.getContext());
            if (segmentColor != null) {
                itemSegmentColorView.setBackgroundColor(segmentColor);
            }

            // category name
            final String friendlyCategoryName =
                    SponsorBlockUtils.getFriendlyCategoryName(segment.category);
            itemSegmentNameTextView.setText(friendlyCategoryName);

            // from
            final String startText = millisecondsToString(segment.startTime);
            itemSegmentStartTimeTextView.setText(startText);

            // to
            final String endText = millisecondsToString(segment.endTime);
            itemSegmentEndTimeTextView.setText(endText);
        }

        private String millisecondsToString(final double milliseconds) {
            final int seconds = (int) (milliseconds / 1000) % 60;
            final int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
            final int hours   = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

            return String.format(Locale.getDefault(),
                    "%02d:%02d:%02d", hours, minutes, seconds);
        }
    }
}
