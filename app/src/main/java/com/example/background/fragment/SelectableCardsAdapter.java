package com.example.background.fragment;/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;
import com.example.background.R;
import com.example.background.module.Bill;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

/**
 * An Adapter that works with a collection of selectable card items
 */
class SelectableCardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Bill> items;

    private SelectionTracker<Long> selectionTracker;

    public SelectableCardsAdapter() {
        this.items = new ArrayList<>();
    }

    public void setItems(List<Bill> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_orders, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Bill item = items.get(position);
        ((ItemViewHolder) viewHolder).bind(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final Details details;
        private final TextView cash;
        private final MaterialCardView materialCardView;
        private final TextView titleView;
        private final TextView subtitleView;
        private final TextView dealer;

        ItemViewHolder(View itemView) {
            super(itemView);
            cash = itemView.findViewById(R.id.cash);
            materialCardView = itemView.findViewById(R.id.item_card);
            titleView = itemView.findViewById(R.id.name);
            subtitleView = itemView.findViewById(R.id.time);
            dealer = itemView.findViewById(R.id.dealer);
            details = new Details();
        }

        private void bind(Bill item, int position) {
            details.position = position;
            titleView.setText(item.getName());
            subtitleView.setText(item.getTime());
            dealer.setText(item.getDealer());
            String cost = item.getCash() + "";
            cash.setText("¥ " + cost.substring(0,cost.indexOf('.') + 2));
            if (selectionTracker != null) {
                bindSelectedState();
            }
        }

        private void bindSelectedState() {
            materialCardView.setSelected(selectionTracker.isSelected(details.getSelectionKey()));
        }

        ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
            return details;
        }
    }

    static class DetailsLookup extends ItemDetailsLookup<Long> {

        private final RecyclerView recyclerView;

        DetailsLookup(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Nullable
        @Override
        public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (view != null) {
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                if (viewHolder instanceof ItemViewHolder) {
                    return ((ItemViewHolder) viewHolder).getItemDetails();
                }
            }
            return null;
        }
    }

    static class KeyProvider extends ItemKeyProvider<Long> {

        KeyProvider(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
            super(ItemKeyProvider.SCOPE_MAPPED);
        }

        @Nullable
        @Override
        public Long getKey(int position) {
            return (long) position;
        }

        @Override
        public int getPosition(@NonNull Long key) {
            long value = key;
            return (int) value;
        }
    }

    static class Details extends ItemDetailsLookup.ItemDetails<Long> {

        long position;

        Details() {
        }

        @Override
        public int getPosition() {
            return (int) position;
        }

        @Nullable
        @Override
        public Long getSelectionKey() {
            return position;
        }

        @Override
        public boolean inSelectionHotspot(@NonNull MotionEvent e) {
            return false;
        }

        @Override
        public boolean inDragRegion(@NonNull MotionEvent e) {
            return true;
        }
    }
}
