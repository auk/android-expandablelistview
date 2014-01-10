package com.example.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.LinkedList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity implements IFieldsProvider {

    List<Field> fields = new LinkedList<Field>();

    private void initFields() {
        fields.add(Field.create("id", "auk-0001"));
        fields.add(Field.create("name", "Test document"));
        fields.add(Field.create("Описание", "Test document description. Test document description. Test document description."));

        Field f = Field.group("address_gp").addChild(Field.create("Страна", "Россия")).addChild(Field.create("Город", "Томск")).addChild(Field.create("index", "634050"));
        fields.add(f);

        f = Field.create("address_gp", "Основной адрес").addChild(Field.create("Country", "Germany")).addChild(Field.create("city", "Bonn")).addChild(Field.create("index", "48153"));
        fields.add(f);

		fields.add(Field.create("Дата рождения", "26.10.1975"));
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public List<Field> getFields() {
        return fields;
    }

    public static class PlaceholderFragment extends Fragment {

        IFieldsProvider fieldsProvider = null;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            if (activity instanceof IFieldsProvider) {
                fieldsProvider = (IFieldsProvider) activity;
            }
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            FieldBaseExpandableListAdapter adapter = null;
            if (fieldsProvider != null) {
                List<Field> fields = fieldsProvider.getFields();
                adapter = new FieldBaseExpandableListAdapter(getActivity(), fields);
            }

            ExpandableListView elvMain = (ExpandableListView) getView().findViewById(R.id.elvMain);
            elvMain.setAdapter(adapter);
        }

    }
}

interface IFieldsProvider {
    List<Field> getFields();
}

