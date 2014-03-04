package com.cw.indexlistview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cw.indexlistview.model.CityListCityModel;
import com.cw.indexlistview.utils.FileUtils;
import com.cw.indexlistview.widget.SectionListAdapter;
import com.cw.indexlistview.widget.SectionListView;

public class SimpleActivity extends Activity {

    public static String CITY_RESULT = "city_result";
    // listview section
    private StandardArrayAdapter arrayAdapter;
    private SectionListAdapter sectionAdapter;
    private SectionListView listView;

    EditText search;

    // sideIndex
    LinearLayout sideIndex;
    // height of side index
    private int sideIndexHeight, sideIndexSize;
    // list with items for side index
    private ArrayList<Object[]> sideIndexList = new ArrayList<Object[]>();

    // an array with countries to display in the list
    private ArrayList<CityListCityModel> mCityListModel;
    static CityListCityModel[] mCityArrayModel;
    private List<CityListCityModel> mCityList = new ArrayList<CityListCityModel>();
    private TextView mLetterTipTextView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        setTitle("城市列表");
        search = (EditText)findViewById(R.id.search_query);
        search.addTextChangedListener(filterTextWatcher);
        listView = (SectionListView)findViewById(R.id.section_list_view);
        sideIndex = (LinearLayout)findViewById(R.id.list_index);
        sideIndex.setOnTouchListener(new Indextouch());

        // not forget to sort array
        getAllCityList();
        mCityListModel = new ArrayList<CityListCityModel>(Arrays.asList(mCityArrayModel));
        arrayAdapter = new StandardArrayAdapter(mCityListModel);

        // adaptor for section
        sectionAdapter = new SectionListAdapter(this.getLayoutInflater(), arrayAdapter);
        listView.setAdapter(sectionAdapter);

        poplulateSideview();

    }

    private void getAllCityList()
    {
        mCityList.addAll(Arrays.asList(FileUtils.readCityList()));
        mCityArrayModel = (CityListCityModel[])mCityList.toArray(new CityListCityModel[mCityList.size()]);
    }

    private class Indextouch implements OnTouchListener {

        @SuppressWarnings("deprecation")
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {

            if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {

                sideIndex.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_rectangle_shape));

                // now you know coordinates of touch
                float sideIndexX = event.getX();
                float sideIndexY = event.getY();

                if (sideIndexX > 0 && sideIndexY > 0) {
                    // and can display a proper item it country list
                    displayListItem(sideIndexY);

                }
            } else {
                sideIndex.setBackgroundColor(Color.TRANSPARENT);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mLetterTipTextView.setVisibility(View.GONE);
            }
            return true;

        }

    };

    public void onWindowFocusChanged(boolean hasFocus)
    {
        // get height when component is poplulated in window
        sideIndexHeight = sideIndex.getHeight();
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onWindowFocusChanged(hasFocus);
    }

    private class StandardArrayAdapter extends BaseAdapter implements Filterable {

        private final ArrayList<CityListCityModel> items;

        public StandardArrayAdapter(ArrayList<CityListCityModel> args) {
            this.items = args;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent)
        {
            View view = convertView;
            if (view == null) {
                final LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.row, null);
            }
            TextView textView = (TextView)view.findViewById(R.id.row_title);
            RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.row_layout);
            layout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v)
                {
                    // Intent intent = new Intent();
                    // intent.putExtra(CITY_RESULT, items.get(position));
                    // setResult(RESULT_OK, intent);
                    // finish();
                    Toast.makeText(SimpleActivity.this, items.get(position).getName(), Toast.LENGTH_SHORT).show();

                }
            });
            if (textView != null) {
                textView.setText(items.get(position).getName());
            }
            return view;
        }

        @Override
        public int getCount()
        {
            // TODO Auto-generated method stub
            return items.size();
        }

        @Override
        public Filter getFilter()
        {
            Filter listfilter = new MyFilter();
            return listfilter;
        }

        @Override
        public Object getItem(int position)
        {
            // TODO Auto-generated method stub
            return items.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            // TODO Auto-generated method stub
            return 0;
        }
    }

    @SuppressLint("DefaultLocale")
    public class MyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            // NOTE: this function is *always* called from a background thread, and
            // not the UI thread.
            constraint = search.getText().toString();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                // do not show side index while filter results
                runOnUiThread(new Runnable() {

                    @Override
                    public void run()
                    {
                        ((LinearLayout)findViewById(R.id.list_index)).setVisibility(View.INVISIBLE);
                    }
                });

                ArrayList<CityListCityModel> filt = new ArrayList<CityListCityModel>();
                ArrayList<CityListCityModel> Items = new ArrayList<CityListCityModel>();
                synchronized (this) {
                    Items = mCityListModel;
                }
                for (int i = 0; i < Items.size(); i++) {
                    CityListCityModel item = Items.get(i);
                    if (item.getJianpin().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        filt.add(item);
                    }
                }

                result.count = filt.size();
                result.values = filt;
            } else {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run()
                    {
                        ((LinearLayout)findViewById(R.id.list_index)).setVisibility(View.VISIBLE);
                    }
                });
                synchronized (this) {
                    result.count = mCityListModel.size();
                    result.values = mCityListModel;
                }

            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            @SuppressWarnings("unchecked")
            ArrayList<CityListCityModel> filtered = (ArrayList<CityListCityModel>)results.values;
            arrayAdapter = new StandardArrayAdapter(filtered);
            sectionAdapter = new SectionListAdapter(getLayoutInflater(), arrayAdapter);
            listView.setAdapter(sectionAdapter);

        }

    }

    private void displayListItem(float sideIndexY)
    {
        // compute number of pixels for every side index item
        double pixelPerIndexItem = (double)sideIndexHeight / sideIndexSize;

        // compute the item index for given event position belongs to
        int itemPosition = (int)(sideIndexY / pixelPerIndexItem);

        if (itemPosition < sideIndexList.size()) {
            // get the item (we can do it since we know item index)
            Object[] indexItem = sideIndexList.get(itemPosition);
            mLetterTipTextView = (TextView)findViewById(R.id.letter_tips);
            mLetterTipTextView.setVisibility(View.VISIBLE);
            mLetterTipTextView.setText((String)sideIndexList.get(itemPosition)[0]);
            listView.setSelectionFromTop((Integer)indexItem[1], 0);
        }
    }

    @SuppressLint("DefaultLocale")
    private void poplulateSideview()
    {

        String latter_temp, latter = "";
        int index = 0;
        sideIndex.removeAllViews();
        sideIndexList.clear();
        for (int i = 0; i < mCityListModel.size(); i++) {
            Object[] temp = new Object[2];
            latter_temp = ((CityListCityModel)mCityListModel.get(i)).getJianpin().substring(0, 1).toUpperCase();
            if (!latter_temp.equals(latter)) {
                // latter with its array index
                latter = latter_temp;
                temp[0] = latter;
                temp[1] = i + index;
                index++;
                sideIndexList.add(temp);

                TextView latter_txt = new TextView(this);
                latter_txt.setText(latter);

                latter_txt.setTextColor(getResources().getColor(R.color.gps_place_city));
                latter_txt.setSingleLine(true);
                // latter_txt.setTextSize(getResources().getDimension(R.dimen.index_list_font));
                latter_txt.setHorizontallyScrolling(false);
                latter_txt.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                latter_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.index_list_font));
                // LinearLayout.LayoutParams params = new
                // LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                // 1);
                // params.gravity = Gravity.CENTER_HORIZONTAL;
                //
                // latter_txt.setLayoutParams(params);
                latter_txt.setPadding(10, 0, 10, 0);

                LinearLayout layout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.city_text_view, null);
                TextView textview = (TextView)layout.findViewById(R.id.letter_tips);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
                params.gravity = Gravity.CENTER_HORIZONTAL;
                layout.setLayoutParams(params);
                textview.setText(latter);
                sideIndex.addView(layout);
            }
        }

        sideIndexSize = sideIndexList.size();

    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s)
        {
            new StandardArrayAdapter(mCityListModel).getFilter().filter(s.toString());
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            // your search logic here
        }

    };

}
