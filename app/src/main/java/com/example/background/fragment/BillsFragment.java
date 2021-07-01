package com.example.background.fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.selection.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.background.R;
import com.example.background.Utils.BillManage;
import com.example.background.activities.MainActivity;
import com.example.background.module.Bill;

import java.text.SimpleDateFormat;
import java.util.*;

public class BillsFragment extends BaseFragment implements android.view.ActionMode.Callback {
    private View view;
    private RecyclerView recycler;
    private ImageButton sort;
    private EditText search;
    private ActionMode actionMode;
    private SelectableCardsAdapter adapter;
    private SelectionTracker<Long> selectionTracker;
    private BillManage billManage;
    private Calendar calendar, tempCalendar;
    private TimeZone zone = TimeZone.getTimeZone("GMT");
    private SimpleDateFormat format;
    private Date date;
    private int type = -1;

    private final int[] colors = {
            Color.parseColor("#FFA500"),
            Color.parseColor("#008000"),
            Color.parseColor("#1D5795"),
            Color.parseColor("#FFB6C1"),
            Color.parseColor("#CD5C5C"),
            Color.parseColor("#778899"),
            Color.parseColor("#4682B4"),
            Color.parseColor("#FFFFFF")
    };
    private final String[] daysOfWeek = {
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
    };
    private final int[] radios = {
            R.id.food,
            R.id.travel,
            R.id.bills,
            R.id.outdoor,
            R.id.edu,
            R.id.clothes,
            R.id.health,
            R.id.rests
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab3, container, false);
        billManage = new BillManage(((MainActivity)context).getOrders());
        calendar = new GregorianCalendar();
        tempCalendar = Calendar.getInstance();
        format = new SimpleDateFormat("yyyy-M-d HH:mm:ss", Locale.CHINA);
        format.setTimeZone(zone);
        initView();
        return view;
    }

    @Override
    public void initView() {
        recycler = view.findViewById(R.id.recycler);
        sort = view.findViewById(R.id.sort);
        search = view.findViewById(R.id.search);
        initRecycler();
        sort.setOnClickListener(view -> initSortDialog());
        adapter.setItems(billManage.getTotalOrders());
        search.setOnEditorActionListener((textView, i, keyEvent) -> {
            adapter.setItems(billManage.getSortList(textView.getText().toString()));
            adapter.notifyDataSetChanged();
            return true;
        });
    }

    private void initRecycler() {
        adapter = new SelectableCardsAdapter();
        recycler.setAdapter(adapter);

        selectionTracker =
                new SelectionTracker.Builder<>(
                        "card_selection",
                        recycler,
                        new SelectableCardsAdapter.KeyProvider(adapter),
                        new SelectableCardsAdapter.DetailsLookup(recycler),
                        StorageStrategy.createLongStorage())
                        .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                        .build();

        adapter.setSelectionTracker(selectionTracker);
        selectionTracker.addObserver(
                new SelectionTracker.SelectionObserver<Long>() {
                    @Override
                    public void onSelectionChanged() {
                        if (selectionTracker.getSelection().size() > 0) {
                            if (actionMode == null) {
                                actionMode = Objects.requireNonNull(getActivity()).startActionMode(BillsFragment.this);
                            }
                            actionMode.setTitle(String.valueOf(selectionTracker.getSelection().size()));
                        } else if (actionMode != null) {
                            actionMode.finish();
                        }
                    }
                });
        recycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }

    private void initSortDialog() {
        type = -1;
        final AlertDialog customizeDialog = new AlertDialog.Builder(getContext()).create();
        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sort, null);
        customizeDialog.setTitle("sort");
        customizeDialog.setView(dialogView);
        final DatePicker dateStart = dialogView.findViewById(R.id.date_start);
        final DatePicker dateEnd = dialogView.findViewById(R.id.date_end);
        final RadioGroup radioGroup = dialogView.findViewById(R.id.radio_group);
        for (int i = 0; i < 8; i++) {
            final int finalI = i;
            ((RadioButton) dialogView.findViewById(radios[i])).setOnCheckedChangeListener((compoundButton, b) -> {
                for (int j = 0; j < 8; j++) ((RadioButton) radioGroup.findViewById(radios[j])).setChecked(false);
                ((RadioButton) radioGroup.findViewById(radios[finalI])).setChecked(type != finalI);
                type = finalI;
            });
        }
        ((Button) dialogView.findViewById(R.id.positive)).setText(R.string.yes);
        dialogView.findViewById(R.id.positive).setOnClickListener(view -> {
            Calendar calendar1 = new GregorianCalendar(dateStart.getYear(), dateStart.getMonth(), dateStart.getDayOfMonth());
            Calendar calendar2 = new GregorianCalendar(dateEnd.getYear(), dateEnd.getMonth(), dateEnd.getDayOfMonth());
            List<Bill> sorted = billManage.getSortList(type, calendar1, calendar2);
            adapter.setItems(sorted);
            adapter.notifyDataSetChanged();
            customizeDialog.dismiss();
        });
        ((Button) dialogView.findViewById(R.id.negative)).setText(R.string.cancel);
        dialogView.findViewById(R.id.negative).setOnClickListener(view -> customizeDialog.dismiss());
        customizeDialog.show();
    }

    @Override
    public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(android.view.ActionMode mode) {
        selectionTracker.clearSelection();
        this.actionMode = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
